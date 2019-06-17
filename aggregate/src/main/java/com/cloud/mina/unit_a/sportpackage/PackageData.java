package com.cloud.mina.unit_a.sportpackage;

/**
 * UnitA 智能终端运动数据包抽象父类
 */
public abstract class PackageData {

    protected String name = "";
    protected String type = "";
    protected String deviceID = "";
    protected String patientID = "";
    protected String company = "";
    protected String password = "";
    protected String appType = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    @Override
    public String toString() {
        return "PackageData[" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", company='" + company + '\'' +
                ", password='" + password + '\'' +
                ", appType='" + appType + '\'' +
                ']';
    }
}
