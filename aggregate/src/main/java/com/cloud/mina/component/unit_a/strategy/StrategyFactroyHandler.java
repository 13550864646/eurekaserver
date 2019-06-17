package com.cloud.mina.component.unit_a.strategy;

import com.cloud.mina.bean.Message;
import com.cloud.mina.unit_a.sportpackage.PackageData;
import com.cloud.mina.util.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * mina 的iohandler 自定义扩展类
 */
public class StrategyFactroyHandler extends IoHandlerAdapter {
    //    定义变量区域
    public MHDataPacketHandleStrategy chain = null;
    PackageData packet = null;
    //    springCloud 消息转发模板
    private RestTemplate restTemplate;
    static Map<String, Class> classMap = new HashMap<String, Class>();

    static {
        /**
         * 不同厂家就是不同的策略 unitA sport/BP 通过数据包的名字来匹配类名
         * sports和bloodpressure
         */
        classMap.put("sports", UnitASportsPacketHandleStrategy.class);
//        classMap.put("bloodpressure", UnitABloodPressurePacketHandleStrategy.class);
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
//        1. 参数验证
        if (message != null && message instanceof PackageData) {
            packet = (PackageData) message;
//            2.调用具体设备处理
            chain = (MHDataPacketHandleStrategy) classMap.get(packet.getName()).newInstance();
//            springBoot版本
            if (chain != null) {
                chain.handle(session, message);
            }
//            发送给转发服务
//            try {
//                Message responseMes = restTemplate.getForObject("http://BOOT-DISPATCH/sendData?appType=" + packet.getAppType() + "&dataType=" + packet.getType(), Message.class);
//                if (responseMes != null && responseMes.getCode() == 1001) {
//                    Logger.writeLog("发送数据成功");
//                } else {
//                    Logger.writeLog("发送数据失败");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.errorLog("连接转发服务异常 转发服务地址为 http //BOOT- DISPATCH");
//            }

//            springMVC版本
//            if (chain != null) {
//                chain.handle(session, message);
//                String dispatchPath = PropertiesReader.getProp("DISPATCH_SERVER_PATH");
//                if (StringUtils.isNotBlank(dispatchPath)) {
////                    参数拼接
//                    List<NameValuePair> urlParameters = new ArrayList<>();
//                    urlParameters.add(new NameValuePair("appType", packet.getAppType()));
//                    urlParameters.add(new NameValuePair("data Type", packet.getType()));
////                    数据发送给转发服务
//                    boolean sendSuccess = HttpClientUtil.sendHttpData(this.getClass().getName(), dispatchPath, urlParameters.toArray(new NameValuePair[urlParameters.size()]));
//                    if (!sendSuccess) {
//                        log.error("数据发送到转发服务失败，转发服务路径为" + dispatchPath + "， 数据为 " + packet.toString());
//                    } else {
//                        log.error("转发服务路径没配置");
//                    }
//                }
//            }
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
