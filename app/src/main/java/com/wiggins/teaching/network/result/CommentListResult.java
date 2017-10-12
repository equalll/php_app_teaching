package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

import java.util.List;

/**
 * author ：wiggins on 2017/7/28 15:59
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */

public class CommentListResult extends BaseResult{


    /**
     * data : {"total":2,"page_num":1,"list":[{"id":"6","user_id":"4","to_user_id":"0","content":"v晓不得八点半到吧","username":"王骏","tousername":"","parent_id":"0","create_time":"2017-07-28 14:59:10","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/7b7b9201707271431324898.jpg"},{"id":"5","user_id":"3","to_user_id":"0","content":"nihao","username":"singwa粉-18618158941","tousername":"","parent_id":"0","create_time":"2017-07-28 13:07:03","image":"1"}]}
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
         * total : 2
         * page_num : 1
         * list : [{"id":"6","user_id":"4","to_user_id":"0","content":"v晓不得八点半到吧","username":"王骏","tousername":"","parent_id":"0","create_time":"2017-07-28 14:59:10","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/7b7b9201707271431324898.jpg"},{"id":"5","user_id":"3","to_user_id":"0","content":"nihao","username":"singwa粉-18618158941","tousername":"","parent_id":"0","create_time":"2017-07-28 13:07:03","image":"1"}]
         */

        private int total;
        private int page_num;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage_num() {
            return page_num;
        }

        public void setPage_num(int page_num) {
            this.page_num = page_num;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 6
             * user_id : 4
             * to_user_id : 0
             * content : v晓不得八点半到吧
             * username : 王骏
             * tousername :
             * parent_id : 0
             * create_time : 2017-07-28 14:59:10
             * image : http://osw05hpy6.bkt.clouddn.com/2017/07/7b7b9201707271431324898.jpg
             */

            private String id;
            private String user_id;
            private String to_user_id;
            private String content;
            private String username;
            private String tousername;
            private String parent_id;
            private String create_time;
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getTo_user_id() {
                return to_user_id;
            }

            public void setTo_user_id(String to_user_id) {
                this.to_user_id = to_user_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getTousername() {
                return tousername;
            }

            public void setTousername(String tousername) {
                this.tousername = tousername;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}
