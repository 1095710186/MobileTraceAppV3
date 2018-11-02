package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 *
 * 2016/Area10/17
 * {""areaName": "北京",
 "level": true,
 "id": 110000,
 "sort": 1,
 "shortName": "北京",
 "parentId": 0}
 */
public class AreaBean implements Serializable {
    private int id;//	区域编码
    private String areaName;//	区域名称
    private String shortName;//	区域简称
    private int level;//	区域级别，1-省，2-市，3-区县
    private int parentId;	//上级编码
    private int sort;//	排序号


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
