package com.huwenmin.brvahdemo.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

/**
 * 作者：Administrator on 2017/5/16 14:41
 * <p>
 * 功能：实体类
 */

public class AssertItem extends BaseItem implements Parcelable {

    /**
     * sid : 8631604
     * title : 女子扫二维码免费打印照片 私密照外泄
     * pic : http://s2.wasu.tv/data/images/201703/24/58d4d24480dd8.jpg
     * fee : 1
     * updatetime : 1490343327
     * url : http://www.wasu.cn/Play/show/id/8631604
     * info : http://clientapi.wasu.cn/Phone/vodinfo/id/8631604
     * type : other
     */

    private int sid;
    private String title;
    private String pic;
    private int fee;
    private String updatetime;
    private String url;
    private String info;
    private String type;
    /**
     * class : 明星八卦
     * pop : 9447
     * abstract :
     * series : 1
     * duration : 75
     */

    @SerializedName("class")
    private String classX;
    private String pop;
    @SerializedName("abstract")
    private String abstractX;
    private String series;
    private String duration;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AssertItem() {
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sid);
        dest.writeString(this.title);
        dest.writeString(this.pic);
        dest.writeInt(this.fee);
        dest.writeString(this.updatetime);
        dest.writeString(this.url);
        dest.writeString(this.info);
        dest.writeString(this.type);
        dest.writeString(this.classX);
        dest.writeString(this.pop);
        dest.writeString(this.abstractX);
        dest.writeString(this.series);
        dest.writeString(this.duration);
    }

    protected AssertItem(Parcel in) {
        this.sid = in.readInt();
        this.title = in.readString();
        this.pic = in.readString();
        this.fee = in.readInt();
        this.updatetime = in.readString();
        this.url = in.readString();
        this.info = in.readString();
        this.type = in.readString();
        this.classX = in.readString();
        this.pop = in.readString();
        this.abstractX = in.readString();
        this.series = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<AssertItem> CREATOR = new Creator<AssertItem>() {
        @Override
        public AssertItem createFromParcel(Parcel source) {
            return new AssertItem(source);
        }

        @Override
        public AssertItem[] newArray(int size) {
            return new AssertItem[size];
        }
    };



    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
