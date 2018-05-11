package com.intellif.core;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 */
public class PageBean<T> implements Serializable {
    //总共的数据条数
    private Long totalCount;
    //第几页
    private Integer pageNum;
    //每页显示多少
    private Integer pageSize;
    //查询出来的数据
    private List<T> datas;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}