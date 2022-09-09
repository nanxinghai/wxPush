package cn.simon.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/25 22:18
 * @description：计算星座
 * @modified By：
 * @version: v1.0
 */
public class Constellation {

    public static String getAstro(String birthday) {
        String[] split = birthday.split("-");
        Integer month = Integer.valueOf(split[1]);
        Integer day = Integer.valueOf(split[2]);
        String[] starArr = {"魔羯座","水瓶座", "双鱼座", "牡羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座" };
        int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return starArr[index];
    }

    public static String getSummary(String constell){
        // http://www.free-api.com/doc/239
        String url = "http://web.juhe.cn:8080/constellation/getAll";
        HashMap<String, Object> param = new HashMap<>();
        param.put("key","4a11bbcbf089edaf14c2d9bdb80c2ec4");
        param.put("consName",constell);
        param.put("type","today");
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return (String) jsonObject.get("summary");
    }
}
