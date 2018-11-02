package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixiaoyang
 */


public class FoodAlias implements Serializable {

    /**
     * id	别名ID
     itemId	菜品ID
     alias	别名
     def	是否默认
     remark	说明
     */

    /**
     * 菜品ID
     */
    public String itemId;
    /**
     * 别名
     */
    public String alias;
    /**
     * 别名ID
     */
    public String id;

    public String def;

    /**
     * 说明
     */
    public String remark;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
