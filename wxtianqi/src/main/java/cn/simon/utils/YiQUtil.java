package cn.simon.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/9/1 2:27
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Component
public class YiQUtil {

    public static void get(){
        String url = "http://api.tianapi.com/ncov/index";
        HashMap<String, Object> param = new HashMap<>();
        param.put("key","b1b6ab8b1a70497ca6a024f500753730");
        param.put("date","2022-09-02");
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        System.out.println(jsonObject);
    }

    public static void main(String[] args) {
        get();
    }
}
