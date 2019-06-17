package com.cloud.mina.util;

/**
 * 数据协议使用工具类
 */
public class DataTypeChangeHelper {
    /**
     * 将一个单字节的 byte 转换成 32 位的 int
     *
     * @param b
     * @return
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将一个单字节的 byte 转换成十六进制的数
     *
     * @param b
     * @return
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    /**
     * 将一 4byte 的数组转换成 32 位的 int
     * bytes buffer
     * param buf
     * param byte[]中开始转换的位置
     *
     * @return
     */
    public static long unsigned4BytesToInt(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        index = index + 4;
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * 将 16 位的 short 转换成 byte 数组
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 将 32 位整数转换成长度为 4的byte 数组
     *
     * @param s
     * @return
     */
    public static byte[] intToByteArray(int s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * long to byte[]
     */
    public static byte[] longToByteArray(long s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 32 位int转byte []
     *
     * @param res
     * @return
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);//最低位
        targets[1] = (byte) ((res >> 8) & 0xff);//次低位
        targets[2] = (byte) ((res >> 16) & 0xff);//次高位
        targets[3] = (byte) (res >>> 24);//最高位，无符号右移
        return targets;
    }

    /**
     * 将长度为 2 的byte 数组转换为 16 位int
     *
     * @param res
     * @return
     */
    public static int byte2int(byte[] res) {
//        res = InversionByte(res);
//        一个 byte 数据左移 24 位变成 Ox??OOOOOO ，再右移 位变成 OxOO??OOOO
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // ! 表示安位或
        return targets;

    }
}
