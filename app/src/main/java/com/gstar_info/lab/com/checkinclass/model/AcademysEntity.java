package com.gstar_info.lab.com.checkinclass.model;

import java.util.List;

/**
 * Created by wangyu on 02/04/2017.
 */

public class AcademysEntity {
    /**
     * error : false
     * msg : ok
     * data : [{"key":"J","info":[{"id":"1","name":"计算机科学与技术学院"},{"id":"4","name":"机械与动力工程学院"},{"id":"7","name":"建筑与城市规划学院"},{"id":"12","name":"经济管理学院"}]},{"key":"H","info":[{"id":"2","name":"化学化工学院"}]},{"key":"C","info":[{"id":"3","name":"材料科学与工程学院"},{"id":"10","name":"城市建设与安全环境学院"}]},{"key":"Z","info":[{"id":"5","name":"制药与生命科学学院"},{"id":"6","name":"自动化学院"}]},{"key":"Y","info":[{"id":"8","name":"艺术学院"}]},{"key":"T","info":[{"id":"9","name":"土木工程学院"}]},{"key":"L","info":[{"id":"11","name":"理学院学院"}]},{"key":"G","info":[{"id":"13","name":"管理科学与工程学院"},{"id":"15","name":"公共管理学院"}]},{"key":"F","info":[{"id":"14","name":"法学院"}]}]
     */

    private boolean error;
    private String msg;
    private List<DataBean> data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * key : J
         * info : [{"id":"1","name":"计算机科学与技术学院"},{"id":"4","name":"机械与动力工程学院"},{"id":"7","name":"建筑与城市规划学院"},{"id":"12","name":"经济管理学院"}]
         */

        private String key;
        private List<InfoBean> info;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 1
             * name : 计算机科学与技术学院
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
