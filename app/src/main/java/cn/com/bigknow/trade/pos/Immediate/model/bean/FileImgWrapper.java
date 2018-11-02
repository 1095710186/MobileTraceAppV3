package cn.com.bigknow.trade.pos.Immediate.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hujie on 2016/8/17..
 */


public class FileImgWrapper implements Parcelable {

    public String masterType;
    public int position;
    public String fileId;
    public String masterId;
    public String filePath;
    public FoodImgInfo foodImgInfo;

    public String url;

    public String getMasterType() {
        return masterType;
    }

    public void setMasterType(String masterType) {
        this.masterType = masterType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FoodImgInfo getFoodImgInfo() {
        return foodImgInfo;
    }

    public void setFoodImgInfo(FoodImgInfo foodImgInfo) {
        this.foodImgInfo = foodImgInfo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //该方法将类的数据写入外部提供的Parcel中。
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(masterType);
        dest.writeInt(position);
        dest.writeString(fileId);
        dest.writeString(masterId);
        dest.writeString(filePath);
        dest.writeSerializable(foodImgInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<FileImgWrapper> CREATOR = new Creator<FileImgWrapper>() {
        //实现从source中创建出类的实例的功能
        @Override
        public FileImgWrapper createFromParcel(Parcel source) {
            FileImgWrapper fileImgWrapper = new FileImgWrapper();
            fileImgWrapper.masterType = source.readString();
            fileImgWrapper.position = source.readInt();
            fileImgWrapper.fileId = source.readString();
            fileImgWrapper.masterId = source.readString();
            fileImgWrapper.filePath = source.readString();
            fileImgWrapper.foodImgInfo = (FoodImgInfo) source.readSerializable();
            return fileImgWrapper;
        }

        //创建一个类型为T，长度为size的数组
        @Override
        public FileImgWrapper[] newArray(int size) {
            return new FileImgWrapper[size];
        }
    };
}