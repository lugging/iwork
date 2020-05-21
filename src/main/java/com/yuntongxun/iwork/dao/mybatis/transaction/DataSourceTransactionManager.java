package com.yuntongxun.iwork.dao.mybatis.transaction;

import com.yuntongxun.iwork.dao.api.transaction.TransactionActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * 事务管理器
 *
 */
public class DataSourceTransactionManager extends org.springframework.jdbc.datasource.DataSourceTransactionManager implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceTransactionManager.class);
    private static final long serialVersionUID = 4055234567258286652L;

    private ApplicationContext applicationContext;

    private TransactionActionListener[] transactionActionListener;


    public static boolean isActualTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }


    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Map<String, TransactionActionListener> transactionActionListenerMap =
                applicationContext.getBeansOfType(TransactionActionListener.class);
        transactionActionListener = new TransactionActionListener[transactionActionListenerMap.size()];
        transactionActionListenerMap.values().toArray(transactionActionListener);
    }


    /**
     * 开启事务后会调用所有TransactionActionListener接口的实现 doBegin<br/>
     * 异常会往外抛
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        for (int i = 0; i < transactionActionListener.length; i++) {
            transactionActionListener[i].doAfterBegin();
            LOGGER.debug("{} do begin success", transactionActionListener[i]);
        }
    }


    /**
     * 提交事务后会调用所有TransactionActionListener接口的实现 doCommit<br/>
     * 出现异常会捕获，不往外抛
     */
    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        for (int i = 0; i < transactionActionListener.length; i++) {
            transactionActionListener[i].doBeforeCommit();
            LOGGER.debug("{} do before commit success", transactionActionListener[i]);
        }
        super.doCommit(status);

        for (int i = 0; i < transactionActionListener.length; i++) {
            try {
                transactionActionListener[i].doAfterCommit();
                LOGGER.debug("{} do after commit success", transactionActionListener[i]);
            } catch (Exception e) {
                LOGGER.warn("{} do after commit fail ", transactionActionListener[i], e);
            }
        }
    }


    /**
     * 回滚事务后会调用所有TransactionActionListener接口的实现doRollback方法<br/>
     * 出现异常会捕获，不往外抛
     */
    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        for (int i = 0; i < transactionActionListener.length; i++) {
            try {
                transactionActionListener[i].doBeforeRollback();
            } catch (Exception e) {
                LOGGER.debug("{} do before rollback success", transactionActionListener[i]);
            }
        }

        super.doRollback(status);

        for (int i = 0; i < transactionActionListener.length; i++) {
            try {
                transactionActionListener[i].doAfterRollback();
                LOGGER.debug("{} do rollback success", transactionActionListener[i]);
            } catch (Exception e) {
                LOGGER.warn("{} do rollback fail ", transactionActionListener[i], e);
            }
        }
    }


    /**
     * 提交或者回滚事务后会调用所有TransactionActionListener接口的实现 doCleanupAfterCompletion 方法<br/>
     * 出现异常会捕获，不往外抛
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        for (int i = 0; i < transactionActionListener.length; i++) {
            try {
                transactionActionListener[i].doCleanupAfterCompletion();
                LOGGER.debug("{} do CleanupAfterCompletion success", transactionActionListener[i]);
            } catch (Exception e) {
                LOGGER.warn("{} do CleanupAfterCompletion fail ", transactionActionListener[i], e);
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
