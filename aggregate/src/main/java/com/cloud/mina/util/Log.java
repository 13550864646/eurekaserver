package com.cloud.mina.util;

public class Log {
    static org.apache.log4j.Logger logger = null;

    static {
        logger = org.apache.log4j.Logger.getLogger(Log.class);
    }

    /**
     * @param log
     */
    public static void debug(String log) {
        logger.debug(log);
    }

    /**
     * @param log
     */
    public static void info(String log) {
        logger.info(log);
    }

    /**
     * @param log
     */
    public static void error(String log) {
        logger.error(log);
    }
}
