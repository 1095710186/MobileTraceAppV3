package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * 2016/8/26 11:57
 */
public class BillDetBean implements Serializable {
    private String id;
    private String orderId;
    private String lineNo;
    private String itemId;
    private String batchNo;
    private String batchId;
    private String unitId;//
    private float qty;
    private float stdPrice;
    private float price;
    private float amt;
    private float discAmt;
    private String mainUnitId; //
    private String mainQty;
    private String description;
    private String baseItemId;
    private String itemName;
    private String unitName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getStdPrice() {
        return stdPrice;
    }

    public void setStdPrice(float stdPrice) {
        this.stdPrice = stdPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(float discAmt) {
        this.discAmt = discAmt;
    }

    public String getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public String getMainQty() {
        return mainQty;
    }

    public void setMainQty(String mainQty) {
        this.mainQty = mainQty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseItemId() {
        return baseItemId;
    }

    public void setBaseItemId(String baseItemId) {
        this.baseItemId = baseItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
