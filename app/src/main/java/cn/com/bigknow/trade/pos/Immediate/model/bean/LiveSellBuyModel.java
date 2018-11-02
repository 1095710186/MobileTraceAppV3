package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by hujie on 2016/8/26..
 */

@Entity
public class LiveSellBuyModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Unique
    @Id
    public String p_id;

    public String p_itemName;

    public String p_itemCode;

    public int p_saleAmt;

    public int p_entryQty;

    public int p_lockQty;

    public String p_mainUnitName;

    public String p_mainUnitId;

    public int p_saleQty;

    public int p_balQty;

    public float price;

    public int qty;

    public String allLiveBatchBeans;

    public String LiveBatchBeans;

    @Generated(hash = 1333726675)
    public LiveSellBuyModel(String p_id, String p_itemName, String p_itemCode,
                            int p_saleAmt, int p_entryQty, int p_lockQty, String p_mainUnitName,
                            String p_mainUnitId, int p_saleQty, int p_balQty, float price, int qty,
                            String allLiveBatchBeans, String LiveBatchBeans) {
        this.p_id = p_id;
        this.p_itemName = p_itemName;
        this.p_itemCode = p_itemCode;
        this.p_saleAmt = p_saleAmt;
        this.p_entryQty = p_entryQty;
        this.p_lockQty = p_lockQty;
        this.p_mainUnitName = p_mainUnitName;
        this.p_mainUnitId = p_mainUnitId;
        this.p_saleQty = p_saleQty;
        this.p_balQty = p_balQty;
        this.price = price;
        this.qty = qty;
        this.allLiveBatchBeans = allLiveBatchBeans;
        this.LiveBatchBeans = LiveBatchBeans;
    }

    @Generated(hash = 542155557)
    public LiveSellBuyModel() {
    }

    public String getAllLiveBatchBeans() {
        return allLiveBatchBeans;
    }

    public void setAllLiveBatchBeans(String allLiveBatchBeans) {
        this.allLiveBatchBeans = allLiveBatchBeans;
    }

    public String getLiveBatchBeans() {
        return this.LiveBatchBeans;
    }

    public String getP_itemName() {
        return p_itemName;
    }

    public void setP_itemName(String p_itemName) {
        this.p_itemName = p_itemName;
    }

    public String getP_itemCode() {
        return p_itemCode;
    }

    public void setP_itemCode(String p_itemCode) {
        this.p_itemCode = p_itemCode;
    }

    public String getP_mainUnitId() {
        return p_mainUnitId;
    }

    public void setP_mainUnitId(String p_mainUnitId) {
        this.p_mainUnitId = p_mainUnitId;
    }

    public void setLiveBatchBeans(String LiveBatchBeans) {
        this.LiveBatchBeans = LiveBatchBeans;
    }

    


    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getP_balQty() {
        return this.p_balQty;
    }

    public void setP_balQty(int p_balQty) {
        this.p_balQty = p_balQty;
    }

    public int getP_saleQty() {
        return this.p_saleQty;
    }

    public void setP_saleQty(int p_saleQty) {
        this.p_saleQty = p_saleQty;
    }

    public String getP_mainUnitName() {
        return this.p_mainUnitName;
    }

    public void setP_mainUnitName(String p_mainUnitName) {
        this.p_mainUnitName = p_mainUnitName;
    }

    public int getP_lockQty() {
        return this.p_lockQty;
    }

    public void setP_lockQty(int p_lockQty) {
        this.p_lockQty = p_lockQty;
    }

    public int getP_entryQty() {
        return this.p_entryQty;
    }

    public void setP_entryQty(int p_entryQty) {
        this.p_entryQty = p_entryQty;
    }

    public int getP_saleAmt() {
        return this.p_saleAmt;
    }

    public void setP_saleAmt(int p_saleAmt) {
        this.p_saleAmt = p_saleAmt;
    }

    public String getP_id() {
        return this.p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }



}
