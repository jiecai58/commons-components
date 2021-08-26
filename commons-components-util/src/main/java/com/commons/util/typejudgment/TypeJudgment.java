package com.util.typejudgment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Array;
import java.util.ArrayList;

/**
 * @author caijie
 */
public class TypeJudgment {

    public static boolean isString(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof String;
    }

    public static boolean isInteger(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Integer;
    }

    public static boolean isLong(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Long;
    }

    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Array;
    }

    public static boolean isArrayList(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof ArrayList;
    }

    public static boolean isJsonObject(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof JsonObject;
    }

    public static boolean isJsonArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof JsonArray;
    }
}
