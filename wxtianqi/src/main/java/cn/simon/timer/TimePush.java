package cn.simon.timer;

import cn.simon.entity.Weather;
import cn.simon.utils.*;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/28 16:33
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Component
public class TimePush {

    @Value("${wx.appID}")
    private String appID;
    @Value("${wx.appsecret}")
    private String appsecret;
    @Value("${wx.templateId}")
    private String tempId;
    @Value("${wx.YQtempId}")
    private String YQtempId;
    @Autowired
    private TianQiUtil tianQiUtil;

    /**
     * 天气推送
     * @param toUser 发送给谁(用户)
     * @param city  用户城市
     * @param birthday  用户生日
     */
    public void pushTianQi(String toUser,String nickname,String city,String birthday){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(toUser)
                .templateId(tempId)
                .build();
        // 准备数据
        String today = FormatTime.format();
        Weather weather = tianQiUtil.getAll(city);
        String constell = Constellation.getAstro(birthday);
        int birthDay = Tools.getBirthDay(birthday);
        String bir = "";
        if(birthDay > 0 ){
            bir = birthDay + "天";
        }else {
            bir = "生日快乐";
        }
        // 每日一句
        String content = YiJuHuaUtil.getContent();
        // 英语翻译
        String englishContent = YiJuHuaEnglishUtil.getContent(content);
        // 星座运势
        String summary = Constellation.getSummary(constell);
        //3,发送模版消息，这里需要配置你的信息
        templateMessage.addData(new WxMpTemplateData("nickname",nickname+"☺☺☺","#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("today",today+"  \"万事胜意\" ","#4EEE94"));
        templateMessage.addData(new WxMpTemplateData("city",weather.getCity()+"","#7A378B"));
        templateMessage.addData(new WxMpTemplateData("wendu",weather.getWendu()+"度"+"️","#7B68EE"));
        templateMessage.addData(new WxMpTemplateData("weather",weather.getWeather()+"⛅","#7B68EE"));
        templateMessage.addData(new WxMpTemplateData("chuanyi",weather.getChuanyi(),"#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("sunrise_1",weather.getSunrise_1()+"","#CD3278"));
        templateMessage.addData(new WxMpTemplateData("sunset_1",weather.getSunset_1()+",希望日落能治愈不开心","#CD3278"));
        templateMessage.addData(new WxMpTemplateData("constell",constell,"#FF8247"));
        templateMessage.addData(new WxMpTemplateData("bir",bir+"","#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("detail",weather.getDetail(),"#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("content","“" + content + "”","#4EEE94"));
//        templateMessage.addData(new WxMpTemplateData("englishContent",englishContent,"#00BFFF"));
//        templateMessage.addData(new WxMpTemplateData("summary",summary,"#00BFFF"));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
