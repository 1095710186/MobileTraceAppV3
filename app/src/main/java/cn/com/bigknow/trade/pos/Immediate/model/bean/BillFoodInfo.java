package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixiaoyang
 * itemId	我的菜品ID
 itemName	菜品名称
 alias	菜品别名
 unitName	销售单位
 qty	销售数量
 price	销售单价

 "id": "H4UK3VLS0NG3P9O2UKAQG84ZCOO4TP2F",
 "itemId": "LJL39KYJ7GRRIDVD5GO65CQWEYKID6RL",
 "qty": 4.0000,
 "price": 1.0000,
 "amt": 4.0000,
 "itemName": "长白萝卜    ",
 "unitName": "公斤",
 "alias": "长白萝卜
 */


public class BillFoodInfo implements Serializable {

    public String id;
    public String itemId;
    public float qty;
    public float price;
    public float amt;
    private String itemName;
    private String unitName;
    private String alias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
