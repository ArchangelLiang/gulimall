package com.angel.common.constant;

public class WareConstant {

    public enum PurchaseStatus {

        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVED(2,"已领取"),
        FINISH(3,"已完成"),
        ERROR(4,"出现异常");


        private int code;
        private String text;

        PurchaseStatus(int code,String text){
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum PurchaseDetailStatus {

        CREATED(0,"新建"),
        ASSIGNED(1,"已领取"),
        BUYING(2,"采购中"),
        FINISH(3,"已完成"),
        FAILED(4,"采购失败");


        private int code;
        private String text;

        PurchaseDetailStatus(int code,String text){
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
