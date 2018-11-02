package cn.com.bigknow.trade.pos.Immediate.model.bean;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 订单2016/8/26 11:52
 */
@Entity
public class Billbean implements Serializable {

    static final long serialVersionUID = 42L;
    @Id
    private String id;//订单ID
    private String billNo;//订单编号
    private String billDate; //订单日期,
    private String createDate;

    private float sumAmt; //订单合计
    private float payAmt; //实际应付

    private String state;  //状态,

    private int selectIndex;
    private int payState = 1;//支付状态 //1未支付 2支付失败
    private int kinds;//种类
    private float sumQty;//菜品总重
    private float balAmt;////已付金额
    private float privilegeAmt;////优惠金额


    private String foodJsons;

    @Transient
    private List<ShopCarFood> shopCarFoods;

    @Generated(hash = 1578798465)
    public Billbean(String id, String billNo, String billDate, String createDate,
            float sumAmt, float payAmt, String state, int selectIndex, int payState,
            int kinds, float sumQty, float balAmt, float privilegeAmt, String foodJsons) {
        this.id = id;
        this.billNo = billNo;
        this.billDate = billDate;
        this.createDate = createDate;
        this.sumAmt = sumAmt;
        this.payAmt = payAmt;
        this.state = state;
        this.selectIndex = selectIndex;
        this.payState = payState;
        this.kinds = kinds;
        this.sumQty = sumQty;
        this.balAmt = balAmt;
        this.privilegeAmt = privilegeAmt;
        this.foodJsons = foodJsons;
    }

    @Generated(hash = 1249289181)
    public Billbean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return this.billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public float getSumAmt() {
        return this.sumAmt;
    }

    public void setSumAmt(float sumAmt) {
        this.sumAmt = sumAmt;
    }

    public float getPayAmt() {
        return this.payAmt;
    }

    public void setPayAmt(float payAmt) {
        this.payAmt = payAmt;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSelectIndex() {
        return this.selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public int getPayState() {
        return this.payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getKinds() {
        return this.kinds;
    }

    public void setKinds(int kinds) {
        this.kinds = kinds;
    }

    public float getSumQty() {
        return this.sumQty;
    }

    public void setSumQty(float sumQty) {
        this.sumQty = sumQty;
    }

    public float getBalAmt() {
        return this.balAmt;
    }

    public void setBalAmt(float balAmt) {
        this.balAmt = balAmt;
    }

    public float getPrivilegeAmt() {
        return this.privilegeAmt;
    }

    public void setPrivilegeAmt(float privilegeAmt) {
        this.privilegeAmt = privilegeAmt;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<ShopCarFood> getShopCarFoods() {
        if (foodJsons != null) {
            return JSON.parseArray(foodJsons, ShopCarFood.class);
        }
        return null;
    }

    
    public void setShopCarFoods(List<ShopCarFood> shopCarFoods) {
        foodJsons = JSON.toJSONString(shopCarFoods);
    }

    public String getFoodJsons() {
        return this.foodJsons;
    }

    public void setFoodJsons(String foodJsons) {
        this.foodJsons = foodJsons;
    }
}
