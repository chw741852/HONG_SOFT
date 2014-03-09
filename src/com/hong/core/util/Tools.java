package com.hong.core.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by hong on 14-2-23 下午7:14.
 */
public class Tools {
    public static String escapeCodeStr(String s) {
        try {
            return new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
