package com.taolijie.schedule.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String stream2String(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);

        char[] cbuf = new char[200];
        int len = -1;

        StringBuilder sb = new StringBuilder();
        while ( (len = reader.read(cbuf)) != -1 ) {
            sb.append(cbuf, 0, len);
        }

        return sb.toString();
    }

    public static Date str2Date(String str) throws ParseException {
        if (null == str) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(str);
    }
}
