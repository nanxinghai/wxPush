package cn.simon.controller;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/23 22:47
 * @description：消息推送业务类
 * @modified By：
 * @version: v1.0
 */
@Component
public class SendController {
    @Value("${wx.appID}")
    private String appID;
    @Value("${wx.appsecret}")
    private String appsecret;

    public void push(){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("otFw-5owlP_sCTrEkeqnoxsB5a_4")
                .templateId("8ReplMmAQOr9pm2Cly36DyAe5ZRBERWSJRccjTS14m8")
                .build();
        //3,发送模版消息，这里需要配置你的信息
        templateMessage.addData(new WxMpTemplateData("today","2022-09-04 星期日","#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("city","成都","#4169E1"));
        templateMessage.addData(new WxMpTemplateData("weather","多云","#B22222"));
        templateMessage.addData(new WxMpTemplateData("wendu","21度","#FF7256"));
        templateMessage.addData(new WxMpTemplateData("chuanyi","天气舒适，建议薄外套，衬衫","#FF34B3"));
        templateMessage.addData(new WxMpTemplateData("sunrise_1","06:35","#9ACD32"));
        templateMessage.addData(new WxMpTemplateData("sunset_1","19:09","#9ACD32"));
        templateMessage.addData(new WxMpTemplateData("constell","处女座","#87CEFF"));
        templateMessage.addData(new WxMpTemplateData("lianai","432","#C87CFB"));
        templateMessage.addData(new WxMpTemplateData("baobir","距离宝宝的生日还有78天","#00CCFF"));
        templateMessage.addData(new WxMpTemplateData("beibir","距离贝贝的生日还有120天","#00CCFF"));
        templateMessage.addData(new WxMpTemplateData("content","“爱意东升西落，浪漫至死不渝”","#FF69B4"));
        templateMessage.addData(new WxMpTemplateData("engContent","“Love rises in the East and falls in the west, romantic until death”","#FF69B4"));

        try {
            System.out.println(templateMessage.toJson());
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

}
