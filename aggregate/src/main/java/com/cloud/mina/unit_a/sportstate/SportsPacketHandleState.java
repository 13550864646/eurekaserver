package com.cloud.mina.unit_a.sportstate;

import org.apache.mina.core.session.IoSession;

public interface SportsPacketHandleState {
    boolean handlePacket(IoSession session, Object message);
}
