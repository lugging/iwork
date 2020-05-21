package com.yuntongxun.iwork.dao.api;

import com.yuntongxun.iwork.dao.api.model.Order;

import java.io.Serializable;
import java.util.*;

/**
 * 持久化对象基类
 * @author liugang
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 3967714311150833256L;

    /** 查询开始日期 */
    private Date queryBeginDate;

    /** 查询结束日期 */
    private Date queryEndDate;

    /** 排序 */
    private List<Order> orderBy;

    /** 附加属性 */
    private Map<String, Object> additionalParameter;

    /** 查询开始日期 */
    public Date getQueryBeginDate() {
        return queryBeginDate;
    }

    /** 查询开始日期 */
    public void setQueryBeginDate(Date queryBeginDate) {
        this.queryBeginDate = queryBeginDate;
    }

    /** 查询结束日期 */
    public Date getQueryEndDate() {
        return queryEndDate;
    }

    /** 查询结束日期 */
    public void setQueryEndDate(Date queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public final List<Order> getOrderBy() {
        return orderBy;
    }

    final void setOrderBy(List<Order> orderBy) {
        this.orderBy = orderBy;
    }

    public void addOrder(Order order) {
        if (orderBy == null) {
            orderBy = new ArrayList<Order>();
        }
        orderBy.add(order);
    }

    public Map<String, Object> getAdditionalParameter() {
        return additionalParameter;
    }

    public void putAdditionalParameter(String key, Object parameterValue) {
        if (additionalParameter == null) {
            additionalParameter = new HashMap<>(3);
        }
        additionalParameter.put(key, parameterValue);
    }
}
