package com.wiggins.teaching.network.result;

import com.google.gson.annotations.SerializedName;
import com.lib.base.net.resultbean.BaseResult;

/**
 * author ：wiggins on 2017/7/19 14:22
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */

public class InitResult extends BaseResult{


    /**
     * data : {"id":"2","app_type":"android","version":"2","version_code":"1.1.9","is_force":"0","apk_url":"http://app.singwa.com/s/eap.sdk","upgrade_point":"å\u008d\u2021çº§äº\u2020å\u201c¦","status":"1","create_time":"1970-01-01 08:00:00","update_time":"1970-01-01 08:00:00","is_update":1}
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
         * id : 2
         * app_type : android
         * version : 2
         * version_code : 1.1.9
         * is_force : 0
         * apk_url : http://app.singwa.com/s/eap.sdk
         * upgrade_point : å‡çº§äº†å“¦
         * status : 1
         * create_time : 1970-01-01 08:00:00
         * update_time : 1970-01-01 08:00:00
         * is_update : 1
         */

        private String id;
        private String app_type;
        private String version;
        private String version_code;
        private String is_force;
        private String apk_url;
        private String upgrade_point;
        @SerializedName("status")
        private String statusX;
        private String create_time;
        private String update_time;
        private int is_update;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getIs_force() {
            return is_force;
        }

        public void setIs_force(String is_force) {
            this.is_force = is_force;
        }

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getUpgrade_point() {
            return upgrade_point;
        }

        public void setUpgrade_point(String upgrade_point) {
            this.upgrade_point = upgrade_point;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
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

        public int getIs_update() {
            return is_update;
        }

        public void setIs_update(int is_update) {
            this.is_update = is_update;
        }
    }
}
