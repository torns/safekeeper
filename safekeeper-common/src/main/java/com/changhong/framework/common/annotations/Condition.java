package com.changhong.framework.common.annotations;

/**
 * 条件表达式
 * @author skylark
 */
public enum Condition {
    AND("&"),
    OR("|"),
    EQUALS("=");
    private String value;
    Condition(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }


}
