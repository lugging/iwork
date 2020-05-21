package com.yuntongxun.iwork.dao.mybatis;

import com.yuntongxun.iwork.dao.api.BaseEntity;
import com.yuntongxun.iwork.dao.api.DaoSupport;
import com.yuntongxun.iwork.dao.api.model.QueryResult;
import com.yuntongxun.iwork.dao.mybatis.helper.SelectForUpdateHelper;
import com.yuntongxun.iwork.dao.mybatis.helper.TotalRecordHelper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description:
* @Author: liugang
* @Date: 2020/5/20 15:50
*/
@Component
public class DaoSupportImpl extends SqlSessionDaoSupport implements DaoSupport {

    final static Logger LOGGER = LoggerFactory.getLogger(DaoSupportImpl.class);

    private static Map<String, Map<String, String>> propertyColumnMapper = new HashMap<>();

    @Autowired
    private SqlSessionFactory sessionFactory;

    /** 用于加载属性对应的字段名称 */
    @PostConstruct
    private void initPropertyColumnMapper() {
        super.setSqlSessionFactory(sessionFactory);
        Collection<ResultMap> rms = getSqlSession().getConfiguration().getResultMaps();
        Iterator<ResultMap> iter = rms.iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (object instanceof ResultMap) {
                ResultMap rm = (ResultMap) object;
                Map<String, String> map = rm.getResultMappings().stream()
                        .collect(Collectors.toMap(ResultMapping::getProperty, ResultMapping::getColumn));
                propertyColumnMapper.put(rm.getId(), map);
            }
        }
    }


    public static Map<String, Map<String, String>> getPropertyColumnMapper() {
        return propertyColumnMapper;
    }


    @Override
    public <T extends BaseEntity> Integer count(T entity) {
        String className = entity.getClass().getName();
        return getSqlSession().selectOne(className + POSTFIX_COUNT, entity);
    }

    @Override
    public <T extends BaseEntity> Integer count(String statementPostfix, T object) {
        return getSqlSession().selectOne(statementPostfix, object);
    }

    @Override
    public Integer count(String statementPostfix, Map<String, Object> parameter) {
        return getSqlSession().selectOne(statementPostfix, parameter);
    }

    @Override
    public <T extends BaseEntity> T selectOne(T entity) {
        String className = entity.getClass().getName();
        return getSqlSession().selectOne(className + POSTFIX_SELECT_ONE, entity);
    }

    @Override
    public <T extends BaseEntity> T selectOne(String statementPostfix, T object) {
        return getSqlSession().selectOne(statementPostfix, object);
    }

    @Override
    public <T extends BaseEntity> T selectOne(String statementPostfix, Map<String, Object> parameter) {
        return getSqlSession().selectOne(statementPostfix, parameter);
    }

    @Override
    public <T extends BaseEntity> T selectForUpdate(T parameter) {
        try {
            SelectForUpdateHelper.setSelectForUpdate();
            return selectOne(parameter);
        } finally {
            SelectForUpdateHelper.cancelSelectForUpdate();
        }
    }


    @Override
    public <T extends BaseEntity> T selectForUpdate(String statementPostfix, T parameter) {
        try {
            SelectForUpdateHelper.setSelectForUpdate();
            return selectOne(statementPostfix, parameter);
        } finally {
            SelectForUpdateHelper.cancelSelectForUpdate();
        }
    }


    @Override
    public <T extends BaseEntity> int insert(T entity) {
        String className = entity.getClass().getName();
        return getSqlSession().insert(className + POSTFIX_INSERT, entity);
    }


    @Override
    public <T extends BaseEntity> int insert(List<T> list) {
        int i = 0;
        for (BaseEntity entity : list) {
            i += this.insert(entity);
        }
        return i;
    }


    @Override
    public <T extends BaseEntity> int update(T entity) {
        String className = entity.getClass().getName();
        return getSqlSession().update(className + POSTFIX_UPDATE, entity);
    }


    @Override
    public <T extends BaseEntity> int update(T setParameter, T whereParameter) {
        String className = setParameter.getClass().getName();
        return update(className + POSTFIX_UPDATE_BY_ENTITY, setParameter, whereParameter);
    }


    @Override
    public <T extends BaseEntity> int update(String statementPostfix, T setParameter, T whereParameter) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("s", setParameter);
        parameter.put("w", whereParameter);
        return update(statementPostfix, parameter);
    }


    @Override
    public <T extends BaseEntity> int update(String statementPostfix, T entity) {
        return getSqlSession().update(statementPostfix, entity);
    }


    @Override
    public int update(String statementPostfix, Map<String, Object> parameter) {
        return getSqlSession().update(statementPostfix, parameter);
    }


    @Override
    public <T extends BaseEntity> int delete(T entity) {
        String className = entity.getClass().getName();
        return getSqlSession().delete(className + POSTFIX_DELETE, entity);
    }


    @Override
    public <T extends BaseEntity> int delete(String statementPostfix, T entity) {
        return getSqlSession().update(statementPostfix, entity);
    }


    @Override
    public int delete(String statementPostfix, Map<String, Object> parameter) {
        return getSqlSession().update(statementPostfix, parameter);
    }


    @Override
    public <T extends BaseEntity> List<T> selectList(T entity) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECT_LIST;
        return selectList(statementPostfix, entity);
    }


    @Override
    public <T extends BaseEntity> List<T> selectList(String statementPostfix, T entity) {
        return getSqlSession().selectList(statementPostfix, entity);
    }


    @Override
    public <T extends BaseEntity> List<T> selectList(String statementPostfix, Map<String, Object> parameter) {
        return getSqlSession().selectList(statementPostfix, parameter);
    }


    @Override
    public <T extends BaseEntity> List<T> selectList(T entity, int pageIndex, int pageSize) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECT_LIST;
        return selectList(statementPostfix, entity, pageIndex, pageSize);
    }


    @Override
    public <T extends BaseEntity> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {
            TotalRecordHelper.setNeedTotalRowCount(false);
            return getSqlSession().selectList(statementPostfix, entity, new RowBounds(pageIndex, pageSize));
        } finally {
            // 需要重新设置是否需要总记录
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }

    @Override
    public <T extends BaseEntity> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {
            TotalRecordHelper.setNeedTotalRowCount(false);
            return getSqlSession().selectList(statementPostfix, parameter, new RowBounds(pageIndex, pageSize));
        } finally {
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(V entity, String mapKey) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECT_MAP;
        return selectMap(statementPostfix, entity, mapKey);
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, V entity, String mapKey) {
        return getSqlSession().selectMap(statementPostfix, entity, mapKey);
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, Map<String, Object> parameter, String mapKey) {
        return getSqlSession().selectMap(statementPostfix, parameter, mapKey);
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(V entity, String mapKey, int pageIndex, int pageSize) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECT_MAP;
        return selectMap(statementPostfix, entity, mapKey, pageIndex, pageSize);
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, V entity, String mapKey, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {

            TotalRecordHelper.setNeedTotalRowCount(false);
            return getSqlSession().selectMap(statementPostfix, entity, mapKey, new RowBounds(pageIndex, pageSize));

        } finally {
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }

    @Override
    public <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, Map<String, Object> parameter, String mapKey, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {
            TotalRecordHelper.setNeedTotalRowCount(false);
            return getSqlSession().selectMap(statementPostfix, parameter, mapKey, new RowBounds(pageIndex, pageSize));
        } finally {
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }


    @Override
    public <T extends BaseEntity> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize) {
        String statementPostfix = entity.getClass().getName() + POSTFIX_SELECT_LIST;
        return selectQueryResult(statementPostfix, entity, pageIndex, pageSize);
    }


    @Override
    public <T extends BaseEntity> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {
            TotalRecordHelper.setNeedTotalRowCount(true);
            List<T> resultList = getSqlSession().selectList(statementPostfix, entity, new RowBounds(pageIndex, pageSize));
            Long totalRecord = TotalRecordHelper.getTotalRecord();
            return new QueryResult<T>(resultList, totalRecord);
        } finally {
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }


    @Override
    public <T extends BaseEntity> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize) {
        Boolean needTotalFlag = TotalRecordHelper.isNeedTotalRowCount();
        try {
            TotalRecordHelper.setNeedTotalRowCount(true);
            List<T> resultList = getSqlSession().selectList(statementPostfix, parameter, new RowBounds(pageIndex, pageSize));
            Long totalRecord = TotalRecordHelper.getTotalRecord();
            return new QueryResult<T>(resultList, totalRecord);
        } finally {
            TotalRecordHelper.setNeedTotalRowCount(needTotalFlag);
        }
    }
}
