package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixiaoyang
 */


public class FoodInfo implements Serializable {

    public String saleUnitName;  //销售计量单位名称
    /**
     * 采购计量单位名称
     */
    public String purUnitName;
    /**
     * 锁定库存
     */
    public float lockQty;
    /**
     * 默认销售价格
     */
    public float salePrice;
    /**
     * 默认采购计量单位ID
     */
    public String purUnitId;
    /**
     * 默认销售计量单位ID
     */
    public String saleUnitId;
    /**
     * 剩余库存
     */
    public float balQty;
    /**
     * 组织ID
     */
    public String orgId;
    /**
     * 菜品ID
     */
    public String itemId;
    /**
     * 菜品名称
     */
    public String itemName;
    /**
     * 可用库存
     */
    public float invQty;
    /**
     * 商户ID
     */
    public String merchantId;
    /**
     * 我的菜品ID
     */
    public String id;

    private  String imgId;

    public float qty;

    private String alias;




    /**
     * 排序号
     */
    public String dispOrder;
    /**
     * 状态 Y 在售
     */
    public String state;

    /**
     * 库存单位ID
     */
    public String mainUnitId;

    private String unitId;  //默认基本计量单位

    private String vendorId;
    private String vendor;
    private String area;
    private boolean isV;   // 判断是否被选中 产地  用于已通过 进货，修改时判定已经入库得菜，true不能修改

    public boolean isV() {
        return isV;
    }

    public void setV(boolean v) {
        isV = v;
    }

    private int sortCount ;

    private float sortPrice;

    public float getSortPrice() {
        return sortPrice;
    }

    public void setSortPrice(float sortPrice) {
        this.sortPrice = sortPrice;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public void setSortCount(int sortCount) {
        this.sortCount = sortCount;
    }

    public int getSortCount() {
        return sortCount;
    }

    public String getAlias() {
        return (alias!=null?alias.toString().trim():alias);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "FoodInfo{" +
                "saleUnitName='" + saleUnitName + '\'' +
                ", purUnitName='" + purUnitName + '\'' +
                ", lockQty=" + lockQty +
                ", salePrice=" + salePrice +
                ", purUnitId='" + purUnitId + '\'' +
                ", saleUnitId='" + saleUnitId + '\'' +
                ", balQty=" + balQty +
                ", orgId='" + orgId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", invQty=" + invQty +
                ", merchantId='" + merchantId + '\'' +
                ", id='" + id + '\'' +
                ", dispOrder='" + dispOrder + '\'' +
                ", state='" + state + '\'' +
                ", mainUnitId=" + mainUnitId +
                '}';
    }

    public String getSaleUnitName() {
        return saleUnitName;
    }

    public void setSaleUnitName(String saleUnitName) {
        this.saleUnitName = saleUnitName;
    }

    public String getPurUnitName() {
        return purUnitName;
    }

    public void setPurUnitName(String purUnitName) {
        this.purUnitName = purUnitName;
    }

    public float getLockQty() {
        return lockQty;
    }

    public void setLockQty(float lockQty) {
        this.lockQty = lockQty;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getPurUnitId() {
        return purUnitId;
    }

    public void setPurUnitId(String purUnitId) {
        this.purUnitId = purUnitId;
    }

    public String getSaleUnitId() {
        return saleUnitId;
    }

    public void setSaleUnitId(String saleUnitId) {
        this.saleUnitId = saleUnitId;
    }

    public float getBalQty() {
        return balQty;
    }

    public void setBalQty(float balQty) {
        this.balQty = balQty;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {

        return (itemName!=null?itemName.toString().trim():itemName);
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getInvQty() {
        return invQty;
    }

    public void setInvQty(float invQty) {
        this.invQty = invQty;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(String dispOrder) {
        this.dispOrder = dispOrder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }
}
