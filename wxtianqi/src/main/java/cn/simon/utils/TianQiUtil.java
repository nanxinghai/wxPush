package cn.simon.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.simon.dao.CityDao;
import cn.simon.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/28 23:45
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Component
public class TianQiUtil {
    @Autowired
    private CityDao cityDao;
    @Autowired
    private Dom4jUtil dom4jUtil;

    public String getTianQi(String citycode){
        // https://api.help.bj.cn/api/?id=45
        String url = "https://api.help.bj.cn/apis/weather/";
        HashMap<String, Object> param = new HashMap<>();
        param.put("id",citycode);
        String response = HttpUtil.get(url, param);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        String weather = (String) jsonObject.get("weather");
        return weather;
    }

    public Weather getAll(String city){
        String url = "http://wthrcdn.etouch.cn/WeatherApi";
        HashMap<String, Object> param = new HashMap<>();
        param.put("city",city);
        String response = HttpUtil.get(url, param);
        Weather weatherobj = dom4jUtil.parseXml(response);
        // 字符串截取
        String cityreal = city.substring(0,city.length() - 1);
        String cityCode = cityDao.getCityCode(cityreal).getCitycode();
        // 返回天气
        String weather = getTianQi(cityCode);
        weatherobj.setWeather(weather);
        return weatherobj;
    }
}
