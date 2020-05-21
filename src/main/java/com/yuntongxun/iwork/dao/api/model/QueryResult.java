package com.yuntongxun.iwork.dao.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果集
 * @author liugang
 */
public class QueryResult<T> implements Serializable {
    private static final long serialVersionUID = 7831476912717683294L;

    /**
     * 结果集
     */
    private List<T> results;

    /**
     * 总数
     */
    private long totalRecord;

    public QueryResult() {}

    public QueryResult(List<T> results, long totalRecord) {
        super();
        this.results = results;
        this.totalRecord = totalRecord;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }
}
