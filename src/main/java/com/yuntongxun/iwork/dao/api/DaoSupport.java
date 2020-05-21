package com.yuntongxun.iwork.dao.api;

import com.yuntongxun.iwork.dao.api.model.QueryResult;

import java.util.List;
import java.util.Map;

/** 
* @Description:  DAO
* @Author: liugang
* @Date: 2020/5/20 15:25
*/
public interface DaoSupport {
    String POSTFIX_COUNT = ".count";
    String POSTFIX_SELECT_ONE = ".selectOne";
    String POSTFIX_INSERT = ".insert";
    String POSTFIX_UPDATE = ".update";
    String POSTFIX_UPDATE_BY_ENTITY = ".updateByEntity";
    String POSTFIX_DELETE = ".delete";
    String POSTFIX_SELECT_LIST = ".selectList";
    String POSTFIX_SELECT_MAP = ".selectMap";

    /**
     * 查询
     * @param parameter
     * @return
     */
    <T extends BaseEntity> Integer count(T parameter);


    /**
     * 查询统计
     * 
     * @param statementPostfix
     * @param parameter PO对象
     * @return
     */
    <T extends BaseEntity> Integer count(String statementPostfix, T parameter);


    /**
     * 查询统计
     * 
     * @param statementPostfix
     * @param parameter
     * @return
     */
    Integer count(String statementPostfix, Map<String, Object> parameter);


    /**
     * 查询单条数据
     * @param parameter
     * @return 
    */
    <T extends BaseEntity> T selectOne(T parameter);


    /**
     * 查询加锁
     * 
     * @param parameter
     * @return 单个对象
     */
    <T extends BaseEntity> T selectForUpdate(T parameter);


    <T extends BaseEntity> T selectForUpdate(String statementPostfix, T parameter);


    <T extends BaseEntity> T selectOne(String statementPostfix, T parameter);


    <T extends BaseEntity> T selectOne(String statementPostfix, Map<String, Object> parameter);


    /**
     * 插入一条记录
     * 
     * @param parameter
     * @return @
     */
    <T extends BaseEntity> int insert(T parameter);


    /**
     * 插入List
     * 
     * @param entity
     * @return @
     */
    <T extends BaseEntity> int insert(List<T> entity);


    /**
     * 更新一个实体
     * 
     * @param parameter
     * @return @
     */
    <T extends BaseEntity> int update(T parameter);


    /**
     * 更新对象
     * 
     * @param setParameter update语句中用于set的参数
     * @param whereParameter update语句中用于where条件的参数
     * @return @
     */
    <T extends BaseEntity> int update(T setParameter, T whereParameter);


    <T extends BaseEntity> int update(String statementPostfix, T setParameter, T whereParameter);


    /**
     * 更新对象
     * 
     * @param statementPostfix 自定义sql ID
     * @param parameter 参数对象
     * @return 更新的记录数 @
     */
    <T extends BaseEntity> int update(String statementPostfix, T parameter);


    /**
     * 更新对象
     * 
     * @param statementPostfix 自定义sql ID
     * @param parameter
     * @return 更新的记录数 @
     */
    int update(String statementPostfix, Map<String, Object> parameter);


    /**
     * 删除
     * 
     * @param parameter 参数
     * @return @
     */
    <T extends BaseEntity> int delete(T parameter);


    /**
     * 自定义的删除
     * 
     * @param statementPostfix
     * @param parameter
     * @return @
     */
    <T extends BaseEntity> int delete(String statementPostfix, T parameter);


    int delete(String statementPostfix, Map<String, Object> parameter);


    /**
     * 查询一个列表， 不分页
     * 
     * @param entity 实体对象
     * @return
     */
    <T extends BaseEntity> List<T> selectList(T entity);


    /**
     * 查询一个列表， 不分页
     */
    <T extends BaseEntity> List<T> selectList(String statementPostfix, T entity);


    /**
     * 查询一个列表， 不分页
     */
    <T extends BaseEntity> List<T> selectList(String statementPostfix, Map<String, Object> parameter);


    /**
     * 分页查询 ,pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     * 
     * @param entity
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return
     */
    <T extends BaseEntity> List<T> selectList(T entity, int pageIndex, int pageSize);


    /**
     * 分页查询 ,pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     * 
     * @param statementPostfix
     * @param entity
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return
     */
    <T extends BaseEntity> List<T> selectList(String statementPostfix, T entity, int pageIndex, int pageSize);


    /**
     * 分页查询 ,pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     *
     * @param statementPostfix
     * @param parameter
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return
     */
    <T extends BaseEntity> List<T> selectList(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);

    /**
     * 查询一个列表，并放入Map，key为指定的表中任意一个字段，不分页
     * @param entity
     * @param mapKey
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(V entity, String mapKey);

    /**
     * 查询一个列表，并放入Map，key为指定的表中任意一个字段，不分页
     * @param statementPostfix
     * @param entity
     * @param mapKey
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, V entity, String mapKey);

    /**
     * 查询一个列表，并放入Map，key为指定的表中任意一个字段，不分页
     * @param statementPostfix
     * @param parameter
     * @param mapKey
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, Map<String, Object> parameter, String mapKey);

    /**
     * 分页查询一个列表，并放入Map，key为指定的表中任意一个字段，pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     * @param entity
     * @param mapKey
     * @param pageIndex
     * @param pageSize
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(V entity, String mapKey, int pageIndex, int pageSize);

    /**
     * 分页查询一个列表，并放入Map，key为指定的表中任意一个字段，pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     * @param statementPostfix
     * @param entity
     * @param mapKey
     * @param pageIndex
     * @param pageSize
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, V entity, String mapKey, int pageIndex, int pageSize);

    /**
     * 分页查询一个列表，并放入Map，key为指定的表中任意一个字段，pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     * @param statementPostfix
     * @param parameter
     * @param mapKey
     * @param pageIndex
     * @param pageSize
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V extends BaseEntity> Map<K, V> selectMap(String statementPostfix, Map<String, Object> parameter, String mapKey, int pageIndex, int pageSize);

    /**
     * 分页查询 ,pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     *
     * @param entity
     * @param pageIndex
     * @param pageSize
     * @return 分页查询的结果列表和总记录数
     */
    <T extends BaseEntity> QueryResult<T> selectQueryResult(T entity, int pageIndex, int pageSize);


    /**
     * 分页查询 pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     *
     * @param statementPostfix
     * @param entity
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return 查询结果集，包括总记录数
     */
    <T extends BaseEntity> QueryResult<T> selectQueryResult(String statementPostfix, T entity, int pageIndex, int pageSize);


    /**
     * 分页查询 pageIndex和pageSize构建成RowBounds， 针对RowBounds做了拦截重写sql
     *
     * @param statementPostfix
     * @param parameter
     * @param pageIndex
     * @param pageSize
     * @return 查询结果集，包括总记录数
     */
    <T extends BaseEntity> QueryResult<T> selectQueryResult(String statementPostfix, Map<String, Object> parameter, int pageIndex, int pageSize);
}
