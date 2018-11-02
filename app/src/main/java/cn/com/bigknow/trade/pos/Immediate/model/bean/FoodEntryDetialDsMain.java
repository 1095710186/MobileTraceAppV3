package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * 2016/8/24 17:53
 */
public class FoodEntryDetialDsMain {
    private String billDate;
    private String operReason;
    private String id;
    private String updateDate;


    public String getId() {
        return id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getOperReason() {
        return operReason;
    }

    public void setOperReason(String operReason) {
        this.operReason = operReason;
    }
}
