package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.text.DecimalFormat;

import cn.com.bigknow.trade.pos.Immediate.app.util.UUIDUtil;

/**
 * Created by hujie on 16/10/28.
 *
 * 购物车菜品
 */

@Entity
public class ShopCarFood {

    @Id
    private String shopCarId = UUIDUtil.generateGUID();

    private int shopCarType;//1 2 3 4 5
    private String id;//订单明细id
    private String itemId;//菜品id
    private String itemName;//菜品名称
    private String mainUnitId;//库存单位id
    private String unitId;//销售单位id
    private float qty;//销售重量
    private float price;//销售价格
    private float amt;//总价
    private String imgId;


    @Generated(hash = 74235426)
    public ShopCarFood() {
    }


    @Generated(hash = 1451266234)
    public ShopCarFood(String shopCarId, int shopCarType, String id, String itemId,
                       String itemName, String mainUnitId, String unitId, float qty,
                       float price, float amt, String imgId) {
        this.shopCarId = shopCarId;
        this.shopCarType = shopCarType;
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.mainUnitId = mainUnitId;
        this.unitId = unitId;
        this.qty = qty;
        this.price = price;
        this.amt = amt;
        this.imgId = imgId;
    }


    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public int getShopCarType() {
        return this.shopCarType;
    }

    public void setShopCarType(int shopCarType) {
        this.shopCarType = shopCarType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMainUnitId() {
        return this.mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public String getUnitId() {

        return this.unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public float getQty() {
        return this.qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmt() {

        this.amt = this.getPrice() * this.getQty();
        DecimalFormat df = new DecimalFormat("#.##");
        if (amt != 0) {
            amt = Float.valueOf(df.format(amt));
        }
        return this.amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getShopCarId() {
        return this.shopCarId;
    }


    public void setShopCarId(String shopCarId) {
        this.shopCarId = shopCarId;
    }


}
