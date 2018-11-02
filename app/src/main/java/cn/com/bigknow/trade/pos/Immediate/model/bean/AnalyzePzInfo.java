package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixy
 * 统计--品种
 */
public class AnalyzePzInfo implements Serializable {
    /***
     * {id	统计分析ID
     merchantId	商户ID
     itemId	我的菜品ID
     type	统计数据类型：D-当日，W-本周，M-本月
     itemName	菜品名称
     alias	菜品别名
     entryBat	今天进货批次
     entryQty	今天进货总量
     saleQty	今天销售总量
     trashQty	今天清库量
     saleAmt	今天销售总额
     avgPrice	今天平均售价
     analyzeDate	统计日期
     remark	说明}
     */

    private String id;
    private String merchantId;
    private String itemId;
    private String type;
    private String itemName;
    private String alias;
    private int entryBat;
    private float entryQty;
    private float saleQty;
    private float trashQty;
    private float saleAmt;
    private float avgPrice;
    private String  analyzeDate;

    private String remark;

    private String imgId;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public AnalyzePzInfo() {
    }

    public AnalyzePzInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getEntryBat() {
        return entryBat;
    }

    public void setEntryBat(int entryBat) {
        this.entryBat = entryBat;
    }

    public float getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(float entryQty) {
        this.entryQty = entryQty;
    }

    public float getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(float saleQty) {
        this.saleQty = saleQty;
    }

    public float getTrashQty() {
        return trashQty;
    }

    public void setTrashQty(float trashQty) {
        this.trashQty = trashQty;
    }

    public float getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(float saleAmt) {
        this.saleAmt = saleAmt;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getAnalyzeDate() {
        return analyzeDate;
    }

    public void setAnalyzeDate(String analyzeDate) {
        this.analyzeDate = analyzeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
