package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.util.ArrayList;

/**
 * 我的库存bean
 * Created by hujie on 2016/8/15 14:15
 */
public class MyInventory {
    /**
     * {total:xx,rows:[{
     * itemCode:菜品编码,
     * itemName：菜品名称,
     * entryQty:入场合计,
     * salQty:销售合计,
     * balQty:结余合计,
     * lockQty:锁定合计,
     * salAmt:销售额合计},..,{}]}
     */

    private String itemCode;
    private String itemName;
    private int entryQty;
    private int saleQty;
    private int balQty;
    private int lockQty;
    private int salAmt;
    private String mainUnitName;
    private String mainUnitId;

    private ArrayList<MyInventoryDetial> myInventoryDetials = new ArrayList<>();

    public int getEntryQty() {
        return entryQty;
    }

    public void setEntryQty(int entryQty) {
        this.entryQty = entryQty;
    }

    public int getLockQty() {
        return lockQty;
    }

    public void setLockQty(int lockQty) {
        this.lockQty = lockQty;
    }

    public int getSalAmt() {
        return salAmt;
    }

    public void setSalAmt(int salAmt) {
        this.salAmt = salAmt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
    }

    public ArrayList<MyInventoryDetial> getMyInventoryDetials() {
        return myInventoryDetials;
    }

    private boolean hasRequstData = false;

    public boolean hasRequstDetialData() {
        return hasRequstData;
    }

    public void requstDetialData() {
        hasRequstData = true;
    }

    public String getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }
}
