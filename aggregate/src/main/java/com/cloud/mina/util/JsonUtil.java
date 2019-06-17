package com.cloud.mina.util;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.List;

/**
 * jso口操作类
 */
public class JsonUtil {
    /**
     * @param json
     * @return
     */
    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    /**
     * 校验字符串是否符合〕 json 格式
     *
     * @param json
     * @return
     */
    public static boolean isGoodJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            JSONObject.fromObject(json);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 功能描述 专入一个javabean 对象生成一个json 格式的字符串
     *
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = objectToJson(props[i].getName());
                    String value = objectToJson(props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    /**
     * 功能描述：传入任意 Ob ect 对象生成一个 json 格式的字符串
     *
     * @param object
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String objectToJson(Object object) {
        StringBuilder json = new StringBuilder();
        if (object == null) {
            json.append("\"\"");//输出双引号
        } else if (object instanceof String || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Date
                || object instanceof java.sql.Date) {
            json.append("\"").append(object.toString()).append("\"");
        } else if (object instanceof List) {
            json.append(listToJson((List) object));
        } else {
            json.append(beanToJson(object));
        }
        return json.toString();
    }

    /**
     * ＊功能描述 通过传入一个列表对象 调用指定方法将列表中的数据生成一个 JSON 规格指定字
     * 符串
     *
     * @param list
     * @return
     */
    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * get jsonobject`s field value
     *
     * @param jo
     * @param field
     * @return
     */
    public static JSONArray getJsonParamterArray(JSONObject jo, String field) {
        JSONArray re_str = new JSONArray();
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getJSONArray(field);
        }
        return re_str;
    }

    /**
     * 从 json 对象中根据 key 获取某一个字段值
     *
     * @param jo
     * @param field
     * @return
     */
    public static JSONObject getJsonParamterObject(JSONObject jo, String field) {
        JSONObject re_str = new JSONObject();
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getJSONObject(field);
        }
        return re_str;
    }

    /**
     * 从 json 对象中根据 key 获取某一个字段值（返回值为字符串）
     *
     * @param jo
     * @param field
     * @return
     */
    public static String getJsonParamterString(JSONObject jo, String field) {
        String re_str = "";
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getString(field);
        }
        return re_str;
    }

    /**
     * 从 json 对象中根据 key 获取某一个字段值（返回值为工nt)
     *
     * @param jo
     * @param field
     * @return
     */
    public static int getJsonParamterinteger(JSONObject jo, String field) {
        int re_str = 0;
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getInt(field);
        }
        return re_str;
    }

    /**
     * 从 json 对象中根据 key 获取某一个字段值 （返回值为 long)
     *
     * @param jo
     * @param field
     * @return
     */
    public static long getJsonParamterLong(JSONObject jo, String field) {
        long re_str = 01;
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getLong(field);
        }
        return re_str;
    }

    /**
     * 从 json 对象中根据 key 获取某一个字段值（返回值为 double)
     *
     * @param jo
     * @param field
     * @return
     */
    public static double getJsonParamterDouble(JSONObject jo, String field) {
        double re_str = 0d;
        if (jo != null && jo.containsKey(field)) {
            re_str = jo.getDouble(field);
        }
        return re_str;
    }

    /**
     * 添加 个json对象 Key/Value 到 JSON 数组末尾
     *
     * @param arr
     * @param key
     * @param value
     * @return
     */
    public static JSONArray addEntryToJsonArray(JSONArray arr, String key, String value) {
        JSONObject jo = new JSONObject();
        jo.put(key, value);
        arr.add(jo);
        return arr;
    }
}
