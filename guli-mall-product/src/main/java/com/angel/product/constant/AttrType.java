package com.angel.product.constant;

/**
 *  属性类型枚举
 *  1为基本属性
 *  0为销售属性
 */
public enum AttrType {

    ATTR_TYPE_BASE(1,"base"),ATTR_TYPE_SALE(0,"sale");

    //属性代码
    private Integer code;
    //属性描述
    private String text;

    AttrType(Integer code,String text){
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
