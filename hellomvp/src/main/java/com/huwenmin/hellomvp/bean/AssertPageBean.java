package com.huwenmin.hellomvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：Administrator on 2017/3/16 16:47
 * <p>
 * 功能: 列表数据
 */

public class AssertPageBean {

    /**
     * page_no : 1
     * page_total : 8
     * page_count : 174
     * datas : [{"title":"安以轩晒婚纱照宣布结婚 就这样被你收服了","class":"明星八卦","pop":"21092","pic":"http://s.wasu.cn/data/images/201703/15/58c90aa59e4e5.jpg","url":"http://www.wasu.cn/Play/show/id/8581066","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8581066","type":"other","abstract":"","updatetime":"1489570471","series":"1","duration":"84"},{"title":"范冰冰晒\u201c睡美人\u201d照 天然嘟唇五官精致皮肤好","class":"热点","pop":"43316","pic":"http://s.wasu.tv/mams/pic/201703/15/09/201703150913506041db6cffd.jpg","url":"http://www.wasu.cn/Play/show/id/8578661","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8578661","type":"other","abstract":"","updatetime":"1489544844","series":"1","duration":"82"},{"title":"两会现女神翻译 盘点历届两会\u201c翻译女神\u201d","class":"社会百态","pop":"502199","pic":"http://s.wasu.tv/mams/pic/201703/12/16/201703121609524809bb18d9e.jpg","url":"http://www.wasu.cn/Play/show/id/8564974","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8564974","type":"other","abstract":"","updatetime":"1489547337","series":"1","duration":"60"},{"title":"和甜馨都是云字辈？李小璐调皮喊话曹云金叫妈 结果被拉黑了","class":"热点","pop":"200900","pic":"http://s.wasu.tv/mams/pic/201703/15/09/20170315091457338a7259a17.jpg","url":"http://www.wasu.cn/Play/show/id/8578648","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8578648","type":"other","abstract":"","updatetime":"1489546849","series":"1","duration":"198"},{"title":"母亲怒砸玛莎拉蒂 母亲怒砸女儿对象的玛莎拉蒂","class":"热点","pop":"239361","pic":"http://s.wasu.tv/mams/pic/201703/15/09/201703150934183751b4b7a8f.jpg","url":"http://www.wasu.cn/Play/show/id/8579262","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8579262","type":"other","abstract":"","updatetime":"1489564858","series":"1","duration":"140"},{"title":"这位音乐人在台湾成立民生党 坚持两岸统一","class":"热点","pop":"69124","pic":"http://s.wasu.tv/mams/pic/201703/14/10/20170314104530494bf26ec6a.jpg","url":"http://www.wasu.cn/Play/show/id/8573343","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8573343","type":"other","abstract":"","updatetime":"1489536054","series":"1","duration":"106"},{"title":"老人海边身亡留遗书 死因令人唏嘘","class":"热点","pop":"31769","pic":"http://s.wasu.tv/mams/pic/201703/14/10/2017031410402638422561eaa.jpg","url":"http://www.wasu.cn/Play/show/id/8573347","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8573347","type":"other","abstract":"","updatetime":"1489536054","series":"1","duration":"137"},{"title":"安徽村民采特大灵芝 长相像人脸一人几乎抱不下","class":"热点","pop":"1393","pic":"http://s.wasu.tv/mams/pic/201703/14/10/201703141044090646a2ac6f4.jpg","url":"http://www.wasu.cn/Play/show/id/8573344","info":"http://clientapi.wasu.cn/Phone/vodinfo/id/8573344","type":"other","abstract":"","updatetime":"1489564858","series":"1","duration":"63"}]
     */

    private int page_no;
    private int page_total;
    private int page_count;
    private List<DatasBean> datas;

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * title : 安以轩晒婚纱照宣布结婚 就这样被你收服了
         * class : 明星八卦
         * pop : 21092
         * pic : http://s.wasu.cn/data/images/201703/15/58c90aa59e4e5.jpg
         * url : http://www.wasu.cn/Play/show/id/8581066
         * info : http://clientapi.wasu.cn/Phone/vodinfo/id/8581066
         * type : other
         * abstract :
         * updatetime : 1489570471
         * series : 1
         * duration : 84
         */

        private String title;
        @SerializedName("class")
        private String classX;
        private String pop;
        private String pic;
        private String url;
        private String info;
        private String type;
        @SerializedName("abstract")
        private String abstractX;
        private String updatetime;
        private String series;
        private String duration;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
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
        public String toString() {
            return "DatasBean{" +
                    "title='" + title + '\'' +
                    ", classX='" + classX + '\'' +
                    ", pop='" + pop + '\'' +
                    ", pic='" + pic + '\'' +
                    ", url='" + url + '\'' +
                    ", info='" + info + '\'' +
                    ", type='" + type + '\'' +
                    ", abstractX='" + abstractX + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    ", series='" + series + '\'' +
                    ", duration='" + duration + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AssertPageBean{" +
                "page_no=" + page_no +
                ", page_total=" + page_total +
                ", page_count=" + page_count +
                ", datas=" + datas.toString() +
                '}';
    }
}
