package com.dto.facade.request;

import lombok.Data;

/**
 * <p>
 * 分页request
 * </p>
 *
 * @author caijie
 * @since 2021/8/26 2:10 下午
 */
@Data
public class BizPageRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 当前的列表取到了第几项，首次请求由前端传入“0“，后续请求将后端返回的currentIndex透传回去。
     */
    private Integer           currentIndex     = 0;

    /**
     * 当前获取第几页数据，仅前端有页面跳转需求时必填。
     */
    private Integer           page             = 1;
    /**
     * 一次获取多少数据，仅前端有页面跳转、有每页显示多少条需求时必填。
     */
    private Integer           pageSize         = 10;

}
