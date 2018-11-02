package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.util.List;

/**
 * Created by hujie on 2016/8/24..
 */


public class LiveSell {

    /**
     * 计量单位名称
     */
    public String mainUnitName;
    /**
     * 入场合计
     */
    public int entryQty;
    /**
     * 菜品名称
     */
    public String itemName;
    /**
     * 销售额合计
     */
    public int saleAmt;
    /**
     * 锁定合计
     */
    public int lockQty;
    /**
     * 菜品编码
     */
    public String itemCode;
    public String id;
    /**
     * 销售合计
     */
    public int saleQty;
    /**
     * 结余合计
     */
    public int balQty;
    /**
     * 计量单位ID
     */
    public String mainUnitId;

    public List<LiveSellBatch> invBat;

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
    }

    public int getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(int entryQty) {
        this.entryQty = entryQty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(int saleAmt) {
        this.saleAmt = saleAmt;
    }

    public int getLockQty() {
        return lockQty;
    }

    public void setLockQty(int lockQty) {
        this.lockQty = lockQty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(int saleQty) {
        this.saleQty = saleQty;
    }

    public int getBalQty() {
        return balQty;
    }

    public void setBalQty(int balQty) {
        this.balQty = balQty;
    }

    public String getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public List<LiveSellBatch> getInvBat() {
        return invBat;
    }

    public void setInvBat(List<LiveSellBatch> invBat) {
        this.invBat = invBat;
    }
}
