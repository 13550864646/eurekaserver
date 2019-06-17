package com.cloud.mina.component.unit_a.strategy;

import org.apache.mina.core.session.IoSession;

public interface MHDataPacketHandleStrategy {
//    UnitA 数据处理方法
    public void handle(IoSession session, Object message);
}
