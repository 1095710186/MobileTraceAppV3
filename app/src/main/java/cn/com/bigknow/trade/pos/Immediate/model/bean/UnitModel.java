package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by hujie on 2016/8/19..
 */

@Entity
public class UnitModel {
    @Unique
    @NotNull
    public String id;
    public String itemId;
    public String code;
    public String umDenom;
    public String name;
    public String enName;
    public String remark;
    public String umConv;
    public String afterUnitId;
    public String beforeUnitId;

    public String getBeforeUnitId() {
        return this.beforeUnitId;
    }

    public void setBeforeUnitId(String beforeUnitId) {
        this.beforeUnitId = beforeUnitId;
    }

    public String getAfterUnitId() {
        return this.afterUnitId;
    }

    public void setAfterUnitId(String afterUnitId) {
        this.afterUnitId = afterUnitId;
    }

    public String getUmConv() {
        return this.umConv;
    }

    public void setUmConv(String umConv) {
        this.umConv = umConv;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnName() {
        return this.enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUmDenom() {
        return this.umDenom;
    }

    public void setUmDenom(String umDenom) {
        this.umDenom = umDenom;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 1578731831)
    public UnitModel(@NotNull String id, String itemId, String code,
                     String umDenom, String name, String enName, String remark,
                     String umConv, String afterUnitId, String beforeUnitId) {
        this.id = id;
        this.itemId = itemId;
        this.code = code;
        this.umDenom = umDenom;
        this.name = name;
        this.enName = enName;
        this.remark = remark;
        this.umConv = umConv;
        this.afterUnitId = afterUnitId;
        this.beforeUnitId = beforeUnitId;
    }

    @Generated(hash = 436854100)
    public UnitModel() {
    }
}
