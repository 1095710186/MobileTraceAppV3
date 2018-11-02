package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 16/8/9.
 */
public class UserInfo implements Serializable {

    /**
     * adminOrgName : 重庆批发市场
     * orgName : 测试商户
     * userNo : 18328502735
     * adminOrgType : E
     * sessionId : 83BBC89D05D2BDBF94F4CC86B6540A0C
     * userName : 杨
     * userId : 297ebe0e560b972901562510a1b900fd
     * logOffFlag : 0
     * orgId : 297ebe0e560b972901560c1338be0009
     * adminOrgId : 297ebe0e560b972901560c124dd10003
     * orgType : M
     * userPersonUuid : 297ebe0e560b972901562510a1b900fc
     * userType : C
     * lang : null
     * pushId;推送Id
     */
    private String birthDate;
    private String merchantNo;
    private String mobile;
    private String idNo;
    private String eduName;
    private String sex;
    private String psnName;
    private String adminOrgName;
    private String orgName;
    private String userNo;
    private String adminOrgType;
    private String sessionId;
    private String userName;
    private String userId;
    private int logOffFlag;
    private String orgId;
    private String adminOrgId;
    private String orgType;
    private String userPersonUuid;
    private String userType;
    private String lang;
    private String pushId;
    private boolean tradePwd;

    private String tokenId;
    private String headImg;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public boolean getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(boolean tradePwd) {
        this.tradePwd = tradePwd;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getAdminOrgName() {
        return adminOrgName;
    }

    public void setAdminOrgName(String adminOrgName) {
        this.adminOrgName = adminOrgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getAdminOrgType() {
        return adminOrgType;
    }

    public void setAdminOrgType(String adminOrgType) {
        this.adminOrgType = adminOrgType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLogOffFlag() {
        return logOffFlag;
    }

    public void setLogOffFlag(int logOffFlag) {
        this.logOffFlag = logOffFlag;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAdminOrgId() {
        return adminOrgId;
    }

    public void setAdminOrgId(String adminOrgId) {
        this.adminOrgId = adminOrgId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getUserPersonUuid() {
        return userPersonUuid;
    }

    public void setUserPersonUuid(String userPersonUuid) {
        this.userPersonUuid = userPersonUuid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEduName() {
        return eduName;
    }

    public void setEduName(String eduName) {
        this.eduName = eduName;
    }

    public String getSex() {
        return sex;
    }

    public String getSexText() {
        if (sex != null) {
            if (sex.equals("M"))
                return "男";
            if (sex.equals("W"))
                return "女";
        }
        return "不详";

    }


    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPsnName() {
        return psnName;
    }

    public void setPsnName(String psnName) {
        this.psnName = psnName;
    }
}
