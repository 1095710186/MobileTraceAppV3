package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixiaoyang
 * 用于新增或更新菜品
 */


public class FoodUpdateAddInfo implements Serializable {

    public String saleUnitName;  //销售计量单位名称
    /**
     * 采购计量单位名称
     */
    public String purUnitName;
    /**
     * 默认销售价格
     */
    public String salePrice;
    /**
     * 默认采购计量单位ID
     */
    public String purUnitId;
    /**
     * 默认销售计量单位ID
     */
    public String saleUnitId;

    /**
     * 菜品ID
     */
    public String itemId;
    /**
     * 菜品名称
     */
    public String itemName;

    public String id;

    /**
     * 库存单位ID
     */

    private String unitId;  //默认基本计量单位

    private String aliasId;// 别名id

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
