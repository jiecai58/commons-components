package com.util.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public final class Lists {
    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    static int computeArrayListCapacity(int arraySize) {
        return saturatedCast(5L + (long)arraySize + (long)(arraySize / 10));
    }

    public static int saturatedCast(long value) {
        if (value > 2147483647L) {
            return 2147483647;
        } else {
            return value < -2147483648L ? -2147483648 : (int)value;
        }
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList();
    }





}
