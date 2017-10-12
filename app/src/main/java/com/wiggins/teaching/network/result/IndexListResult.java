package com.wiggins.teaching.network.result;

import com.lib.base.net.resultbean.BaseResult;

import java.util.List;

/**
 * author ：wiggins on 2017/7/19 14:22
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */
public class IndexListResult extends BaseResult {


    /**
     * data : {"heads":[{"id":"26","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"345","catname":"韩娱"},{"id":"25","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"345","catname":"韩娱"},{"id":"24","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"123","catname":"韩娱"}],"positions":[{"id":"26","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"345","catname":"韩娱"},{"id":"25","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"345","catname":"韩娱"},{"id":"24","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"123","catname":"韩娱"},{"id":"23","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"12","catname":"韩娱"},{"id":"22","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"23456","catname":"韩娱"},{"id":"21","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"1234","catname":"韩娱"},{"id":"11","catid":"3","image":"http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg","title":"è\u20ac\u0081å¤\u2013åˆ°ä¸­å\u203a½ä¸\u20acè\u201e¸è\u2019™åœˆï¼š400å\u2026ƒçŽ°é\u2021\u20182ä¸ªæœˆèŠ±ä¸\u008då\u2021ºåŽ»ï¼\u0081","read_count":"10","catname":"韩娱"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<HeadsBean> heads;
        private List<PositionsBean> positions;

        public List<HeadsBean> getHeads() {
            return heads;
        }

        public void setHeads(List<HeadsBean> heads) {
            this.heads = heads;
        }

        public List<PositionsBean> getPositions() {
            return positions;
        }

        public void setPositions(List<PositionsBean> positions) {
            this.positions = positions;
        }

        public static class HeadsBean {
            /**
             * id : 26
             * catid : 3
             * image : http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg
             * title : è€å¤–åˆ°ä¸­å›½ä¸€è„¸è’™åœˆï¼š400å…ƒçŽ°é‡‘2ä¸ªæœˆèŠ±ä¸å‡ºåŽ»ï¼
             * read_count : 345
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

        public static class PositionsBean {
            /**
             * id : 26
             * catid : 3
             * image : http://osw05hpy6.bkt.clouddn.com/2017/07/f8c28201707180243309326.jpg
             * title : è€å¤–åˆ°ä¸­å›½ä¸€è„¸è’™åœˆï¼š400å…ƒçŽ°é‡‘2ä¸ªæœˆèŠ±ä¸å‡ºåŽ»ï¼
             * read_count : 345
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
}
