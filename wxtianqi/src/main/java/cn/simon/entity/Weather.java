package cn.simon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/29 0:55
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private String city;
    private String wendu;
    private String weather;
    private String chuanyi;
    private String sunrise_1;
    private String sunset_1;
    public String detail;

    public Weather(String city, String wendu, String chuanyi, String sunrise_1, String sunset_1, String detail) {
        this.city = city;
        this.wendu = wendu;
        this.chuanyi = chuanyi;
        this.sunrise_1 = sunrise_1;
        this.sunset_1 = sunset_1;
        this.detail = detail;
    }
}
