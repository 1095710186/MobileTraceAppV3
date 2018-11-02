package cn.com.bigknow.trade.pos.Immediate.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujie on 2016/8/16 10:01
 * 进货管理列表实体类
 */
public class FoodEntryInfo implements Serializable {

    private String id;
    private String searchNo;//车牌号
    private String billDate;//单据日期
    private List<FoodInfo> detList;//菜品列表
    @JSONField(serialize = false)
    private List<String> foodList;
    private String state;//单据状态Y通过 R为拒绝 S审核中 N
    private String kinds;//种类
    private String itemQty;//菜品总重
    private String totalQty;//车菜总重
    private String createTime;
    private List<Opinion> opinionList;//驳回意见列表
    @JSONField(serialize = false)
    private String opinionString;//驳回原因
    private String fileId;
    private String updateDate;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;

    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getSearchNo() {
        return searchNo;
    }

    public void setSearchNo(String searchNo) {
        this.searchNo = searchNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public List<FoodInfo> getDetList() {
        return detList;
    }

    public void setDetList(List<FoodInfo> detList) {
        this.detList = detList;
    }

    public void setFoodList(List<String> foodList) {
        this.foodList = foodList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Opinion> getOpinionList() {
        return opinionList;
    }

    public void setOpinionList(List<Opinion> opinionList) {
        this.opinionList = opinionList;
    }

    public void setOpinionString(String opinionString) {
        this.opinionString = opinionString;
    }

    public List<String> getFoodList() {
        if (foodList != null) {
            return foodList;
        }
        if (detList != null) {
            foodList = new ArrayList<>();
            for (FoodInfo foodInfo : detList) {
//                foodList.add(foodInfo.getItemName());
                foodList.add(foodInfo.getAlias());
            }
        }


        return foodList;


    }


    public String getOpinionString() {
        if (opinionList != null && opinionList.size() > 0) {
            opinionString = opinionList.get(0).getContent();
        }
        return opinionString;
    }
}
