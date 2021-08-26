package com.util.idutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateIdBySnowflakeTest {
    public static void main(String[] args) {
        Long beginTime = System.currentTimeMillis();
        List<Long> generateOrderNo = Collections.synchronizedList(new ArrayList<Long>());
        IntStream.range(0, 100000000).parallel().forEach(i -> {
            generateOrderNo.add(GenerateIdBySnowflake.generatId());
        });

        List<Long> filterOrderNos = generateOrderNo.stream().distinct().collect(Collectors.toList());

        System.out.println("订单样例：" + generateOrderNo.get(22));
        System.out.println("生成订单数：" + generateOrderNo.size());
        System.out.println("过滤重复后订单数：" + filterOrderNos.size());
        System.out.println("重复订单数：" + (generateOrderNo.size() - filterOrderNos.size()));
        System.out.println("耗时:" + (System.currentTimeMillis() - beginTime));
    }
}
