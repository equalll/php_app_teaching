package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

/**
 * author ：wiggins on 2017/7/27 16:59
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */

public class GetPraiseResult extends BaseResult {

    /**
     * data : {"isUpVote":0}
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
         * isUpVote : 0
         */

        private int isUpVote;

        public int getIsUpVote() {
            return isUpVote;
        }

        public void setIsUpVote(int isUpVote) {
            this.isUpVote = isUpVote;
        }
    }
}
