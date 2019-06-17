package com.cloud.mina.util;

import com.cloud.mina.unit_a.sportpackage.No8OneWayPacket;
import com.cloud.mina.unit_a.sportpackage.No8ThreeWayPacket;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.Arrays;

/**
 * Tep 协议智能终端运动设备 号包数据入库通用方法类名称 SaveSportsNo8PacketUtil
 */
public class SaveSportsNo8PacketUtil {

    /**
     * 存储智能终端运动数据 历史包
     *
     * @param session
     * @param packet
     * @return
     */
    public static boolean saveNewSportHistory(IoSession session, No8OneWayPacket packet) {
        Logger.writeLog("存储－历史包数据！");
        JSONArray jsArr = new JSONArray();
        JsonUtil.addEntryToJsonArray(jsArr, "stepSum", packet.getStep() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "calSum", packet.getKcal() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "distanceSum", packet.getDistance() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "yxbsSum", packet.getEffective_step() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "weight", packet.getWeight() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "stride", packet.getStride() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeOne", packet.getLevell() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeTwo", packet.getLevel2() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeThree", packet.getLevel3() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeFour", packet.getLevel4() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "uploadType", packet.getTran_type() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "measureTime", DateUtil.formatRestfulDate(packet.getStepdate()));
        String sql = "insert into sports(phone , deviceid, apptype , dataType , realTime , datavalue , pname , receiveTime)values(?,?,?,?,?,?,?,now())";
        boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"), (String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"), PropertiesReader.getProp("DATATYPE_STEPCOUNT"), DateUtil.format(packet.getStepdate()), jsArr.toString(), "No8-1");
        return ret;
    }

    /**
     * 存储详细包
     *
     * @param session
     * @param data
     * @param stepdate
     * @return
     */
    public static boolean saveNewSportDetail(IoSession session, String data, String stepdate) {
        Logger.writeLog(" 以新协议格式存储－详细包数据！ ");
        JSONObject jo = JSONObject.fromObject(data);
        JSONObject dataJson = jo.getJSONObject("data");
        String hour = dataJson.getString("hour");
        JSONArray dataValue = dataJson.getJSONArray("datavalue ");
        JsonUtil.addEntryToJsonArray(dataValue, "hour", hour);
        JsonUtil.addEntryToJsonArray(dataValue, "measureTime", DateUtil.format(stepdate));
        String sql = "insert into sports(phone , deviceid, apptype , dataType ,realTime , datavalue,pname , receiveTime) values (?,?,?,?,?,?,?,now())";
        boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"), (String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"), PropertiesReader.getProp("changyaobin"), DateUtil.format(stepdate), dataValue.toString(), "No8-2");
        return ret;
    }

    /**
     * ＊存储智能终端运动数据－简要包
     *
     * @param session
     * @param packet
     * @return
     */
    public static boolean saveNewSportSimple(IoSession session, No8ThreeWayPacket packet) {
        Logger.writeLog("存储－简要包数据!");
        JSONArray jsArr = new JSONArray();
        JsonUtil.addEntryToJsonArray(jsArr, "stepSum", packet.getStep() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "calSum", packet.getKcal() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "distanceSum", packet.getDistance() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "yxbsSum", packet.getEffective_step() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "weight", packet.getWeight() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "stride", packet.getStride() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeOne", packet.getLevell() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeTwo", packet.getLevel2() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeThree", packet.getLevel3() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "degreeFour", packet.getLevel4() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "uploadType", packet.getTran_type() + "");
        JsonUtil.addEntryToJsonArray(jsArr, "measureTime", DateUtil.formatRestfulDate(packet.getStepdate()));
        String sql = "insert into sports(phone , deviceid, apptype , dataType , realTime , datavalue , pname , receiveTime)values(?,?,?,?,?,?,?,now())";
        boolean ret = C3P0Util.insertOrUpdateData(sql, (String) session.getAttribute("patientId"), (String) session.getAttribute("deviceId"), (String) session.getAttribute("appType"), PropertiesReader.getProp("DATATYPE_STEPCOUNT"), DateUtil.format(packet.getStepdate()), jsArr.toString(), "No8-3");
        return ret;
    }

    /**
     * unitA 智能终端运动数据返回 ack 给客户端
     *
     * @param out
     * @param type
     */
    public static void sendNo8Ack(IoSession out, boolean result, int type) {
        byte[] ack = new byte[13];
        byte[] crc_c = new byte[2];
        ack[0] = -89;
        ack[1] = -72;
        ack[2] = 0;
        ack[3] = 1;
        ack[4] = 0;
        ack[5] = 0;
        ack[6] = 0;
        ack[7] = 13;
        ack[8] = 8;
        ack[9] = (byte) type;
        if (result)
            ack[10] = 14; //成功
        else
            ack[10] = 15; //失效
        crc_c = MLinkCRC.crc16(ack);
        ack[11] = crc_c[0];
        ack[12] = crc_c[1];
        out.write(IoBuffer.wrap(ack));
        Logger.writeLog("return No8-" + type + "ACK end" + Arrays.toString(ack));
    }
}
