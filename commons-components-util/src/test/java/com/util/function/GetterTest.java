package com.util.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 获取对象属性和值
 * @author caijie
 */
public class GetterTest {

    public void xx(){
        ArrayList<String> objects = new ArrayList<>();
        objects.add("test");
        TraderWareHouseEntity requestData = TraderWareHouseEntity.builder()
                .traderId(objects).distance("xx").build();

        //获取对象的某个属性名称
        Getter.attributeName(requestData::getDistance);

        List<Getter> getterList = new ArrayList<>();
        getterList.add(requestData::getTraderId);
        termQuery(getterList);
    }

    private void termQuery(List<Getter> getterList) {
        if(getterList.isEmpty()){
            return;
        }
        for (Getter getter : getterList) {
            BiConsumer<String, Object> keyAndValue = (key, value) -> {
                //打印属性名称和对应值
                System.out.printf("key:"+ key + "value" + value);
            };
            Getter.use(getter, keyAndValue);
        }
    }


}
