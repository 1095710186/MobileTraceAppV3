package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on  2016/8/22 18:43
 */
public class OutBean {
    private String operDate;//操作时间
    private String updateDate; //",更新时间
    private String creator; //操作人
    private String operReason; //[B销货撤销，T销货]操作原因
    private String batchNo;//批次号
    private String operType;//[IN库存增加，OUT库存减少]操作类型，operReason：[B销货撤销，T销货]操作原因,b
    private String batchId; //批次ID
    private int balQty;//剩余数量
    private String mainUnitName;//",库存单位
    private int entryQty;//总数
    private String itemName; //菜品名称
    private String itemCode;
    private String enterDate;//入场时间
    private int qty;//操作数量
    private String id;//",操作ID
    private String state; //状态
    private int saleQty; //操作数量


    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperReason() {
        return operReason;
    }

    public void setOperReason(String operReason) {
        this.operReason = operReason;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
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

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
    }

    public int getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(int entryQty) {
        this.entryQty = entryQty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }
}
