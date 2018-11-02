package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 2016/8/24..
 */


public class LiveSellBatch {

    /**
     * 批次编号
     */
    public String batchNo;
    public int lockQty;
    /**
     * 单位名称
     */
    public String unitName;
    /**
     * 菜品编码
     */
    public String itemCode;

    public int otherOutQty;
    /**
     * 菜品类型名称
     */
    public String typeName;
    /**
     * 批次ID
     */
    public String batchId;
    /**
     * 结余(不包括锁定)数量
     */
    public int balQty;
    /**
     * 组织ID
     */
    public String orgId;
    /**
     * 产地名称
     */
    public String prodArea;

    public String lastEnterDate;
    /**
     * 菜品ID
     */
    public String itemId;
    /**
     * 当日入场合计
     */
    public int entryQty;

    public String enterDate;

    public int saleAmt;
    /**
     * 菜品名称
     */
    public String itemName;
    /**
     * 结余(包括锁定)
     */
    public int invQty;
    public String merchantId;

    public String vendor;
    /**
     * 默认销售价格
     */
    public float price;

    /**
     * 单位ID
     */
    public String unitId;

    public String lastTransDate;

    public int saleQty;

    public float stdPrice;
    /**
     * 单位ID
     */
    public String mainUnitId;

    /**
     * 购买数量
     */
    public int qty;

    /**
     * 购买数量
     */
    public boolean isCheck;

    /**
     * 时候是首选批次
     */
    public boolean isFirst;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(int saleAmt) {
        this.saleAmt = saleAmt;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }

    public float getStdPrice() {
        return stdPrice;
    }

    public void setStdPrice(float stdPrice) {
        this.stdPrice = stdPrice;
    }

    public String getLastTransDate() {
        return lastTransDate;
    }

    public void setLastTransDate(String lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public int getOtherOutQty() {
        return otherOutQty;
    }

    public void setOtherOutQty(int otherOutQty) {
        this.otherOutQty = otherOutQty;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getLockQty() {
        return lockQty;
    }

    public void setLockQty(int lockQty) {
        this.lockQty = lockQty;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public int getBalQty() {
        return balQty;
    }

    public void setBalQty(int balQty) {
        this.balQty = balQty;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProdArea() {
        return prodArea;
    }

    public void setProdArea(String prodArea) {
        this.prodArea = prodArea;
    }

    public String getLastEnterDate() {
        return lastEnterDate;
    }

    public void setLastEnterDate(String lastEnterDate) {
        this.lastEnterDate = lastEnterDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(int entryQty) {
        this.entryQty = entryQty;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getInvQty() {
        return invQty;
    }

    public void setInvQty(int invQty) {
        this.invQty = invQty;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }
}
