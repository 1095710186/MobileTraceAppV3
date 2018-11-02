package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 2016/8/18 11:00
 */
public class MyInventoryDetial {
    /**
     * {total:xx,rows:[{
     * batchNo:入场批次号，
     * entryQty:入场数量，
     * balQty:结余数量...},..,{}]}
     * <p>
     * "spec": null,
     * "balQty": 176,
     * "itemName": "猪肉",
     * "enterDate": "2016-07-25",
     * "remark": null,
     * "vendor": "TT",
     * "prodArea": "TT",
     * "lastTransDate": "2016-08-01 15:59:08",
     * "mainUnitId": "LGZT0H32SH8BACIQ183B0FRDKEDCVDN1",
     * "saleAmt": 369,
     * "itemId": "WFK2RV5H1GPFEDOVTKMCZ249LO1ZPCT",
     * "otherEntryQty": null,
     * "id": "J6HL0TN3Z17K9T9AV4LU9QET9MXSPTAS",
     * "orgId": "297ebe0e560b972901560c124dd10003",
     * "typeName": "肉类",
     * "batchNo": "BAT20160725000008",
     * "saleQty": 24,
     * "unitName": "公斤",
     * "lockQty": 1,
     * "entryQty": 200,
     * "lastEnterDate": "2016-07-25",
     * "otherOutQty": null,
     * "merchantId": "297ebe0e560b972901560c1338be0009",
     * "auxAttr": null,
     * "itemCode": "M01"
     * forSaleQty;可用库存
     */
    private String itemCode;
    private String auxAttr;
    private String merchantId;
    private int otherOutQty;
    private String lastEnterDate;
    private int lockQty;
    private int forSaleQty; //可用库存
    private String unitName;
    private String typeName;
    private String orgId;
    private String otherEntryQty;
    private String itemId;
    private String lastTransDate;
    private int saleAmt;
    private String prodArea;
    private String vendor;
    private String remark;
    private String spec;
    private String mainUnitId;
    private String id;
    private String itemName;
    private int saleQty;
    private String batchNo;
    private int entryQty;
    private int balQty;
    private String enterDate;
    private String unitId;
    private int qty;
    private String operReason;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(int entryQty) {
        this.entryQty = entryQty;
    }

    public int getBalQty() {
        return balQty;
    }

    public void setBalQty(int balQty) {
        this.balQty = balQty;
    }

    public String getOperReason() {
        return operReason;
    }

    public void setOperReason(String operReason) {
        this.operReason = operReason;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getAuxAttr() {
        return auxAttr;
    }

    public void setAuxAttr(String auxAttr) {
        this.auxAttr = auxAttr;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getOtherOutQty() {
        return otherOutQty;
    }

    public void setOtherOutQty(int otherOutQty) {
        this.otherOutQty = otherOutQty;
    }

    public String getLastEnterDate() {
        return lastEnterDate;
    }

    public void setLastEnterDate(String lastEnterDate) {
        this.lastEnterDate = lastEnterDate;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOtherEntryQty() {
        return otherEntryQty;
    }

    public void setOtherEntryQty(String otherEntryQty) {
        this.otherEntryQty = otherEntryQty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLastTransDate() {
        return lastTransDate;
    }

    public void setLastTransDate(String lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public int getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(int saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getProdArea() {
        return prodArea;
    }

    public void setProdArea(String prodArea) {
        this.prodArea = prodArea;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public int getForSaleQty() {
        return forSaleQty;
    }

    public void setForSaleQty(int forSaleQty) {
        this.forSaleQty = forSaleQty;
    }

    public String getShowStr() {
        return batchNo + "    " + forSaleQty + "/" + entryQty + "    " + forSaleQty * 100 / entryQty + "%";
//        return batchNo + "    " + balQty + "/" + entryQty + "    " + balQty * 100 / entryQty + "%";
    }

    public int getProgress() {
        return balQty * 100 / entryQty;
    }
}
