package com.huwenmin.hellomvp.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：Administrator on 2017/3/29 15:14
 * <p>
 * 功能：
 */

public class ColumnDataItem {

    /**
     * column_id : 9705405
     * column_name : 首页轮播图
     * column_type : lbt
     * column_typestr : []
     * datas : [{"title":"人民的名义","pic":"http://s1.wasu.tv/data/images/201703/29/58db23dc84892.jpg","fee":1,"updatetime":"1490756573","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/111011","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/111011","type":"tv","source":"agg","series":"更新至1集","abstract":"群星上演最大尺度反腐剧"},{"title":"和妈妈一起谈恋爱","pic":"http://s3.wasu.tv/data/images/201703/27/58d911208b505.jpg","fee":1,"updatetime":"1490620706","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/110985","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/110985","type":"tv","source":"agg","series":"更新至4集","abstract":"闫学晶带着女儿追幸福"},{"title":"非凡任务","pic":"http://s4.wasu.tv/data/images/201703/29/58db1cc5c47fa.jpg","fee":1,"updatetime":"1490754778","livebill":null,"url":"http://www.wasu.cn/wap/subject/onpreview/target/2529","type":"other","source":"专题","abstract":"全国观影抢票"},{"title":"画江湖之杯莫停","pic":"http://s4.wasu.tv/data/images/201703/29/58db238ddea7e.jpg","fee":1,"updatetime":"1490756496","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/105762","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/105762","type":"cartoon","source":"agg","series":"更新至17集","abstract":"江湖再现绝世神兵"},{"title":"因为遇见你","pic":"http://s2.wasu.tv/data/images/201703/22/58d2903fb9ebe.jpg","fee":1,"updatetime":"1490194497","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/110313","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/110313","type":"tv","source":"agg","series":"更新至45集","abstract":"孙怡邓伦上演欢喜冤家互损记"},{"title":"绑架者","pic":"http://s1.wasu.tv/data/images/201703/28/58da2afc60a75.jpg","fee":1,"updatetime":"1490692886","livebill":null,"url":"http://www.wasu.cn/wap/subject/onpreview/target/2532","type":"other","source":"专题","abstract":"全国观影抢票中"},{"title":"热血尖兵","pic":"http://s1.wasu.tv/data/images/201703/08/58bfcba511536.jpg","fee":1,"updatetime":"1488964518","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/110465","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/110465","type":"tv","source":"agg","series":"更新至23集","abstract":"王紫逸塑造青春新偶像"},{"title":"不一样的美男子2","pic":"http://s.wasu.cn/data/images/201703/14/58c762f8bb4b0.jpg","fee":1,"updatetime":"1489462010","livebill":null,"url":"http://www.wasu.cn/Agginfo/index/id/110581","info":"http://clientapi.wasu.cn/AggPhone/vodinfo/id/110581","type":"tv","source":"agg","series":"更新至18集","abstract":"张云龙阚清子开启\u201c危爱\u201d之旅"}]
     */

    private int column_id;
    private String column_name;
    private String column_type;
    private List<?> column_typestr;
    private List<DatasBean> datas;

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public List<?> getColumn_typestr() {
        return column_typestr;
    }

    public void setColumn_typestr(List<?> column_typestr) {
        this.column_typestr = column_typestr;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * title : 人民的名义
         * pic : http://s1.wasu.tv/data/images/201703/29/58db23dc84892.jpg
         * fee : 1
         * updatetime : 1490756573
         * livebill : null
         * url : http://www.wasu.cn/Agginfo/index/id/111011
         * info : http://clientapi.wasu.cn/AggPhone/vodinfo/id/111011
         * type : tv
         * source : agg
         * series : 更新至1集
         * abstract : 群星上演最大尺度反腐剧
         */

        private String title;
        private String pic;
        private int fee;
        private String updatetime;
        private Object livebill;
        private String url;
        private String info;
        private String type;
        private String source;
        private String series;
        @SerializedName("abstract")
        private String abstractX;

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

        public Object getLivebill() {
            return livebill;
        }

        public void setLivebill(Object livebill) {
            this.livebill = livebill;
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSeries() {
            return series;
        }

        public void setSeries(String series) {
            this.series = series;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }
    }
}
