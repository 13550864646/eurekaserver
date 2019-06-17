package com.cloud.mina.unit_a.sportstate;

import com.cloud.mina.unit_a.sportpackage.LogoutPacket;
import com.cloud.mina.util.Logger;
import com.cloud.mina.util.MLinkCRC;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * unitA 公司智能终端运动退出登录处理状态类
 */
public class SportLogoutState implements SportsPacketHandleState {
    @Override
    public boolean handlePacket(IoSession session, Object message) {
        if (message != null && message instanceof LogoutPacket) {
            Logger.writeLog("logout package be handled patientID:" + session.getAttribute("patientId") + " company :" + session.getAttribute("company") + "device id:" + session.getAttribute("deviceid"));
            handleLogoutData(session);
            session.close(true);
            return true;
        }
        return false;
    }

    private void handleLogoutData(IoSession session) {
        byte[] ack = new byte[12];
        byte[] crc_c = new byte[2];
        ack[0] = -89;
        ack[1] = -72;
        ack[2] = 0;
        ack[3] = 1;
        ack[4] = 0;
        ack[5] = 0;
        ack[6] = 0;
        ack[7] = 12;
        ack[8] = 1;
        ack[9] = 3;
        crc_c = MLinkCRC.crc16(ack);
        ack[10] = crc_c[0];
        ack[11] = crc_c[1];
        session.write(IoBuffer.wrap(ack));
        Logger.writeLog("in method handleLogoutData end the ack :" + "-89 - 72 0 1 0 0 0 12 1 3 " + crc_c[0] + " " + crc_c[1]);
    }
}
