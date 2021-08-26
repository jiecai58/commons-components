package com.util.function;

/**
 * 获取实体的某个属性名称
 * @author caijie
 */
public class SFunctionTest {
    public void xx(){
        String name = SFunction.getName(TraderWareHouseEntity::getTraderId);
    }
}
