package cn.com.bigknow.trade.pos.Immediate.model.bean;


import java.io.Serializable;

/**
 * Created by hujie on 2016/8/16..
 */
public class FoodImgInfo implements Serializable {

    public String id;
    public String orgId;
    public String userId;
    public String uploadYear;
    public String dispOrder;
    public String uploadMonth;
    public String fileTypeCode;
    public String delFlag;
    public String masterFileId;
    public String uploadSession;
    public String fileName;
    public String rootPath;
    public String fileLoc;
    public String uploadTime;
    public String contentType;
    public String fileSize;
    public String keyDesc;
    public String remark;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUploadYear() {
        return uploadYear;
    }

    public void setUploadYear(String uploadYear) {
        this.uploadYear = uploadYear;
    }

    public String getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(String dispOrder) {
        this.dispOrder = dispOrder;
    }

    public String getUploadMonth() {
        return uploadMonth;
    }

    public void setUploadMonth(String uploadMonth) {
        this.uploadMonth = uploadMonth;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getMasterFileId() {
        return masterFileId;
    }

    public void setMasterFileId(String masterFileId) {
        this.masterFileId = masterFileId;
    }

    public String getUploadSession() {
        return uploadSession;
    }

    public void setUploadSession(String uploadSession) {
        this.uploadSession = uploadSession;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getFileLoc() {
        return fileLoc;
    }

    public void setFileLoc(String fileLoc) {
        this.fileLoc = fileLoc;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
