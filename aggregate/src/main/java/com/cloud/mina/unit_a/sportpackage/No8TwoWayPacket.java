package com.cloud.mina.unit_a.sportpackage;

import java.util.ArrayList;
import java.util.List;

/**
 * UnitA 智能终端运动数据包（ 2 号包）
 */
public class No8TwoWayPacket extends PackageData{

    private List<String> stepcount2data = new ArrayList<String>();
    private List<String> stepdate = new ArrayList<String>() ;
//    根据1 分钟数据计算出的有效步数包
    private List<String> stepEffective = new ArrayList<String> () ;
    public No8TwoWayPacket(){
        this.name="sports";
        this.type="No8-2";
    }

    public List<String> getStepcount2data() {
        return stepcount2data;
    }

    public void setStepcount2data(List<String> stepcount2data) {
        this.stepcount2data = stepcount2data;
    }

    public List<String> getStepdate() {
        return stepdate;
    }

    public void setStepdate(List<String> stepdate) {
        this.stepdate = stepdate;
    }

    public List<String> getStepEffective() {
        return stepEffective;
    }

    public void setStepEffective(List<String> stepEffective) {
        this.stepEffective = stepEffective;
    }
}
