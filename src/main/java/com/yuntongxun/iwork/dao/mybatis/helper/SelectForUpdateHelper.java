package com.yuntongxun.iwork.dao.mybatis.helper;

/**
 * @author liugang
 */
public class SelectForUpdateHelper {

    private static String updateSql = " FOR UPDATE ";

    private static final String WAIT = "WAIT";

    /**
     * 保存分页查询时是否需要总记录数
     */
    private static ThreadLocal<Boolean> selectForUpdateHolder = ThreadLocal.withInitial(() -> Boolean.FALSE);

    public static boolean isSelectForUpdate() {
        return selectForUpdateHolder.get();
    }

    public static void setSelectForUpdate() {
        selectForUpdateHolder.set(true);
    }

    public static void cancelSelectForUpdate() {
        selectForUpdateHolder.set(false);
    }

    public static String getUpdateSql() {
        return updateSql;
    }

    /** waitTime 需要配置为 "wait 30" */
    public void setWaitTime(String waitTime) {
        String updateSql = waitTime.toUpperCase();
        if (updateSql.length() > 0 && updateSql.indexOf(WAIT) == -1) {
            updateSql = "WAIT " + waitTime;
        }
        SelectForUpdateHelper.updateSql = " FOR UPDATE " + updateSql;
    }
}