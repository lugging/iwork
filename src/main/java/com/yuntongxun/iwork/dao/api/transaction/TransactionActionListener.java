package com.yuntongxun.iwork.dao.api.transaction;

/**
 * 事务监听接口<br/>
 * @author liugang
 */
public interface TransactionActionListener {

    /**
     * 在开启事务后调用
     */
    void doAfterBegin();


    /**
     * 在提交事务后调用
     */
    void doBeforeCommit();


    /**
     * 在提交事务后调用
     */
    void doAfterCommit();


    /**
     * 在回滚事务后调用
     */
    void doBeforeRollback();


    /**
     * 在回滚事务后调用
     */
    void doAfterRollback();


    /**
     * 在提交或回滚事务后都会调用 ，doCommit/doRollback之后执行
     */
    void doCleanupAfterCompletion();

}
