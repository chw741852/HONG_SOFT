package com.hong.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-9-15
 * Time: 下午1:44
 * To change this template use File | Settings | File Templates.
 */
public class ReflectUtils {
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = findField(obj.getClass(), fieldName);
        return getFieldValue(obj, fieldName, field);
    }

    public static Field findField(Class clazz, String fieldName) {
        Class cl = clazz;
        Field field = null;
        while (cl != null && cl != Object.class) {
            try {
                field = cl.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } finally {
                if (field != null)
                    return field;
                cl = cl.getSuperclass();
                continue;
            }
        }

        return null;
    }

    public static Object getFieldValue(Object obj, String fieldName, Field field) {
        if (field != null) {
            Method method = findGetMethod(obj.getClass(), fieldName, field);
            if (method != null) {
                try {
                    return method.invoke(obj, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (Modifier.isPublic(field.getModifiers()) == false) {
                field.setAccessible(true);
            }
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Method findGetMethod(Class clazz, String fieldName, Field field) {
        if (field == null)  return null;
        Class cl = clazz;
        Method method = null;
        String methodName = "get" + capitalize(fieldName);

        while (cl != null && cl!=Object.class) {
            try {
                method = cl.getDeclaredMethod(methodName, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                if (method != null) return method;
                cl = cl.getSuperclass();
                continue;
            }
        }

        return null;
    }

    /**
     * 初始化第一个字母大写
     * @param name
     * @return
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0)
            return name;

        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * instantiate an Object from a given class name
     * @param className full qualified name of the class
     * @return the instantiated Object
     * @throws Exception
     */
    public static Object createObject(String className) throws Exception {
        return createObject(Class.forName(className));
    }

    /**
     * instantiate an Object instance
     * @param objClass
     * @return
     * @throws Exception
     */
    public static Object createObject(Class objClass) throws Exception {
        return objClass.newInstance();
    }

    /**
     * 设置对象属性值方法
     * @param obj   对象
     * @param propertyName  对象属性
     * @param newValue  值
     */
    public static void executeSetPropertyMethod(Object obj, String propertyName, Object newValue) {
        String setFieldIndexName = "set" + capitalize(propertyName);
        try {
            Class cls = obj.getClass();
            Method methodList[] = cls.getMethods();
            for (Method method:methodList) {
                if (method.getName().equals(setFieldIndexName)) {
                    Class paramTypes[] = method.getParameterTypes();
                    Method setMethod = cls.getMethod(setFieldIndexName, paramTypes);
                    Object argList[] = new Object[1];
                    argList[0] = newValue;
                    setMethod.invoke(obj, argList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
