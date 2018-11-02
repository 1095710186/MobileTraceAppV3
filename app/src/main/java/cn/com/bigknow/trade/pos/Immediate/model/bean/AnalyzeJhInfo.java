package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixy
 * 统计--进货-量
 */
public class AnalyzeJhInfo implements Serializable {
    /***
     * {entryBat	进货批次
     entryQty	进货总量
     entryDays	进货天数
     avgQty	日均进货量}
     */
    private String entryBat;
    private float entryQty;
    private  String entryDays;
    private float avgQty;


    public AnalyzeJhInfo() {
    }

    public String getEntryBat() {
        return entryBat;
    }

    public void setEntryBat(String entryBat) {
        this.entryBat = entryBat;
    }

    public float getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(float entryQty) {
        this.entryQty = entryQty;
    }

    public String getEntryDays() {
        return entryDays;
    }

    public void setEntryDays(String entryDays) {
        this.entryDays = entryDays;
    }

    public float getAvgQty() {
        return avgQty;
    }

    public void setAvgQty(float avgQty) {
        this.avgQty = avgQty;
    }
}
