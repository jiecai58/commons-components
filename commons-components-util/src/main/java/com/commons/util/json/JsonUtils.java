package com.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  Gson json util
 * @author Jie.cai58@gmail.com
 * @date 2019/9/19 14:19
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final Gson g;
    private static final Pattern PATTERN = Pattern.compile("^__(\\d+)__$");
    private static final String TYPE="type";
    private static final String PROPERTY = "property";
    private static final String ITEM = "item";
    private static final String REQUIRED = "required";

    static {
        g = new GsonBuilder()
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
		        .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
					@Override
					public JsonElement serialize(Double src, java.lang.reflect.Type typeOfSrc,
                                                 JsonSerializationContext context) {
						if (src == src.longValue()) {
                            return new JsonPrimitive(src.longValue());
                        }
		                return new JsonPrimitive(src);  
					}  
		        }).create();
    }

    public static Gson getGson() {
        return g;
    }

    /**
     * JavaBean to Json with pretty-print, usually for logger out
     */
    public static String toPrettyPrintJson(Object o) {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
                .toJson(o);
    }

    /**
     * trans json object string like "{\"a\": 1, ...}" to JavaBean of classOfT
     * return null if fail to trans, no exception out
     */
    public static <T> T fromJsonObject(String json, Class<T> classOfT) {
        return fromJsonObject(json, classOfT, true);
    }

    /**
     * parse json object string like "{\"a\": 1, ...}" to JavaBean of classOfT
     *
     * @param suppressException:
     *      false - RunTimeException out,
     *      true  - no exception out just return null if trans fail
     */
    public static <T> T fromJsonObject(String json, Class<T> classOfT, boolean suppressException) {
        try {
            return g.fromJson(json, classOfT);
        }
        catch (Exception e) {
            if (suppressException) {
                logger.warn("[fromJsonObject] fail to parse string to json object, string = {}, "
                        + "just suppressException and return null, exception is: ", json, e);
                return null;
            }
            else {
                logger.error("[fromJsonObject] fail to parse string to json object, string = {},  exception is: ", json, e);
                throw new RuntimeException("fail to parse string to json object");
            }
        }
    }

    /**
     * parse json array string like "[{\"a\": 1, ...}, ...]" to List<classOfT>
     * return null if fail to trans, no exception out
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> classOfT) {
        return fromJsonArray(json, classOfT, true);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> classOfT, boolean suppressException) {
        try {
            return g.fromJson(json, new TypeToken<List<T>>() { }.getType());
        }
        catch (Exception e) {
            if (suppressException) {
                logger.warn("[fromJsonArray] fail to parse string to json array, string = {}, "
                        + "just suppressException and return null, exception is: ", json, e);
                return null;
            }
            else {
                logger.error("[fromJsonArray] fail to parse string to json array, string = {}, exception is: ", json, e);
                throw new RuntimeException("fail to parse string to json array");
            }
        }
    }

    /**
     * JaveBean to Json
     */
    public static String toJson(Object o) {
        return g.toJson(o);
    }

    /**
     * JsonElement from gson is a good stuff, it represent a json-element
     * which could be a string/number/boolean/... or a jsonObject, jsonArray, and jsonNull
     * JsonElement is usually used for unknown type of the inputting json string
     *
     * Notice:
     *  Although json-element could represent any valid json-element, doesn't mean it won't throw exception out
     *  It will throw RuntimeException: JsonSyntaxException out when Malformed Json met, like:
     *      "{a:1}\n{b:2}" ==> JsonSyntaxException out, can use JsonReader().setLenient(true) to avoid
     */
    public static JsonElement parseJson(String json) {
        return parseJson(json, true, true);
    }

    public static JsonElement parseJson(String json, boolean isLenient, boolean suppressException) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            return new JsonParser().parse(json);
        }
        catch (JsonSyntaxException e) {
            if (isLenient) {
                logger.warn("[parseJson] met malformed json: {}, use lenient mode to parse again, exception is: ", json, e);
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                return new JsonParser().parse(reader);
            }
            else if (suppressException) {
                logger.warn("[parseJson] met malformed json: {}, just stress exception and return null, exception is: ", json, e);
                return null;
            }
            else {
                logger.error("[parseJson] met malformed json: {}, exception is: ", json, e);
                throw new RuntimeException("this is not a valid json in strict mode !");
            }
        }
    }

    public static JsonElement parseJsonFromObj(Object obj) {
        return parseJsonFromObj(obj, true, true);
    }

    public static JsonElement parseJsonFromObj(Object obj, boolean isLenient, boolean suppressException) {
        return parseJson(toJson(obj), isLenient, suppressException);
    }

    public static class WrongJsonTypeException extends RuntimeException {
        private static final long serialVersionUID = 2616243687206003197L;

        public WrongJsonTypeException(String msg) {
            super(msg);
        }
    }

    /**
     * JsonElement to JsonObject if can,
     * otherwise, return null
     */
    public static JsonObject toJsonObj(JsonElement element) {
        return toJsonObj(element, null);
    }

    public static JsonObject toJsonObj(JsonElement element, JsonObject defaultVal) {
        return toJsonObj(element, defaultVal, true);
    }

    public static JsonObject toJsonObj(JsonElement element, boolean suppressException) {
        return toJsonObj(element, null, suppressException);
    }
    public static JsonObject toJsonObj(JsonElement element, JsonObject defaultVal, boolean suppressException) {
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a JsonObject !");
        }
    }

    /**
     * JsonElement to JsonArray if can,
     * otherwise, return null
     */
    public static JsonArray toJsonArr(JsonElement element) {
        return toJsonArr(element, null);
    }

    public static JsonArray toJsonArr(JsonElement element, JsonArray defaultVal) {
        return toJsonArr(element, defaultVal, true);
    }

    public static JsonArray toJsonArr(JsonElement element, boolean suppressException) {
        return toJsonArr(element, null, suppressException);
    }

    public static JsonArray toJsonArr(JsonElement element, JsonArray defaultVal, boolean suppressException) {
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a JsonArray !");
        }
    }

    /**
     * JsonElement to String if can,
     * otherwise, return null
     */
    public static String toPrimitiveString(JsonElement element) {
        return toPrimitiveString(element, null);
    }

    public static String toPrimitiveString(JsonElement element, String defaultVal) {
        return toPrimitiveString(element, defaultVal, true);
    }

    public static String toPrimitiveString(JsonElement element, boolean suppressException) {
        return toPrimitiveString(element, null, suppressException);
    }

    public static String toPrimitiveString(JsonElement element, String defaultVal, boolean suppressException) {
        if (element != null && element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a String !");
        }
    }

    /**
     * JsonElement to Long if can,
     * otherwise, return null
     */
    public static Long toPrimitiveLong(JsonElement element) {
        return toPrimitiveLong(element, null);
    }

    public static Long toPrimitiveLong(JsonElement element, Long defaultVal) {
        return toPrimitiveLong(element, defaultVal, true);
    }

    public static Long toPrimitiveLong(JsonElement element, boolean suppressException) {
        return toPrimitiveLong(element, null, suppressException);
    }

    public static Long toPrimitiveLong(JsonElement element, Long defaultVal, boolean suppressException) {
        if (element != null && element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsLong();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a Long !");
        }
    }

    /**
     * JsonElement to Double if can,
     * otherwise, return null
     */
    public static Double toPrimitiveDouble(JsonElement element) {
        return toPrimitiveDouble(element, null);
    }

    public static Double toPrimitiveDouble(JsonElement element, Double defaultVal) {
        return toPrimitiveDouble(element, defaultVal, true);
    }

    public static Double toPrimitiveDouble(JsonElement element, boolean suppressException) {
        return toPrimitiveDouble(element, null, suppressException);
    }

    public static Double toPrimitiveDouble(JsonElement element, Double defaultVal, boolean suppressException) {
        if (element != null && element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsDouble();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a Double !");
        }
    }

    /**
     * JsonElement to Boolean if can,
     * otherwise, return null
     */
    public static Boolean toPrimitiveBoolean(JsonElement element) {
        return toPrimitiveBoolean(element, null);
    }

    public static Boolean toPrimitiveBoolean(JsonElement element, Boolean defaultVal) {
        return toPrimitiveBoolean(element, defaultVal, true);
    }

    public static Boolean toPrimitiveBoolean(JsonElement element, boolean suppressException) {
        return toPrimitiveBoolean(element, null, suppressException);
    }

    public static Boolean toPrimitiveBoolean(JsonElement element, Boolean defaultVal, boolean suppressException) {
        if (element != null && element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        else if (suppressException) {
            return defaultVal;
        }
        else {
            throw new WrongJsonTypeException("this json element is not a Boolean !");
        }
    }

    /**
     * JsonObj copy property
     */
    public static void copyPropery(JsonObject obj, JsonObject other) {
        if (obj == null || other == null) {
            return;
        }

        for (Entry<String, JsonElement> e : other.entrySet()) {
            obj.add(e.getKey(), e.getValue());
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // spec part
    // ---------------------------------------------------------------------------------------------------------------------
    /**
     * to define a json schema, constraint its structure, spec comes out
     * for the following json:
     * json = {
     *   head: {
     *      title: "",
     *      logo: {
     *          a: "",
     *          b: ""
     *      },
     *      list: [
     *          {
     *              text: "",
     *              options: 1
     *          },
     *          {
     *              text: "",
     *              options: 2
     *          }
     *      ]
     *   },
     *   body: {
     *      content: [
     *          {
     *              title: "",
     *              art: "",
     *              show: true,
     *              ids: [1,2,3]
     *          },
     *          {
     *              title: "",
     *              art: "",
     *              show: false,
     *              ids: [4,5,6]
     *          }
     *      ],
     *      total: 1000,
     *      days: ["2014-06-12 10:11:12", "2015-10-20 10:11:23"],
     *      opts: [
     *          [{"aaa": 1, "bbb":2}, {"aaa": 3, "bbb": 4}],
     *          [{"aaa": 5, "bbb":6}, {"aaa": 7, "bbb": 8}],
     *          [{"aaa": 9, "bbb":0}, {"aaa": 1, "bbb": 2}]
     *      ]
     *   }
     * }
     * the corresponding spec is:
     * spec = [
     *     {
     *         "name": "head",
     *         "type": "object",
     *         "require": true
     *         "property": [
     *             {
     *                 "name": "title",
     *                 "type": "string",
     *                 "require": false
     *             },
     *             {
     *                 "name": "logo",
     *                 "type": "object",
     *                 "property": [
     *                     {
     *                         "name": "a",
     *                         "type": "string"
     *                     },
     *                     {
     *                         "name": "b",
     *                         "type": "string"
     *                     }
     *                 ]
     *             },
     *             {
     *                 "name": "list",
     *                 "type": "array",
     *                 "item": {
     *                     "type": "object",
     *                     "property": [
     *                         {
     *                             "name": "text",
     *                             "type": "string"
     *                         },
     *                         {
     *                             "name": "options",
     *                             "type": "number"
     *                         }
     *                     ]
     *                 }
     *             }
     *         ]
     *     },
     *     {
     *         "name": "body",
     *         "type": "object",
     *         "property": [
     *             {
     *                 "name": "content",
     *                 "type": "array",
     *                 "item": {
     *                     "type": "object",
     *                     "property": [
     *                         {
     *                             "name": "title",
     *                             "type": "string"
     *                         },
     *                         {
     *                             "name": "art",
     *                             "type": "string"
     *                         },
     *                         {
     *                             "name": "show",
     *                             "type": "boolean"
     *                         },
     *                         {
     *                             "name": "ids",
     *                             "type": "array",
     *                             "item": {
     *                                 "type": "number"
     *                             }
     *                         }
     *                     ]
     *                 }
     *             },
     *             {
     *                 "name": "total",
     *                 "type": "number"
     *             },
     *             {
     *                 "name": "days",
     *                 "type": "array",
     *                 "item": {
     *                     "type": "string"
     *                 }
     *             },
     *             {
     *                 "name": "opts",
     *                 "type": "array",
     *                 "item": {
     *                     "type": "array",
     *                     "item": {
     *                         "type": "object",
     *                         "property": [
     *                             {
     *                                 "name": "aaa",
     *                                 "type": "number"
     *                             },
     *                             {
     *                                  "name": "bbb",
     *                                  "type": "number"
     *                             }
     *                         ]
     *                     }
     *                 }
     *             },
     *         ]
     *     }
     * ]
     *
     * I provide check method to ensure json schema strictly followed by spec: checkJsonBySpec
     * and also the method to gen spec by json exmaple: genSpecByJson (TODO)
     *
     * @author liuguodong01
     *
     */
    public static boolean checkJsonBySpec(JsonElement json, String spec) {
        return checkJsonBySpec(json, parseJson(spec));
    }

    public static boolean checkJsonBySpec(JsonElement json, JsonElement spec) {
        if (json == null || spec == null) {
            return true;
        }

        if (json.isJsonObject() && spec.isJsonArray()) {
            return checkJsonObjectBySpec(json.getAsJsonObject(), spec.getAsJsonArray(), null);
        }

        if (json.isJsonArray() && spec.isJsonObject()) {
            return checkJsonArrayBySpec(json.getAsJsonArray(), spec.getAsJsonObject(), null);
        }

        return false;
    }

    private static boolean checkJsonObjectBySpec(JsonObject jsonObj, JsonArray specs, String jsonPath) {
        if (jsonObj == null || specs == null) {
            // uncomplete input, regard tihs as pass
            return true;
        }

        if (jsonPath == null) {
            jsonPath = "";
        }

        for (int i = 0; i < specs.size(); i++) {
            JsonElement s = specs.get(i);
            if (!s.isJsonObject()) {
                logger.warn("[checkJsonObjectBySpec] the spec's format is fucking wrong !");
                return false;
            }
            JsonObject spec = s.getAsJsonObject();

            if (!spec.has("name") || !spec.get("name").isJsonPrimitive()
                    || !spec.get("name").getAsJsonPrimitive().isString()) {
                logger.warn("[checkJsonObjectBySpec] the spec's format is fucking wrong, "
                        + "name is a must and should be string !");
                return false;
            }
            String name = spec.get("name").getAsString();

            String localJsonPath = new String(jsonPath);
            if (StringUtils.isEmpty(localJsonPath)) {
                localJsonPath = name;
            } else {
                localJsonPath += "." + name;
            }

            if (jsonObj.has(name)) {
                JsonElement element = jsonObj.get(name);
                if (!checkJsonElementBySpec(element, spec, localJsonPath)) {
                    return false;
                }
            } else if (isJsonElementRequired(spec)) {
                logger.warn("[checkJsonObjectBySpec] missing path: {}", localJsonPath);
                return false;
            }
        }

        return true;
    }

    private static boolean checkJsonArrayBySpec(JsonArray jsonArr, JsonObject spec, String jsonPath) {
        if (jsonArr == null || spec == null) {
            return true;
        }

        for (int i = 0; i < jsonArr.size(); i++) {
            JsonElement element = jsonArr.get(i);

            // for array index, the json path should be __X__
            String indexName = fromArrayIndex2JsonPath(i);
            String localJsonPath = new String(jsonPath);
            if (StringUtils.isEmpty(localJsonPath)) {
                localJsonPath = indexName;
            } else {
                localJsonPath += "." + indexName;
            }

            if (!checkJsonElementBySpec(element, spec, localJsonPath)) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkJsonElementBySpec(JsonElement element, JsonObject spec, String jsonPath) {
        if (element == null || spec == null) {
            return true;
        }

        /*
         * Update: change type from 'must' to 'allow'
         *
         *  when no type specified in spec, just let it pass
         */
        if (!spec.has(TYPE) || !spec.get(TYPE).isJsonPrimitive()
                || !spec.get("type").getAsJsonPrimitive().isString()) {
            // LOG.warn("[checkJsonElementBySpec] the spec's format is fucking wrong, "
            //             + "'type' is a must and should be string !");
            // return false;
            return true;
        }

        String type = spec.get(TYPE).getAsString();
        switch(type) {
            case "number":
                if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
                    logger.warn("[checkJsonElementBySpec] the element is not a number, @{}", jsonPath);
                    return false;
                }
                break;
            case "string":
                if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                    logger.warn("[checkJsonElementBySpec] the element is not a string, @{}", jsonPath);
                    return false;
                }
                break;
            case "boolean":
                if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean()) {
                    logger.warn("[checkJsonElementBySpec] the element is not a boolean, @{}", jsonPath);
                    return false;
                }
                break;
            case "object":
                if (element.isJsonNull()) {
                    if (isJsonElementRequired(spec)) {
                        logger.warn("[checkJsonElementBySpec] the element is null, but is required, @{}", jsonPath);
                        return false;
                    }
                }
                else if (element.isJsonObject()) {
                    if (spec.has(PROPERTY)) {
                        if (!spec.get(PROPERTY).isJsonArray()) {
                            logger.warn("[checkJsonElementBySpec] the spec's format is fucking wrong, "
                                    + "'property' should be an array under object type @{}!", jsonPath);
                            return false;
                        }
                        if (!checkJsonObjectBySpec(element.getAsJsonObject(),
                                spec.get("property").getAsJsonArray(), jsonPath)) {
                            return false;
                        }
                    }
                }
                else {
                    logger.warn("[checkJsonElementBySpec] the element is not an object, @{}", jsonPath);
                    return false;
                }
                break;
            case "array":
                if (element.isJsonNull()) {
                    if (isJsonElementRequired(spec)) {
                        logger.warn("[checkJsonElementBySpec] the element is null, but is required, @{}", jsonPath);
                        return false;
                    }
                }
                if (element.isJsonArray()) {
                    if (spec.has(ITEM)) {
                        if (!spec.get(ITEM).isJsonObject()) {
                            logger.warn("[checkJsonElementBySpec] the spec's format is fucking wrong, "
                                    + "'item' should be an object under array type @{}!", jsonPath);
                            return false;
                        }
                        if (!checkJsonArrayBySpec(element.getAsJsonArray(),
                                spec.get("item").getAsJsonObject(), jsonPath)) {
                            return false;
                        }
                    }
                } else {
                    logger.warn("[checkJsonElementBySpec] the element is not an array, @{}", jsonPath);
                    return false;
                }
                break;
            default:
                logger.warn("[checkJsonElementBySpec] the spec's format is fucking wrong, unkonwn type {}", type);
                return false;
        }
        return true;
    }

    private static boolean isJsonElementRequired(JsonObject spec) {
        if (spec == null) {
            // by default always required
            return true;
        }

        if (spec.has(REQUIRED)) {
            if (spec.get(REQUIRED).isJsonPrimitive()
                    && spec.get(REQUIRED).getAsJsonPrimitive().isBoolean()) {
                if (spec.get(REQUIRED).getAsBoolean() == true) {
                    return true;
                } else {
                    // Yes, this is the only branch which will return false:
                    // spec has required, and the value must be false !
                    return false;
                }
            } else {
                logger.warn("[isJsonElementRequired] the spec's format fucking wrong, required should be boolean");
                // spec has 'required', but not a boolean, regard it as required
                return true;
            }
        }

        // no 'required' specified, that means 'required' !
        return true;
    }

    /**
     * TODO
     */
    public static JsonElement genSpecByJson(JsonElement json) {
        return null;
    }


    public static String fromArrayIndex2JsonPath(int index) {
        return "__" + index + "__";
    }

    /**
     * parse array index json path to real array index
     *   __X__ ==> x
     *
     * @return: -1 means this is not a arrayIndex
     */
    public static int fromJsonPath2ArrayIndex(String path) {
        Matcher m = PATTERN.matcher(path);
        if (m.find()) {
            return Integer.valueOf(m.group(1));
        }
        return -1;
    }

    /**
     * Special part
     * Note: JSONObject.getXXX() will throw runtime exception, no matter whether json or gson is
     *       In order to suppress this and meanwhile to support json path getter,
     *       the following method out
     * @Author:
     *
     *
     * param obj: could be:
     *              json string,
     *              JSONObject,
     *              JSONArray,
     *              JsonElement,
     *              JsonObject,
     *              JsonArray,
     *              JsonPrimitive,
     *              JsonNull
     * param path: json path, if empty or null, means itself
     *                  {
     *                      "a": {
     *                          "b": [
     *                              {
     *                                  "c": 1,
     *                                  "d": {}
     *                              },
     *                              {
     *                                  "c": 2,
     *                                  "d": {},
     *                                  "e": "balabala"
     *                              }
     *                          ]
     *                      }
     *                  }
     *              to locate to "e", json path will be: a.b.__1__.e
     *
     * All the following func, internally use jsonElement actually !
     *
     * Notice:
     *      If no default value speicified, always regard null as default
     *      and this will sometimes make caller puzzle,
     *      one doesn't know the return value null means the path is invalid or just null there
     *      e.g.
     *          getString("{\"a\": {\"b\": 123}", "a.b.c");
     *              ==> return null, actually json path a.b.c invalid
     *          getString("{\"a\": {\"b\": {\"c\": null, "d": 123}}}", "a.b.c")
     *              ==> return null, actually there is a JsonNull under json path a.b.c
     *      Considering this, param "suppressException" added in, default value is true.
     *      If set it to false, no invalid path will throw a RuntimeException
     *
     *      getX("{a:{b:{c:null}}}", "a.b.c", null, Type.String, null, false);
     *      getX("{a:{b:{c:null}}}", "a.b.c", null, Type.String, "abc", false);
     *      getX("{a:{b:{c:null}}}", "a.b.c", null, Type.Null, null, false)
     *      getX("{a:{b:{c:null}}}", "a.b.c", null, Type.Null, "def", false)
     */
    private static enum Type {
        STRING,
        LONG,
        DOUBLE,
        BOOLEAN,
        OBJECT,
        ARRAY,
        NULL;
    }

    /**
     * parse array index json path to real array index
     *   __X__ ==> x
     *
     * @return: -1 means this is not a arrayIndex
     */
    private static long parseArrayIndex(String jsonPath) {
        Matcher m = PATTERN.matcher(jsonPath);
        while (m.find()) {
            return Long.valueOf(m.group(1));
        }
        return -1;
    }

    private static Object getX(Object obj, String path, String tmpPath,
                               Type type, Object defaultVal, boolean suppressException) {

        // 1. to jsonElement first
        JsonElement jsonElement = parseJsonFromObj(obj);

        // 2. jsonElement is null, no suppress exception, path must be invalid, runtime exeption out
        if (jsonElement == null && !suppressException) {
            if (StringUtils.isEmpty(tmpPath)) {
                throw new RuntimeException("input obj is invalid !");
            } else if (StringUtils.isNotEmpty(path)) {
                throw new RuntimeException("no such path " + tmpPath
                        + ", required whole path is " + tmpPath + "." + path);
            } else {
                throw new RuntimeException("no such path " + tmpPath);
            }
        }

        // 3. start to return / recurse case by case
        try {
            switch (type) {
                case STRING:
                    String defaultString = defaultVal instanceof String ? (String) defaultVal : null;
                    if (jsonElement == null) {
                        return defaultString;
                    } else if (StringUtils.isEmpty(path)) {
                        return toPrimitiveString(jsonElement, defaultString, suppressException);
                    } else {
                        // path not end, recursion go on ...
                    }
                    break;
                case LONG:
                    Long defaultLong = defaultVal instanceof Long ? (Long) defaultVal : null;
                    if (jsonElement == null) {
                        return defaultLong;
                    } else if (StringUtils.isEmpty(path)) {
                        return toPrimitiveLong(jsonElement, defaultLong, suppressException);
                    }
                    break;
                case DOUBLE:
                    Double defaultDouble = defaultVal instanceof Double ? (Double) defaultVal : null;
                    if (jsonElement == null) {
                        return defaultDouble;
                    } else if (StringUtils.isEmpty(path)) {
                        return toPrimitiveDouble(jsonElement, defaultDouble, suppressException);
                    } else {
                        // path not end, recursion go on ...
                    }
                    break;
                case BOOLEAN:
                    break;
                case OBJECT:
                    break;
                case ARRAY:
                    break;
                case NULL:
                    break;
                default:
                    break;
            }
        }
        catch (WrongJsonTypeException e) {
            if (StringUtils.isEmpty(tmpPath)) {
                throw new RuntimeException("The type of input obj mismatch. " + e.getMessage());
            } else {
                throw new RuntimeException("The type on the path " + tmpPath + " mismatch. " + e.getMessage());
            }
        }

        return defaultVal;
    }

    public static String getString(Object obj, String path) {
        return getString(obj, path, null);
    }

    public static String getString(Object obj, String path, String defaultVal) {
        return defaultVal;
    }

    public static Long getLong(Object obj, String path) {
        return getLong(obj, path, null);
    }

    public static Long getLong(Object obj, String path, Long defaultVal) {
        return defaultVal;
    }

    public static Double getDouble(Object obj, String path) {
        return getDouble(obj, path, null);
    }

    public static Double getDouble(Object obj, String path, Double defaultVal) {
        return defaultVal;
    }

    public static Boolean getBoolean(Object obj, String path) {
        return getBoolean(obj, path, null);
    }

    public static Boolean getBoolean(Object obj, String path, Boolean defaultVal) {
        return defaultVal;
    }

    public static JsonObject getObj(Object obj, String path) {
        return null;
    }

    public static JsonArray getArr(Object obj, String path) {
        return null;
    }

    public static <T> T getObj(Object obj, String path, Class<T> classOfT) {
        return null;
    }

    public static <T> List<T> getArr(Object obj, String path, Class<T> classOfT) {
        return null;
    }

    private static void removeNull(JsonObject jsonObj) {
        Iterator<Entry<String, JsonElement>> it = jsonObj.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, JsonElement> e = it.next();
            if (e.getValue().isJsonNull()) {
                jsonObj.remove(e.getKey());
                it.remove();
            }
            else if (e.getValue().isJsonObject()) {
                removeNull(e.getValue().getAsJsonObject());
            }
            else if (e.getValue().isJsonArray()) {
                removeNull(e.getValue().getAsJsonArray());
            }
        }
    }

    private static void removeNull(JsonArray jsonArray) {
        Iterator<JsonElement> it = jsonArray.iterator();
        while (it.hasNext()) {
            JsonElement e = it.next();
            if (e.isJsonNull()) {
                jsonArray.remove(e);
                it.remove();
            }
            else if (e.isJsonObject()) {
                removeNull(e.getAsJsonObject());
            }
            else if (e.isJsonArray()) {
                removeNull(e.getAsJsonArray());
            }
        }
    }

    public static JsonElement getElementByJsonPath(JsonElement root, String path) {
        if (StringUtils.isBlank(path)) {
            return root;
        }

        String[] ps = path.split("\\.");
        JsonElement next = null;
        try {
            if (root.isJsonObject()) {
                next = root.getAsJsonObject().get(ps[0]);
            } else if (root.isJsonArray()) {
                next = root.getAsJsonArray().get(Integer.parseInt(ps[0]));
            } else {
                return root;
            }
        } catch (Exception e) {
            return root;
        }

        if (next != null) {
            return getElementByJsonPath(next,
                    StringUtils.join(ArrayUtils.subarray(ps, 1, ps.length), "."));
        }

        return root;
    }

    /**
     * TODO: not done yet ...
     */
    public static List<String> getAllJsonPath(String json) {
        List<String> jsonPaths = new ArrayList<String>();
        JsonElement jsonElement = parseJson(json);
        if (jsonElement == null) {
            return null;
        }

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String prop = entry.getKey();
                JsonElement e = entry.getValue();
            }
        }
        else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Iterator<JsonElement> it = jsonArray.iterator();
            while (it.hasNext()) {
                JsonElement e = it.next();
            }
        }
        /*
         * else if (jsonElement.isJsonNull()) {
         *     // TODO
         * }
         * else if (jsonElement.isJsonPrimitive()) {
         *     // TODO
         * }
         */

        return null;
    }


}
