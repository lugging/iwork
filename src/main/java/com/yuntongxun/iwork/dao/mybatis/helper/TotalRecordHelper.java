package com.yuntongxun.iwork.dao.mybatis.helper;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * 获取总记录数的工具类
 * 
 * @author liugang
 *
 */
public class TotalRecordHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TotalRecordHelper.class);

    /**
     * 保存计算总行数的值
     */
    private static ThreadLocal<Long> totalRowCountHolder = new ThreadLocal<>();

    /**
     * 保存分页查询时是否需要总记录数
     */
    private static ThreadLocal<Boolean> isNeedTotalRowCountHolder = ThreadLocal.withInitial(() -> Boolean.FALSE);


    public static boolean isNeedTotalRowCount() {
        return isNeedTotalRowCountHolder.get();
    }


    public static void setNeedTotalRowCount(Boolean isNeedTotalRowCount) {
        isNeedTotalRowCountHolder.set(isNeedTotalRowCount);
    }


    /**
     * 获取查询对象的总行数
     * 
     * @param sql 获取总行数的SQL
     * @param statementHandler
     * @param sql
     * @param connection
     * @throws Throwable
     */
   public static void setTotalRecord(String sql, PreparedStatementHandler statementHandler, Connection connection) throws Throwable {
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(sql);
            ParameterHandler parameterHandler = statementHandler.getParameterHandler();
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            long count = 0L;
            if (rs.next()) {
                count = rs.getLong(1);
            }
            LOGGER.debug("count result : [{}]", count);
            totalRowCountHolder.set(count);
        } finally {
           IOHelper.close(rs);
           IOHelper.close(countStmt);
        }
    }


    /**
     * 获取当前线程对应的分页查询的总行数
     * 
     * @return
     */
    public static Long getTotalRecord() {
        return totalRowCountHolder.get();
    }
}