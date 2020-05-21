package com.yuntongxun.iwork.dao.api.model;

import java.io.Serializable;


/**
 * 排序对象
 * 
 * @author liugang
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -9052322409478952879L;

    /**
     * 升序
     * @param propertyName
     */
    public static Order asc(String propertyName) {
        return new Order(propertyName, " ASC");
    }

    /**
     * 降序
     * @param propertyName
     * @return
     */
    public static Order desc(String propertyName) {
        return new Order(propertyName, " DESC");
    }

    /** 属性名称 */
    private String propertyName;

    /** 表字段名 */
    private String columnName;

    /** asc | desc **/
    private String sort;

    private Order(String propertyName, String sort) {
        this.propertyName = propertyName;
        this.columnName = propertyName;
        this.sort = sort;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getSort() {
        return sort;
    }

}
