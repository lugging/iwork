package com.yuntongxun.iwork.dao.api.transaction;

/**
 * 事务监听接口
 *
 * @author liugang
 */
public class DefaultTransactionActionListener implements TransactionActionListener {

    @Override
    public void doAfterBegin() {

    }


    @Override
    public void doBeforeCommit() {

    }


    @Override
    public void doAfterCommit() {

    }


    @Override
    public void doBeforeRollback() {

    }


    @Override
    public void doAfterRollback() {

    }


    @Override
    public void doCleanupAfterCompletion() {

    }


    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
