package com.cloud.mina.util;


import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件流的工具类
 */
public class PropertiesReader {
    private static Properties prop = new Properties();

    static {
        try {
            InputStream SystemIn = new ClassPathResource("com/Config/SysConf.properties").getInputStream();
            prop.load(SystemIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProp(String name) {
        if (prop != null && prop.containsKey(name)) {
            return prop.getProperty(name, "");
        } else {
            return "";
        }
    }
}
