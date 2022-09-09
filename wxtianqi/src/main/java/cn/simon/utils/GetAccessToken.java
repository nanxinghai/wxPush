package cn.simon.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/24 22:10
 * @description：获取access_token
 * @modified By：
 * @version: v1.0
 */
@Component
public class GetAccessToken {
    @Value("${wx.appID}")
    private String appID;
    @Value("${wx.appsecret}")
    private String appsecret;

    public JSONObject getToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        HashMap<String, Object> param = new HashMap<>();
        param.put("grant_type","client_credential");
        param.put("appid",appID);
        param.put("secret",appsecret);
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        // 正常返回示例: {"access_token":"ACCESS_TOKEN","expires_in":7200}
        return jsonObject;
    }

}
