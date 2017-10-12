package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

import java.util.List;

/**
 * author ：wiggins on 2017/7/19 14:22
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */
public class CatResult extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * catid : 0
         * catname : 首页
         */

        private int catid;
        private String catname;

        public int getCatid() {
            return catid;
        }

        public void setCatid(int catid) {
            this.catid = catid;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }
    }
}
