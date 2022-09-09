package cn.simon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/25 22:52
 * @description：2022-08-25 星期四  (当前时间格式化)
 * @modified By：
 * @version: v1.0
 */
public class FormatTime {

    public static String format(){
        SimpleDateFormat myFmt3 = new SimpleDateFormat("yyyy-MM-dd E");
        Date now = new Date();
        return myFmt3.format(now);
    }
}
