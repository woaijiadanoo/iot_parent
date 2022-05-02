package com.ruyuan.jiangzh.iot.base.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class PageDTO<T> {
    // 当前页
    private long nowPage;
    // 每页条数
    private long pageSize;
    // 总记录数
    private long totals;
    // 总页数
    private long totalPages;
    // 返回的列表对象
    private List<T> records;
    // 封装的条件列表
    /*
        email - like - %gmail%
     */
    @JsonIgnore
    private Map<String, Object> conditions = Maps.newHashMap();

    public PageDTO(long nowPage, long pageSize){
        this.nowPage = nowPage;
        this.pageSize = pageSize;
    }

    // 查询出结果以后，将结果数据传入
    public void setResult(long totals,long totalPages, List<T> result){
        this.totals = totals;
        this.totalPages = totalPages;
        this.records = result;
    }

    // 加入拼接条件
    public void spellCondition(String fieldName, Object fieldValue){
        conditions.put(fieldName, fieldValue);
    }

    public long getNowPage() {
        return nowPage;
    }

    public void setNowPage(long nowPage) {
        this.nowPage = nowPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotals() {
        return totals;
    }

    public void setTotals(long totals) {
        this.totals = totals;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

}
