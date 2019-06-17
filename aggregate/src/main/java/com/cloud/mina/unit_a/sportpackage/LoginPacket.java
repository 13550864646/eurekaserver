package com.cloud.mina.unit_a.sportpackage;

/**
 * UnitA 智能终端运动登录数据包
 */
public class LoginPacket extends PackageData {

    private String loginTime = "";

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
