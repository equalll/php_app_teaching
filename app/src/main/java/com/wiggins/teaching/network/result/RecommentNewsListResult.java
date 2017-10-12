package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

import java.util.List;

/**
 * author ：wiggins on 2017/7/19 14:22
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */
public class RecommentNewsListResult extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 22
         * catid : 3
         * image : http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg
         * title : 老外到中国一脸蒙圈：400元现金2个月花不出去！
         * read_count : 23456
         * catname : 韩娱
         */

        private String id;
        private String catid;
        private String image;
        private String title;
        private String read_count;
        private String catname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRead_count() {
            return read_count;
        }

        public void setRead_count(String read_count) {
            this.read_count = read_count;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }
    }
}
