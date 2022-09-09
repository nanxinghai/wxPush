package cn.simon.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/28 14:30
 * @description：腾讯地图api
 * @modified By：
 * @version: v1.0
 */
@Component
public class TXMapUtil {

    @Value("${tx.key}")
    private String key;
    @Value("${tx.SK}")
    private String SK;

    /**
     * 根据经纬度调取api获取地理位置
     * @param latitude 纬度
     * @param longitude 经度
     * @return
     */
    public String getLocation(String latitude,String longitude){
        // https://lbs.qq.com/service/webService/webServiceGuide/webServiceGcoder
        String url = "https://apis.map.qq.com/ws/geocoder/v1/";
        HashMap<String, Object> param = new HashMap<>();
        param.put("key", key);
        String localtion = latitude + "," + longitude;
        param.put("location",localtion);
        // sign加密
        String sig = DigestUtil.md5Hex("/ws/geocoder/v1/?key=" + key + "&location=" + localtion + SK);
        param.put("sig",sig);
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONObject result = (JSONObject)jsonObject.get("result");
        JSONObject ad_info = (JSONObject)result.get("ad_info");
        String city = (String)ad_info.get("city");
        return city;
    }

}
