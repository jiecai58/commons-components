package com.dto.facade.response;


import com.dto.facade.page.PageResult;
import com.extension.enums.BizEnum;
import com.extension.enums.BizErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caijie
 * @since 2021/8/26 2:10 下午
 */
@Data
public class BizPageResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * 状态码
     * </p>
     */
    private int code;
    /**
     * 信息
     */
    private String message;
    /**
     * 分页数据
     */
    private PageResult<T> data;

    public BizPageResponse() {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc());
    }

    public BizPageResponse(BizEnum errorCode) {
        this(errorCode, errorCode.getDesc());
    }

    public BizPageResponse(PageResult<T> data) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), data);
    }

    public BizPageResponse(List<T> list, int pageNum, int pageSize, long total) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), list, pageNum, pageSize, total);
    }

    public BizPageResponse(List<T> list, int pageNum, int pageSize, long total, int currentIndex) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), list, pageNum, pageSize, total,
                currentIndex);
    }

    public BizPageResponse(BizEnum errorCode, String message) {
        this(errorCode, message, new PageResult<>());
    }

    public BizPageResponse(BizEnum errorCode, String message, PageResult<T> data) {
        this.code = errorCode.getCode();
        this.message = message;
        this.data = data;
    }

    public BizPageResponse(BizEnum errorCode, String message, List<T> list, int pageNum, int pageSize, long total) {
        this.code = errorCode.getCode();
        this.message = message;
        this.data = PageResult.of(list, pageNum, pageSize, total);
    }

    public BizPageResponse(BizEnum errorCode, String message, List<T> list, int pageNum, int pageSize, long total,
                           int currentIndex) {
        this.code = errorCode.getCode();
        this.message = message;
        this.data = PageResult.of(list, pageNum, pageSize, total, currentIndex);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success() {
        return new BizPageResponse<>();
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(String message) {
        return new BizPageResponse<>(BizErrorCodeEnum.SUCCESS, message);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param data 数据
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(PageResult<T> data) {
        return new BizPageResponse<>(data);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(List<T> list, int pageNum, int pageSize, long total) {
        return new BizPageResponse<>(list, pageNum, pageSize, total);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @param currentIndex 当前数据index
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(List<T> list, int pageNum, int pageSize, long total,
                                                 int currentIndex) {
        return new BizPageResponse<>(list, pageNum, pageSize, total, currentIndex);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(String message, PageResult<T> data) {
        return new BizPageResponse<>(BizErrorCodeEnum.SUCCESS, message, data);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(String message, List<T> list, int pageNum, int pageSize, long total) {
        return new BizPageResponse<>(BizErrorCodeEnum.SUCCESS, message, list, pageNum, pageSize, total);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param list 分页结果
     * @param pageNum 当前页
     * @param pageSize 每页数据大小
     * @param total 总条数
     * @param currentIndex 当前数据index
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> success(String message, List<T> list, int pageNum, int pageSize, long total,
                                                 int currentIndex) {
        return new BizPageResponse<>(BizErrorCodeEnum.SUCCESS, message, list, pageNum, pageSize, total, currentIndex);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> operationFailed() {
        return new BizPageResponse<>(BizErrorCodeEnum.OPERATION_FAILED);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> operationFailed(String message) {
        return new BizPageResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> systemError() {
        return new BizPageResponse<>(BizErrorCodeEnum.SYSTEM_ERROR);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> systemError(String message) {
        return new BizPageResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> paramError() {
        return new BizPageResponse<>(BizErrorCodeEnum.PARAM_ERROR);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @return {@link BizPageResponse}
     */
    public static <T> BizPageResponse<T> paramError(String message) {
        return new BizPageResponse<>(BizErrorCodeEnum.PARAM_ERROR, message);
    }

}
