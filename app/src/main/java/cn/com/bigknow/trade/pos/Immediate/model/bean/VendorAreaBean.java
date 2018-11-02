package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 *
 *供货商产地
 * {
 "id": "3KASKSTIK7VZ87Y6XT17O4QC6RU2T5VY",
 "orgId": "8a8189ca57aca67c0157aca9b4690002",
 "merchantId": "8a8189ca57aca67c0157acaaf0240006",
 "itemId": "IE0EPJGSMEJM7090RNLIRS5DLWTH7MGB",
 "prodId": 140400,
 "prodArea": "山西省长治市",
 "vendor": "DD批发",
 "state": "Y",
 "createTime": "2016-10-12 17:15:39",
 "updateTime": "2016-10-12 17:15:39",
 "remark": null
 */
public class VendorAreaBean implements Serializable {
    private String id;//	供货商ID
    private String itemId;//	我的菜品ID
    private String merchantId;//	商户ID
    private String prodId;//	产地编码
    private String prodArea;	//产地名称
    private String vendor;//	供货商名称


    @Override
    public String toString() {
        return "VendorAreaBean{" +
                "id='" + id + '\'' +
                ", ItemId='" + itemId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", prodId='" + prodId + '\'' +
                ", prodArea='" + prodArea + '\'' +
                ", vendor='" + vendor + '\'' +
                '}';
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdArea() {
        return prodArea;
    }

    public void setProdArea(String prodArea) {
        this.prodArea = prodArea;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
