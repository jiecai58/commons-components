package com.extension.enums;

import java.io.Serializable;

/**
 * <p>
 * 业务枚举基类，约定10001、10002、10025、10026为系统异常
 * </p>
 * <p>
 * 10000-19999 共通定义
 * </p>
 * <p>
 * 20000-20999 大前端相关业务定义
 * </p>
 *
 * @author caijie
 */
public interface BizEnum extends Serializable {

    public int getCode();

    public String getName();

    public String getDesc();

}
