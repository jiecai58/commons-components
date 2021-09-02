package com.dto.facade.response;


import com.extension.enums.BizEnum;
import com.extension.enums.BizErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author caijie
 * @since 2021/8/26 2:10 下午
 */
@Data
public class BizResponse<T> implements Serializable {

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
     * 数据
     */
    private T data;

    public BizResponse() {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc());
    }

    public BizResponse(BizEnum errorCode) {
        this(errorCode, errorCode.getDesc());
    }

    public BizResponse(T data) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), data);
    }

    public BizResponse(BizEnum errorCode, String message) {
        this(errorCode, message, null);
    }

    public BizResponse(BizEnum errorCode, String message, T data) {
        this.code = errorCode.getCode();
        this.message = message;
        this.data = data;
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> success() {
        return new BizResponse<>();
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> success(String message) {
        return new BizResponse<>(BizErrorCodeEnum.SUCCESS, message);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> success(T data) {
        return new BizResponse<>(data);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> success(String message, T data) {
        return new BizResponse<>(BizErrorCodeEnum.SUCCESS, message, data);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> operationFailed() {
        return new BizResponse<>(BizErrorCodeEnum.OPERATION_FAILED);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> operationFailed(String message) {
        return new BizResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> operationFailed(T data) {
        return new BizResponse<>(BizErrorCodeEnum.OPERATION_FAILED, BizErrorCodeEnum.OPERATION_FAILED.getDesc(), data);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> operationFailed(String message, T data) {
        return new BizResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message, data);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> systemError() {
        return new BizResponse<>(BizErrorCodeEnum.SYSTEM_ERROR);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> systemError(String message) {
        return new BizResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> systemError(T data) {
        return new BizResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, BizErrorCodeEnum.SYSTEM_ERROR.getDesc(), data);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> systemError(String message, T data) {
        return new BizResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message, data);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> paramError() {
        return new BizResponse<>(BizErrorCodeEnum.PARAM_ERROR);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> paramError(String message) {
        return new BizResponse<>(BizErrorCodeEnum.PARAM_ERROR, message);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> paramError(T data) {
        return new BizResponse<>(BizErrorCodeEnum.PARAM_ERROR, BizErrorCodeEnum.PARAM_ERROR.getDesc(), data);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizResponse}
     */
    public static <T> BizResponse<T> paramError(String message, T data) {
        return new BizResponse<>(BizErrorCodeEnum.PARAM_ERROR, message, data);
    }

}
