package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

/**
 * author ：wiggins on 2017/7/25 10:32
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */

public class LoginResult extends BaseResult{

    /**
     * data : {"token":"woT6llDzfBD4BxLpMna2IdjCHiqijgLpwYaCKzKaan3NhPnDuiPcr0N0g2dp9M3u"}
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
         * token : woT6llDzfBD4BxLpMna2IdjCHiqijgLpwYaCKzKaan3NhPnDuiPcr0N0g2dp9M3u
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
