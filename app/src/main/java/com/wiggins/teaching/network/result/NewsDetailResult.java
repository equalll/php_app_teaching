package com.wiggins.teaching.network.result;

import com.google.gson.annotations.SerializedName;
import com.lib.base.net.resultbean.BaseResult;

/**
 * author ：wiggins on 2017/7/19 14:22
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */
public class NewsDetailResult extends BaseResult {


    /**
     * data : {"id":"11","title":"老外到中国一脸蒙圈：400元现金2个月花不出去！","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","content":"","small_title":"老外到中国一脸蒙圈","description":"老外到中国一脸蒙圈：400元现金2个月花不出去！","is_position":"1","create_time":"2017-07-13 23:23:31","update_time":"2017-07-13 23:23:31","status":"1","is_head_figure":"1","read_count":"10","listorder":"0","source_type":"0","is_allowcomments":"1","catname":"韩娱","upvote_count":12,"comment_count":24}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 11
         * title : 老外到中国一脸蒙圈：400元现金2个月花不出去！
         * catid : 3
         * image : http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg
         * content :
         * small_title : 老外到中国一脸蒙圈
         * description : 老外到中国一脸蒙圈：400元现金2个月花不出去！
         * is_position : 1
         * create_time : 2017-07-13 23:23:31
         * update_time : 2017-07-13 23:23:31
         * status : 1
         * is_head_figure : 1
         * read_count : 10
         * listorder : 0
         * source_type : 0
         * is_allowcomments : 1
         * catname : 韩娱
         * upvote_count : 12
         * comment_count : 24
         */

        private String id;
        private String title;
        private String catid;
        private String image;
        private String content;
        private String small_title;
        private String description;
        private String is_position;
        private String create_time;
        private String update_time;
        @SerializedName("status")
        private String statusX;
        private String is_head_figure;
        private String read_count;
        private String listorder;
        private String source_type;
        private String is_allowcomments;
        private String catname;
        private int upvote_count;
        private int comment_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSmall_title() {
            return small_title;
        }

        public void setSmall_title(String small_title) {
            this.small_title = small_title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIs_position() {
            return is_position;
        }

        public void setIs_position(String is_position) {
            this.is_position = is_position;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getIs_head_figure() {
            return is_head_figure;
        }

        public void setIs_head_figure(String is_head_figure) {
            this.is_head_figure = is_head_figure;
        }

        public String getRead_count() {
            return read_count;
        }

        public void setRead_count(String read_count) {
            this.read_count = read_count;
        }

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getSource_type() {
            return source_type;
        }

        public void setSource_type(String source_type) {
            this.source_type = source_type;
        }

        public String getIs_allowcomments() {
            return is_allowcomments;
        }

        public void setIs_allowcomments(String is_allowcomments) {
            this.is_allowcomments = is_allowcomments;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

        public int getUpvote_count() {
            return upvote_count;
        }

        public void setUpvote_count(int upvote_count) {
            this.upvote_count = upvote_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }
    }
}
