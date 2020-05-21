package com.yuntongxun.iwork.dao.mybatis.interceptor;

import com.yuntongxun.iwork.dao.api.BaseEntity;
import com.yuntongxun.iwork.dao.api.model.Order;
import com.yuntongxun.iwork.dao.mybatis.DaoSupportImpl;
import com.yuntongxun.iwork.dao.mybatis.constants.Constants;
import com.yuntongxun.iwork.dao.mybatis.helper.SelectForUpdateHelper;
import com.yuntongxun.iwork.dao.mybatis.helper.TotalRecordHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 分页拦截器
 * @author liugang
 * @see
 */
public abstract class AbstractInterceptor implements Interceptor {

    final static Logger LOGGER = LoggerFactory.getLogger(AbstractInterceptor.class);

    private static final String ROW_BOUNDS = "rowBounds";
    private static final String SQL = "sql";
    private static final String DELEGATE = "delegate";
    private static final int CONNECTION_INDEX = 0;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
        PreparedStatementHandler preparedStatHandler = (PreparedStatementHandler) FieldUtils.readField(statementHandler, DELEGATE, true);
        RowBounds rowBounds = (RowBounds) FieldUtils.readField(preparedStatHandler, ROW_BOUNDS, true);
        Connection connection = (Connection) invocation.getArgs()[CONNECTION_INDEX];
        BoundSql boundSql = preparedStatHandler.getBoundSql();
        String originalSql = compress(boundSql.getSql());

        // 排序,组装sql
        Object parameter = preparedStatHandler.getParameterHandler().getParameterObject();
        if (parameter instanceof BaseEntity) {
            List<Order> orders = ((BaseEntity) parameter).getOrderBy();
            if (orders != null) {
                StringBuilder orderSql = new StringBuilder(originalSql).append(" ORDER BY ");
                Map<String, String> columnMapping = DaoSupportImpl.getPropertyColumnMapper()
                        .get(parameter.getClass().getName() + Constants.BASE_RESULT_MAP);

                for (Order order : orders) {
                    String columnName = order.getColumnName();
                    if (columnMapping != null) {
                        String mappingColumn = columnMapping.get(order.getPropertyName());
                        if (!StringUtils.isEmpty(mappingColumn)) {
                            columnName = mappingColumn;
                        }
                    }
                    orderSql.append(columnName).append(order.getSort()).append(",");
                }

                originalSql = orderSql.deleteCharAt(orderSql.length() - 1).toString();
            }
        }

        // 没有分页，直接返回原调用
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            if (SelectForUpdateHelper.isSelectForUpdate()) {
                originalSql += SelectForUpdateHelper.getUpdateSql();
            }
            FieldUtils.writeField(boundSql, SQL, originalSql, true);
            return invocation.proceed();
        }

        // 有分页

        // 1.获取总行数，将行数绑定到当前线程中
        if (TotalRecordHelper.isNeedTotalRowCount()) {
            String countSql = getCountSql(originalSql);
            TotalRecordHelper.setTotalRecord(countSql, preparedStatHandler, connection);
        }

        // 2.获取分页的结果集，重写分页sql
        String pagingSql = getPagingSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit());

        FieldUtils.writeField(boundSql, SQL, pagingSql, true);
        FieldUtils.writeField(rowBounds, "offset", RowBounds.NO_ROW_OFFSET, true);
        FieldUtils.writeField(rowBounds, "limit", RowBounds.NO_ROW_LIMIT, true);

        return invocation.proceed();
    }


    /**
     * 获取查询总数对应的SQL
     * 
     * @param querySelect
     * @return
     */
    private String getCountSql(String querySelect) {
        querySelect = compress(querySelect);
        String upperQuerySelect = querySelect.toUpperCase();
        int orderIndex = getLastOrderInsertPoint(upperQuerySelect);
        int formIndex = getAfterFormInsertPoint(upperQuerySelect);
        String select = upperQuerySelect.substring(0, formIndex);

        // 如果SELECT 中包含 DISTINCT或者GROUP BY 只能在外层包含COUNT
        if (select.indexOf("SELECT DISTINCT") != -1 || upperQuerySelect.indexOf("GROUP BY") != -1) {
            return new StringBuffer(querySelect.length()).append("select count(1) count from (")
                .append(querySelect.substring(0, orderIndex)).append(" ) t").toString();
        } else {
            return new StringBuffer(querySelect.length()).append("select count(1) count ")
                .append(querySelect.substring(formIndex, orderIndex)).toString();
        }
    }


    /**
     * 得到最后一个Order By的插入点位置
     * 
     * @return 返回最后一个Order By插入点的位置
     */
    private int getLastOrderInsertPoint(String querySelect) {
        int orderIndex = querySelect.lastIndexOf("ORDER BY");
        if (orderIndex == -1) {
            orderIndex = querySelect.length();
        } else {
            if (!isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length()))) {
                throw new RuntimeException("SQL 分页必须要有Order by 语句!");
            }
        }
        return orderIndex;
    }


    /**
     * 得到分页的SQL
     * 
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 分页SQL
     */
    protected abstract String getPagingSql(String querySelect, int pageIndex, int pageSize);


    /**
     * 将SQL语句压缩成一条语句，并且每个单词的间隔都是1个空格
     * 
     * @param sql SQL语句
     * @return 如果sql是NULL返回空，否则返回转化后的SQL
     */
    private static String compress(String sql) {
        return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
    }

    /**
     * 得到SQL第一个正确的FROM的的插入点
     */
    private static int getAfterFormInsertPoint(String querySelect) {
        String regex = "\\s+FROM\\s+";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(querySelect);
        while (matcher.find()) {
            int fromStartIndex = matcher.start(0);
            String text = querySelect.substring(0, fromStartIndex);
            if (isBracketCanPartnership(text)) {
                return fromStartIndex;
            }
        }
        return 0;
    }


    /**
     * 判断括号"()"是否匹配,并不会判断排列顺序是否正确
     * 
     * @param text 要判断的文本
     * @return 如果匹配返回TRUE, 否则返回FALSE
     */
    private static boolean isBracketCanPartnership(String text) {
        if (text == null || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
            return false;
        }
        return true;
    }


    /**
     * 得到一个字符在另一个字符串中出现的次数
     * 
     * @param text 文本
     * @param ch 字符
     */
    private static int getIndexOfCount(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            count = (text.charAt(i) == ch) ? count + 1 : count;
        }
        return count;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
