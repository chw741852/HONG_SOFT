package com.hong.core.enumeration;

/**
 * Created by hong on 14-3-8 下午3:27.
 */
public enum UserEnum {
    SUPER("1"), MANAGER("2"), NORMAL("3");

    private String val;

    private UserEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
