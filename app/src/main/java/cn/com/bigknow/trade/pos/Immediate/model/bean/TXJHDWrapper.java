package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;

/**
 * Created by hujie on 16/10/26.
 */

public class TXJHDWrapper implements Serializable {

    static final long serialVersionUID = 42L;
    private String id;
    private ChePai chePai;
    private String total;
    private String imageFilePath;
    private String fileId;
    private List<FoodInfo> foodInfos;
    private String updateDate;
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    public String getUpdateDate() {
        return updateDate;
    }

    public String IMG_URL = ModelConfig.MVP_URL + "servlet/fileupload?oper_type=img&masterFileType=%1$s&masterId=%2$s&fileId=%3$s";
    private static final String FILETYPE_entryBill = "entryBill";

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
        if(fileId!=null) {
            imageFilePath = String.format(IMG_URL, id, FILETYPE_entryBill, fileId);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FoodInfo> getFoodInfos() {
        if (foodInfos == null) {
            foodInfos = new ArrayList<>();
        }
        return foodInfos;
    }

    public void setFoodInfos(List<FoodInfo> foodInfos) {
        this.foodInfos = foodInfos;
    }

    public ChePai getChePai() {
        return chePai;
    }

    public void setChePai(ChePai chePai) {
        this.chePai = chePai;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
