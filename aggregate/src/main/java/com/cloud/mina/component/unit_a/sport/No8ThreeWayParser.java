package com.cloud.mina.component.unit_a.sport;

import com.cloud.mina.component.filter.UnitASportComponent;
import com.cloud.mina.unit_a.sportpackage.No8ThreeWayPacket;
import com.cloud.mina.unit_a.sportpackage.PackageData;
import com.cloud.mina.util.DataTypeChangeHelper;
import com.cloud.mina.util.DateUtil;
import com.cloud.mina.util.DeviceIDResolver;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.stereotype.Component;

/**
 * un tA 公司智能终端运动数据包（ 号包）解码器
 */
@Component
public class No8ThreeWayParser extends UnitASportComponent {
    @Override
    public boolean check(IoBuffer buffer) {
        if (buffer.get(8) == 8 && buffer.get(9) == 3) {
            return true;
        }
        return false;
    }

    @Override
    public PackageData generateRealPackageData(IoBuffer buffer) {
        log.info(this.getClass().getSimpleName() + ".generateRealPackageData()begin ...");
        No8ThreeWayPacket packet = packetPacking(buffer);
        log.info(this.getClass().getSimpleName() + ".generateRealPackageData()end.");
        return packet;
    }

    private No8ThreeWayPacket packetPacking(IoBuffer buffer) {
        No8ThreeWayPacket packet = new No8ThreeWayPacket();
        byte kcal_b[] = new byte[4];
        byte step_b[] = new byte[4];
        byte effective_step_b[] = new byte[4];
        byte distance_b[] = new byte[4];
        byte level1_b[] = new byte[2];
        byte level2_b[] = new byte[2];
        byte level3_b[] = new byte[2];
        byte level4_b[] = new byte[2];
        int tran_type = buffer.get(10);
        int year = buffer.get(11);
        int month = buffer.get(12);
        int day = buffer.get(13);
        effective_step_b[0] = buffer.get(14);
        effective_step_b[1] = buffer.get(15);
        effective_step_b[2] = buffer.get(16);
        effective_step_b[3] = buffer.get(17);
        int battery = buffer.get(22);
        int weight = buffer.get(23);
        int stride = buffer.get(24);
        kcal_b[0] = buffer.get(25);
        kcal_b[1] = buffer.get(26);
        kcal_b[2] = buffer.get(27);
        kcal_b[3] = buffer.get(28);
        step_b[0] = buffer.get(29);
        step_b[1] = buffer.get(30);
        step_b[2] = buffer.get(31);
        step_b[3] = buffer.get(32);
        distance_b[0] = buffer.get(33);
        distance_b[1] = buffer.get(34);
        distance_b[2] = buffer.get(35);
        distance_b[3] = buffer.get(36);
        level1_b[1] = buffer.get(37);
        level1_b[0] = buffer.get(38);
        level2_b[1] = buffer.get(39);
        level2_b[0] = buffer.get(40);
        level3_b[1] = buffer.get(41);
        level3_b[0] = buffer.get(42);
        level4_b[1] = buffer.get(43);
        level4_b[0] = buffer.get(44);
        long kcal = DataTypeChangeHelper.unsigned4BytesToInt(kcal_b, 0);
        long step = DataTypeChangeHelper.unsigned4BytesToInt(step_b, 0);
        long effective_step = DataTypeChangeHelper.unsigned4BytesToInt(effective_step_b, 0);
        long distance = DataTypeChangeHelper.unsigned4BytesToInt(distance_b, 0);
        int levell = DataTypeChangeHelper.byte2int(level1_b) * 2;
        int level2 = DataTypeChangeHelper.byte2int(level2_b) * 2;
        int level3 = DataTypeChangeHelper.byte2int(level3_b) * 2;
        int level4 = DataTypeChangeHelper.byte2int(level4_b) * 2;
        String firmware_version = DeviceIDResolver.getFirmwareVersion(buffer.array(), 18);
        String prefix = DeviceIDResolver.getDeviceIDPrefix(buffer.array(), 65);
        String deviceID = DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), 70);
        String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
        String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
        packet.setDeviceID(deviceID);
        packet.setPatientID(patientID);
        packet.setCompany(company);
        packet.setFirmware_version(firmware_version);
        packet.setPrefix(prefix);
        String stepdate = DateUtil.getStepdate(year, month, day);
        packet.setBattery(battery);
        packet.setStepdate(stepdate);
        packet.setWeight(weight);
        packet.setStride(stride);
        packet.setKcal(kcal);
        packet.setStep(step);
        packet.setDistance(distance);
        packet.setEffective_step(effective_step);
        packet.setLevell(levell);
        packet.setLevel2(level2);
        packet.setLevel3(level3);
        packet.setLevel4(level4);
        packet.setTran_type(tran_type);
        return packet;
    }
}
