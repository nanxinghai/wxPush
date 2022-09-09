package cn.simon.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/29 1:59
 * @description：
 * @modified By：
 * @version: v1.0
 */
public class YiJuHuaEnglishUtil {
    public static String getContent(String content){
        // http://www.cncsto.com/article/202030
        String url = "http://fanyi.youdao.com/translate";
        HashMap<String, Object> param = new HashMap<>();
        param.put("doctype","json");
        param.put("type","ZH_CN2EN");
        param.put("i",content);
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONArray translateResult = (JSONArray) jsonObject.get("translateResult");
        JSONObject englishResult = (JSONObject)((JSONArray)translateResult.get(0)).get(0);
        String englishContent = (String) englishResult.get("tgt");
        return englishContent;
    }
}
