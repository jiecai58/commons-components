package com.util.function;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author caijie
 */
@Builder
@Data
public class TraderWareHouseEntity {
    private List<String> traderId;

    private String distance;
}
