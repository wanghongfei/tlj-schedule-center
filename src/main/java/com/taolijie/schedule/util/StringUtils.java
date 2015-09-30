package com.taolijie.schedule.util;

/**
 * Created by whf on 9/30/15.
 */
public class StringUtils {
    private StringUtils() {}

    /**
     * 字符串拼接。
     * @param cap 可以指定一个估计的拼接后的字符串长度, 提高性能. 传递0表示不指定长度
     * @param objs
     * @return
     */
    public static String concat(int cap, Object... objs) {
        if (null == objs) {
            return null;
        }

        if (cap <= 0) {
            cap = 40;
        }

        StringBuilder sb = new StringBuilder(cap);

        for (Object o : objs) {
            sb.append(o.toString());
        }

        return sb.toString();
    }
}
