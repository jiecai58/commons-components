package com.dto.facade.response;

import com.extension.enums.BizEnum;
import com.extension.enums.BizErrorCodeEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 通用 扩展 response
 * </p>
 *
 * @author caijie
 * @since 2021/8/26 2:10 下午
 */
@Data
@ToString(callSuper = true)
public class BizExtResponse<T> extends BizResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * 扩展属性
     * </p>
     */
    private Map<String, Object> attachment;

    public BizExtResponse() {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc());
    }

    public BizExtResponse(BizEnum errorCode) {
        this(errorCode, errorCode.getDesc());
    }

    public BizExtResponse(T data) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), data, null);
    }

    public BizExtResponse(Map<String, Object> attachment) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), null, attachment);
    }

    public BizExtResponse(T data, Map<String, Object> attachment) {
        this(BizErrorCodeEnum.SUCCESS, BizErrorCodeEnum.SUCCESS.getDesc(), data, attachment);
    }

    public BizExtResponse(BizEnum errorCode, String message) {
        this(errorCode, message, null, null);
    }

    public BizExtResponse(BizEnum errorCode, String message, T data) {
        this(errorCode, message, data, null);
    }

    public BizExtResponse(BizEnum errorCode, String message, Map<String, Object> attachment) {
        this(errorCode, message, null, attachment);
    }

    public BizExtResponse(BizEnum errorCode, String message, T data, Map<String, Object> attachment) {
        super(errorCode, message, data);
        this.attachment = attachment;
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success() {
        return new BizExtResponse<>();
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(String message) {
        return new BizExtResponse<>(BizErrorCodeEnum.SUCCESS, message);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(T data) {
        return new BizExtResponse<>(data);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(Map<String, Object> attachment) {
        return new BizExtResponse<>(attachment);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(data, attachment);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(String message, T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.SUCCESS, message, data);
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> success(String message, T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.SUCCESS, message, data, attachment);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed() {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(String message) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, BizErrorCodeEnum.OPERATION_FAILED.getDesc(),
                data);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, BizErrorCodeEnum.OPERATION_FAILED.getDesc(),
                attachment);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, BizErrorCodeEnum.OPERATION_FAILED.getDesc(),
                data, attachment);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(String message, T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message, data);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(String message, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message, attachment);
    }

    /**
     * <p>
     * 操作失败
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> operationFailed(String message, T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.OPERATION_FAILED, message, data, attachment);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError() {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(String message) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, BizErrorCodeEnum.SYSTEM_ERROR.getDesc(), data);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, BizErrorCodeEnum.SYSTEM_ERROR.getDesc(), attachment);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, BizErrorCodeEnum.SYSTEM_ERROR.getDesc(), data,
                attachment);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(String message, T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message, data);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(String message, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message, attachment);
    }

    /**
     * <p>
     * 系统异常
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> systemError(String message, T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.SYSTEM_ERROR, message, data, attachment);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError() {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(String message) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, message);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, BizErrorCodeEnum.PARAM_ERROR.getDesc(), data);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, BizErrorCodeEnum.PARAM_ERROR.getDesc(), attachment);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, BizErrorCodeEnum.PARAM_ERROR.getDesc(), data,
                attachment);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(String message, T data) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, message, data);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(String message, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, message, attachment);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param message 信息
     * @param data 数据
     * @param attachment 扩展数据
     * @return {@link BizExtResponse}
     */
    public static <T> BizExtResponse<T> paramError(String message, T data, Map<String, Object> attachment) {
        return new BizExtResponse<>(BizErrorCodeEnum.PARAM_ERROR, message, data, attachment);
    }

}
