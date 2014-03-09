package com.hong.core.enumeration;

/**
 * Created by hong on 14-3-8 下午1:59.
 */
public enum DictEnum {
    YES("1"), NO("0");

    private String val;

    private DictEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
