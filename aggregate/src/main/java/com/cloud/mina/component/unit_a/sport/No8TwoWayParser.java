package com.cloud.mina.component.unit_a.sport;

import com.cloud.mina.component.filter.UnitASportComponent;
import com.cloud.mina.unit_a.sportpackage.No8TwoWayPacket;
import com.cloud.mina.unit_a.sportpackage.PackageData;
import com.cloud.mina.util.DataTypeChangeHelper;
import com.cloud.mina.util.DateUtil;
import com.cloud.mina.util.DeviceIDResolver;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.stereotype.Component;

/**
 * unitA 公司智能终端运动数据包（ 2号数据包）解码器
 */
@Component
public class No8TwoWayParser extends UnitASportComponent {
    @Override
    public boolean check(IoBuffer buffer) {
        if (buffer.get(8) == 8 && buffer.get(9) == 2) {
            return true;
        }
        return false;
    }

    @Override
    public PackageData generateRealPackageData(IoBuffer buffer) {
        log.info(this.getClass().getSimpleName() + ".generateRealPackageData()begin ...");
        String prefix = DeviceIDResolver.getNo8PackageDevicePrefix(buffer.array());
        No8TwoWayPacket packet = null;
        packet = handle5MinutesDetailPacket(buffer);
        log.info(this.getClass().getSimpleName() + " .generateRealPackageData()end.");
        return packet;
    }

    private No8TwoWayPacket handle5MinutesDetailPacket(IoBuffer buffer) {
        No8TwoWayPacket packet = new No8TwoWayPacket();
        int number = 0;
        byte length[] = new byte[4];
        byte year[] = new byte[2];
        byte stepcount[] = new byte[2];
        byte stepkcal[] = new byte[2];
        byte data[] = new byte[2];
        int year_u[] = new int[24];
        int month_u[] = new int[24];
        int day_u[] = new int[24];
        int Hour[] = new int[24];
        int hourdata[][] = new int[24][72];
        int hourdata_real[][] = new int[24][72];
        length[0] = buffer.get(4);
        length[1] = buffer.get(5);
        length[2] = buffer.get(6);
        length[3] = buffer.get(7);
        long lengthvalue = DataTypeChangeHelper.unsigned4BytesToInt(length, 0);
        String deviceID = DeviceIDResolver.getDeviceIDFromBytes(buffer.array(), (int) (lengthvalue - 18));
        String patientID = DeviceIDResolver.searchPatientidByDeviceid(deviceID);
        String company = DeviceIDResolver.searchCompanyByDeviceid(deviceID);
        packet.setDeviceID(deviceID);
        packet.setPatientID(patientID);
        packet.setCompany(company);
        int unUsedataNum = 12;
        if ((lengthvalue - 12) % 114 == 0) {
            unUsedataNum = 12;
        } else {
            unUsedataNum = 33;
        }
        long max_times_tran = (lengthvalue - unUsedataNum) / 114;
        for (int i = 0; i <= 24; i++) {
            if (number == (lengthvalue - unUsedataNum)) {
                break;
            }
            year[1] = buffer.get(10 + number);
            year[0] = buffer.get(11 + number);
            year_u[i] = DataTypeChangeHelper.byte2int(year);
            month_u[i] = buffer.get(13 + number);
            day_u[i] = buffer.get(14 + number);
            Hour[i] = buffer.get(15 + number);
            for (int j = 0; j < 12; j++) {
                stepcount[0] = buffer.get(16 + j * 2 + number);
                stepcount[1] = buffer.get(17 + j * 2 + number);
                hourdata[i][j] = DataTypeChangeHelper.byte2int(stepcount);
                hourdata_real[i][j] = DataTypeChangeHelper.byte2int(stepcount);
            }
            for (int j = 0; j < 12; j++) {
                stepkcal[0] = buffer.get(40 + j * 2 + number);
                stepkcal[1] = buffer.get(41 + j * 2 + number);
                hourdata[i][j + 12] = DataTypeChangeHelper.byte2int(stepkcal);
            }
            for (int j = 0; j < 12; j++) {
                if (buffer.get(64 + j + number) < 0) {
                    hourdata[i][j + 24] = buffer.get(64 + j + number) + 256;
                    hourdata[i][j + 24] = hourdata[i][j + 24] * 2;
                } else {
                    hourdata[i][j + 24] = buffer.get(64 + j + number);
                    hourdata[i][j + 24] = hourdata[i][j + 24] * 2;
                }
                if (buffer.get(76 + j + number) < 0) {
                    hourdata[i][j + 36] = buffer.get(76 + j + number) + 256;
                    hourdata[i][j + 36] = hourdata[i][j + 36] * 2;
                } else {
                    hourdata[i][j + 36] = buffer.get(76 + j + number);
                    hourdata[i][j + 36] = hourdata[i][j + 36] * 2;
                }
                if (buffer.get(88 + j + number) < 0) {
                    hourdata[i][j + 48] = buffer.get(88 + j + number) + 256;
                    hourdata[i][j + 48] = hourdata[i][j + 48] * 2;
                } else {
                    hourdata[i][j + 48] = buffer.get(88 + j + number);
                    hourdata[i][j + 48] = hourdata[i][j + 48] * 2;
                }
            }
            for (int j = 0; j < 12; j++) {
                data[0] = buffer.get(100 + j * 2 + number);
                data[1] = buffer.get(101 + j * 2 + number);
                hourdata[i][j + 60] = DataTypeChangeHelper.byte2int(data);
            }
            number = number + 114;
        }
        for (int times_tran = 0; times_tran < max_times_tran; times_tran++) {
            StringBuffer stepcount2data = new StringBuffer();
            stepcount2data = stepcount2data.append("{\"data\":{\"datatype\":\"STEPCOUNT2\",");
            stepcount2data = stepcount2data.append("\"hour\"" + ":\"" + String.valueOf(Hour[times_tran]) + "\"," + "\"datavalue\":[{\"snp5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("}, {\"knp5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 12]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 12])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("},{\"level2p5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 24]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 24])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("},{\"level3p5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 36]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 36])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("},{\"level4p5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 48]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 48])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("},{\"yuanp5\":");
            stepcount2data.append("\"");
            for (int i = 0; i < 12; i++) {
                if (i == 11) {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60]));
                } else {
                    stepcount2data.append(String.valueOf(hourdata[times_tran][i + 60])).append(",");
                }
            }
            stepcount2data.append("\"");
            stepcount2data.append("}]}}");
            String stepdate = DateUtil.format(year_u[times_tran] + "-" + month_u[times_tran] + "-" + day_u[times_tran]);
            stepdate = stepdate.replaceAll("-", "");
            packet.getStepcount2data().add(stepcount2data.toString());
            packet.getStepdate().add(stepdate);
        }
        return packet;
    }
}
