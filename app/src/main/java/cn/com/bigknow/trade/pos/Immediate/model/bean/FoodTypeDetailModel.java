package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by hujie on 2016/8/18..
 */

@Entity
public class FoodTypeDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Unique
    private String id;
    private String typeName; //类别
    private String simpleName;  //商品简称
    private String price;  //默认销售价格
    private String remark; //描述
    private String name;  //名称
    private String helpCode;  //助记码
    private String purUntiId;  //默认采购计量单位
    private String imgId;
    @Unique
    @NotNull
    private String code;  //编码
    private String typeId;  //所属商品类型id
    private String unitId;  //默认基本计量单位
    private String saleUnitId;


    @Generated(hash = 794508062)
    public FoodTypeDetailModel(String id, String typeName, String simpleName, String price,
            String remark, String name, String helpCode, String purUntiId, String imgId,
            @NotNull String code, String typeId, String unitId, String saleUnitId) {
        this.id = id;
        this.typeName = typeName;
        this.simpleName = simpleName;
        this.price = price;
        this.remark = remark;
        this.name = name;
        this.helpCode = helpCode;
        this.purUntiId = purUntiId;
        this.imgId = imgId;
        this.code = code;
        this.typeId = typeId;
        this.unitId = unitId;
        this.saleUnitId = saleUnitId;
    }

    @Generated(hash = 1925909438)
    public FoodTypeDetailModel() {
    }


    public String getSaleUnitId() {
        return saleUnitId;
    }

    public void setSaleUnitId(String saleUnitId) {
        this.saleUnitId = saleUnitId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getPurUntiId() {
        return purUntiId;
    }

    public void setPurUntiId(String purUntiId) {
        this.purUntiId = purUntiId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
