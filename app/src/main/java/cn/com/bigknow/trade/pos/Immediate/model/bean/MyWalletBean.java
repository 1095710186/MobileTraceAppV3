package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：褚世文
 * 创建时间：2016/9/6 10:26
 */
public class MyWalletBean implements Serializable {
    private String id;//": "账户ID",
    private String orgId;//": "组织ID",
    private String merchantId;//": "商户ID",
    private String accountNo;//": "账户编号",
    private String creator;//": "创建人",
    private String creatorId;//": "创建人ID",
    private String createDate;//": "创建时间",
    private double amt;//": 余额,
    private double availAmt;//": 可用余额,
    private double frozenAmt;//": 冻结金额,
    private String updateDate;//": "更新时间",
    private String description;//": 备注,
    private List<BankCardInfoBean> bankCardList = new ArrayList<>();//绑定银行卡
    private List<FlowRecordBean> detList = new ArrayList<>();//账户流水


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public double getAvailAmt() {
        return availAmt;
    }

    public String getAvailAmtStr() {
        return String.format("%.2f", availAmt);
    }

    public void setAvailAmt(double availAmt) {
        this.availAmt = availAmt;
    }

    public double getFrozenAmt() {
        return frozenAmt;
    }

    public void setFrozenAmt(double frozenAmt) {
        this.frozenAmt = frozenAmt;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BankCardInfoBean> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCardInfoBean> bankCardList) {
        this.bankCardList = bankCardList;
    }

    public List<FlowRecordBean> getDetList() {
        return detList;
    }

    public void setDetList(List<FlowRecordBean> detList) {
        this.detList = detList;
    }

    public BankCardInfoBean getDefBankCard() {
        for (BankCardInfoBean b : bankCardList) {
            if (b.getDefFlag().equals("Y"))
                return b;
        }
        return null;
    }
}
