/*
 * Copyright 2020  All right reserved. This software is the
 * confidential and proprietary information of xx ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 *
 */
package com.dto.facade.page;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分页结果
 * </p>
 *
 * @author caijie
 * @since 2021/8/26 2:10 下午
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * 当前页
     * </p>
     */
    private int pageNum;

    /**
     * <p>
     * 每页的数量
     * </p>
     */
    private int pageSize;

    /**
     * <p>
     * 业务数据
     * </p>
     */
    private List<T> results = new ArrayList<>();

    /**
     * <p>
     * 总条数
     * </p>
     */
    private long total;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 当前数据index
     */
    private int currentIndex;

    /**
     * <p>
     * 是否还有数据
     * </p>
     */
    private boolean hasNext = false;

    public PageResult() {
    }

    /**
     * <p>
     * 包装分页对象
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     */
    public PageResult(List<T> list, int pageNum, int pageSize, long total) {
        this(list, pageNum, pageSize, total, 0);
    }

    /**
     * <p>
     * 包装分页对象
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @param currentIndex 当前数据index
     */
    public PageResult(List<T> list, int pageNum, int pageSize, long total, int currentIndex) {
        this.results = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.currentIndex = currentIndex;
        this.total = total;
        calculatePageCount(pageSize, total);

        //判断页面边界
        judgePageBoundary();
    }

    /**
     * <p>
     * 构建PageResult
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> of(List<T> list, int pageNum, int pageSize, long total) {
        return new PageResult<>(list, pageNum, pageSize, total);
    }

    /**
     * <p>
     * 构建PageResult
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @param currentIndex 当前数据index
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> of(List<T> list, int pageNum, int pageSize, long total, int currentIndex) {
        return new PageResult<>(list, pageNum, pageSize, total, currentIndex);
    }

    /**
     * <p>
     * 计算总页数
     * </p>
     *
     * @param pageSize 每页大小
     * @param total 记录总数
     */
    private void calculatePageCount(int pageSize, long total) {
        if (total == -1) {
            this.pageCount = 1;
            return;
        }
        if (pageSize > 0) {
            this.pageCount = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        } else {
            this.pageCount = 0;
        }
    }

    /**
     * <p>
     * 判定页面边界
     * </p>
     */
    private void judgePageBoundary() {
        this.hasNext = this.pageNum < this.pageCount;
    }
}
