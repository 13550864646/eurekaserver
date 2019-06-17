package com.cloud.mina.component.filter;

import com.cloud.mina.unit_a.sportpackage.PackageData;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class ComponentIOFilter extends IoFilterAdapter {
    public Component component;

    public ComponentIOFilter(Component component) {
        super();
        this.component = component;
    }

    public ComponentIOFilter() {
        super();
    }

//    数据接收转换核心方法

    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
//        1. 调用接口 component 实现字节流转为 Java 对象
//        data= component.getDataFromBuffer(ioBuffer)
//        2. 递归调用 messageReceived 处理下一个设备
//        next Filter.message Received(session,data);
        packageHandle(nextFilter, session, message);
    }

    private void packageHandle(NextFilter nextFilter, IoSession session, Object message) {
        PackageData data = null;
        //1. 判断 message 是字节流还是 Java 对象 PackageData
        //如登录包被解析后， message 换为 LoginPacket ，这个时候进入 if (data == null),
        //此时的 nextFiler是unitABPComponent ，但是没有内容结束程序

        if (message instanceof IoBuffer) {
            IoBuffer ioBuffer = (IoBuffer) message;
            ioBuffer.setAutoExpand(true);
            data = component.getDataFromBuffer(ioBuffer);
        }
        String appType = (String) session.getAttribute("appType");
        if (data == null) {
//            2.Filter 就是 unitASportsComponent unitABPComponent 因为
//            unitABPComponent nextfilter=null ，结束程序
//            登录包过来后 IoFilterAdapter messageReceived 方法 执行
//            nextFilter.messageReceived(session data ）之后 递归进入packageHandle
//            在下面方法结束程序
            nextFilter.messageReceived(session, message);
        } else {
            nextFilter.messageReceived(session, data);
        }
    }

}
