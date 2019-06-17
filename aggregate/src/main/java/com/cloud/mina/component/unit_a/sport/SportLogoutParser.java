package com.cloud.mina.component.unit_a.sport;

import com.cloud.mina.component.filter.UnitASportComponent;
import com.cloud.mina.unit_a.sportpackage.LogoutPacket;
import com.cloud.mina.unit_a.sportpackage.PackageData;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.stereotype.Component;

/**
 * unitA 公司智能终端运动退出解码器
 */
@Component
public class SportLogoutParser extends UnitASportComponent {
    @Override
    public boolean check(IoBuffer buffer) {
        if (buffer.get(8) == 1 && buffer.get(9) == 3) {
            return true;
        }
        return false;
    }

    @Override
    public PackageData generateRealPackageData(IoBuffer buffer) {
        log.info(this.getClass().getSimpleName() + ". generateRealPackage Data()begin . ..");
        LogoutPacket packet = new LogoutPacket();
        packet.setName("sports");
        packet.setType("logout");
        log.info(this.getClass().getSimpleName() + ". generateRealPackage Data()end.");
        return packet;
    }
}
