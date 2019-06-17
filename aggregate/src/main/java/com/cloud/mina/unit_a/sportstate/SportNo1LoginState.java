package com.cloud.mina.unit_a.sportstate;

import com.cloud.mina.unit_a.sportpackage.LoginPacket;
import com.cloud.mina.util.DataTypeChangeHelper;
import com.cloud.mina.util.Logger;
import com.cloud.mina.util.MLinkCRC;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.util.Calendar;

/**
 * unitA 公司智能终端运动数据包（ 号包）登录状态处理类
 */
public class SportNo1LoginState implements SportsPacketHandleState {
    @Override
    public boolean handlePacket(IoSession session, Object message) {
        LoginPacket packet = null;
        if (message != null && message instanceof LoginPacket) {
            packet = (LoginPacket) message;
            if (packet.getPatientID() == null || "".equals(packet.getPatientID().trim())) {
                return false;
            }
            session.setAttribute("patientId", packet.getPatientID());
            session.setAttribute("deviceId", packet.getDeviceID());
            session.setAttribute("company", packet.getCompany());
            session.setAttribute("loginTime", packet.getLoginTime());
            session.setAttribute("appType", packet.getAppType());
//            回复 ACK
            responseToClient(session);
            return true;
        } else {
//            回复 NAK
            responseToClient(session);
            return false;
        }
    }

    private void responseToClient(IoSession session) {
        byte[] ack = responsePacking(session);
        session.write(IoBuffer.wrap(ack));
    }

    private byte[] responsePacking(IoSession session) {
        byte[] ack = new byte[19];
        byte[] crc_c = new byte[4];
        ack[0] = -89;
        ack[1] = -72;
        ack[2] = 0;
        ack[3] = 1;
        ack[4] = 0;
        ack[5] = 0;
        ack[6] = 0;
        ack[7] = 19;
        ack[8] = 1;
        ack[9] = 1;
        Logger.writeLog("patientID" + session.getAttribute("patientId") + "has no param data to send to stepeounter");
        Calendar eal = Calendar.getInstance();
        int year = eal.get(Calendar.YEAR);
        int month = eal.get(Calendar.MONTH) + 1;
        int day = eal.get(Calendar.DAY_OF_MONTH);
        int hour = eal.get(Calendar.HOUR_OF_DAY);
        int minute = eal.get(Calendar.MINUTE);
        int seeond = eal.get(Calendar.SECOND);
        byte[] year_b = DataTypeChangeHelper.int2byte(year);
        byte[] month_b = DataTypeChangeHelper.int2byte(month);
        byte[] day_b = DataTypeChangeHelper.int2byte (day);
        byte[] hour_b = DataTypeChangeHelper.int2byte(hour);
        byte[] minute_b = DataTypeChangeHelper.int2byte(minute);
        byte[] seeond_b = DataTypeChangeHelper.int2byte(seeond);
        ack[10] = year_b[1];
        ack[11] = year_b[0];
        ack[12] = month_b[0];
        ack[13] = day_b[0];
        ack[14] = hour_b[0];
        ack[15] = minute_b[0];
        ack[16] = seeond_b[0];
        crc_c = MLinkCRC.crc16(ack);
        ack[17] = crc_c[0];
        ack[18] = crc_c[1];
        return ack ;
    }
}
