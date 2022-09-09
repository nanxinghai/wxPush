package cn.simon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/25 22:57
 * @description：
 * @modified By：
 * @version: v1.0
 */
public class Tools {
    /**
     *  计算距离生日还有多少天
     * @param addtime：生日日期
     */
    public static int getBirthDay(String addtime) {
        int days = 0;
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String clidate = addtime;
            Calendar cToday = Calendar.getInstance(); // 存今天
            Calendar cBirth = Calendar.getInstance(); // 存生日
            cBirth.setTime(myFormatter.parse(clidate)); // 设置生日
            cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
            if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
                // 生日已经过了，要算明年的了
                days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
                days += cBirth.get(Calendar.DAY_OF_YEAR);
            } else {
                // 生日还没过
                days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static void main(String[] args) {
        System.out.println(getBirthDay("2022-08-24"));
    }
}
