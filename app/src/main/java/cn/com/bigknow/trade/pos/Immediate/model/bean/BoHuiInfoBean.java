package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * 申报作记录
 *
 *
 */
public class BoHuiInfoBean {
    /**
     * "total": 6,
     *"rows": [{
     *"operDate": 操作时间,
     *"creator": 操作人,
     *"billType": 单据类型, //入场申报：entry,订单：order
     *"billId": 单据ID,
     *"creatorId": 操作人ID,
     *"action": 操作类型,submitEntry提交，cancelEntry撤销，passEntry通过，rejectEntry驳回

     *"remark": 描述,
     *"id": 流水ID,
     *"extId": 附加信息,
     *"state": 状态

     ${MVPURL}/mvpBillLog/find.action?billId=xx&billType=xx
     参数：
     billId:操作对应业务单据ID：入场申报ID，订单ID
     billType:单据类型
     startDate:操作开始日期
     endDate:操作截止日期
     */
    private String operDate;
    private String creator;
    private String billType;
    private String billId;
    private String creatorId;
    private String action;
    private String remark;
    private String id;
    private String extId;
    private String state;


    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
