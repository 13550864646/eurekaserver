package com.cloud.mina.component.unit_a.strategy;

import com.cloud.mina.unit_a.sportpackage.PackageData;
import com.cloud.mina.unit_a.sportstate.*;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

public class UnitASportsPacketHandleStrategy implements MHDataPacketHandleStrategy {
    static Map<String, Class> classMap = new HashMap<String, Class>();

    //    定义变量区域
    static {
        classMap.put("login", SportNo1LoginState.class);
        classMap.put("logout", SportLogoutState.class);
        classMap.put("No8-1", SportNo8OneWayState.class);
        classMap.put("No8-2", SportNo8TwoWayState.class);
        classMap.put("No8-3", SportNo8ThreeWayState.class);
    }

    SportsPacketHandleState state = null;

    public void setState(SportsPacketHandleState state) {
        this.state = state;
    }

    public void handle(IoSession session, Object message) {
//      根据数据包的头 调用具体的状态类
        if (message != null && message instanceof PackageData) {
            PackageData packageData = (PackageData) message;
            try {
                setState((SportsPacketHandleState) classMap.get(packageData.getType()).newInstance());
                state.handlePacket(session, message);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
