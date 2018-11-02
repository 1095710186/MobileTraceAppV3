package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * 商户客服信息
 * 2016/9/7 0:28
 * {"merchantCode":"商户编号",
 *      "orgName":"成都金沙批发市场",
 *      "psnId":"用户个人ID",
 *      "userNo":"用户编号,...
 *      "responer":商户所有者,
 *      "userName":客户名称,
 *      "orgId":市场ID,
 *      "merchantName":商户名,
 *      "merchantId":商户ID,
 *      "merchantNo":商铺号}
 */
public class CustomerBean implements Serializable {
    private String merchantCode;//": "商户编号",
    private String orgName;//": 成都金沙批发市场,
    private String psnId;//": 用户个人ID,
    private String userNo;//": "用户编号",
    private String responer;//": "商户所有者-09-02",
    private String userName;//": "客户名称",
    private String orgId;//": 市场ID,
    private String merchantName;//": 商户名,
    private String merchantId;//": 商户ID,
    private String merchantNo;//": 商铺号,
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPsnId() {
        return psnId;
    }

    public void setPsnId(String psnId) {
        this.psnId = psnId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getResponer() {
        return responer;
    }

    public void setResponer(String responer) {
        this.responer = responer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
}
