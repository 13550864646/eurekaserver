package com.cloud.mina.component.unit_a.sport;

import com.cloud.mina.component.filter.UnitASportComponent;
import com.cloud.mina.unit_a.sportpackage.LoginPacket;
import com.cloud.mina.unit_a.sportpackage.PackageData;
import com.cloud.mina.util.DateUtil;
import com.cloud.mina.util.DeviceIDResolver;
import com.cloud.mina.util.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.stereotype.Component;

/**
 * unitA 公司智能终端运动登录数据包解码器
 */
@Component
public class SportLoginParser extends UnitASportComponent {
    @Override
    public boolean check(IoBuffer buffer) {
        if (buffer.get(8) == 1 && buffer.get(9) == -128) {
            return true;
        }
        return false;
    }

    @Override
    public PackageData generateRealPackageData(IoBuffer buffer) {
        log.info(this.getClass().getSimpleName() + ".generateRealPackageData()begin ...");
        LoginPacket data = new LoginPacket();
        data.setDeviceID(DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), 10));
        data.setPatientID(DeviceIDResolver.searchPatientidByDeviceid(data.getDeviceID()));
        data.setAppType(DeviceIDResolver.searchAppTypeByDeviced(data.getDeviceID()));
        data.setLoginTime(DateUtil.getCurrentTime());
        data.setName("sports");
        data.setType("login");
        if (data.getDeviceID() != null && data.getDeviceID().length() > 4) {
            data.setCompany(DeviceIDResolver.searchCompanyByDeviceid(data.getDeviceID()));
        }
        Logger.writeLog("NO.l package handled device_id:" + data.getDeviceID() + "patient ID :" + data.getPatientID() + "company :"
                + data.getCompany());
        log.info(this.getClass().getSimpleName()+
               ".generateRealPackage Data()end.");
        return data;
    }
}
