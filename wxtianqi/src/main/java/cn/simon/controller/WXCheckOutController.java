package cn.simon.controller;

import cn.simon.dao.UserDao;
import cn.simon.entity.TextMessage;
import cn.simon.entity.User;
import cn.simon.timer.TimePush;
import cn.simon.utils.CheckOutUtil;
import cn.simon.utils.FormatTime;
import cn.simon.utils.TXMapUtil;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/24 23:14
 * @description：监听微信验签接口(wx监听url)
 * @modified By：
 * @version: v1.0
 */
@RestController
public class WXCheckOutController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${wx.appID}")
    private String appID;
    @Value("${wx.appsecret}")
    private String appsecret;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TXMapUtil txMapUtil;
    @Autowired
    private TimePush timePush;

    @RequestMapping("/getconn")
    public String connection(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        // https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
        String signature=request.getParameter("signature");
        String timestamp=request.getParameter("timestamp");
        String nonce=request.getParameter("nonce");
        String echostr=request.getParameter("echostr");

        boolean isCheck = CheckOutUtil.checkSignature(signature, timestamp, nonce);

        // 解析xml事件行为
        try {
            Map<String, String> map = CheckOutUtil.parseXml(request);
            System.out.println(map);
            String event = map.get("Event");
            String msgType = map.get("MsgType");
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            // 取消关注移除用户
            if("unsubscribe".equalsIgnoreCase(event)){
                userDao.deleteOne(fromUserName);
            }
            // 如果是关注事件
            // https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
            if("subscribe".equalsIgnoreCase(event)){
                logger.info("微信用户" + fromUserName + ": 已关注公众号!");
                // 关注后发送模板
                push(fromUserName);
                // 添加用户
                userDao.addOne(fromUserName);
            }
            // 准备给对方发送消息
            TextMessage textMessage = new TextMessage();
            textMessage.setMsgType("text");
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            String contentToUser = "";
            // 对方主动发送消息
            if("text".equalsIgnoreCase(msgType)){
                String content = map.get("Content");
                String[] split = content.split("，");
                if(split.length == 1){
                    contentToUser = "格式不对，中间间隔是中文的逗号";
                }else{
                    String nickname = split[0];
                    String birthday = split[1];
                    if(!birthday.matches("^(((?:19|20)\\d\\d)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]))$")){
                        // 昵称和生日格式不对
                        contentToUser = "生日格式不对,请重新发送";
                    }else{
                        userDao.updateOne(nickname,birthday,fromUserName);
                    }
                }
            }
            // 接受对方位置
            if("LOCATION".equalsIgnoreCase(event)){
                logger.info("接受对方位置");
                // 获取经纬度
                String latitude = map.get("Latitude");
                String longitude = map.get("Longitude");
                String address = txMapUtil.getLocation(latitude, longitude);
                // 保存信息
                userDao.updateOne(address,fromUserName);
            }
            if("TEMPLATESENDJOBFINISH".equalsIgnoreCase(event)){
                return null;
            }
            // 如果用户身份信息全部合法，推送早安预报
            User user = userDao.getOne(fromUserName);
            boolean fieldNotNull = isFieldNotNull(user);
            if(fieldNotNull){
                if(user.getIsfrist().equalsIgnoreCase("0")){
                    // 推送早安预报
                    timePush.pushTianQi(user.getUsername(),user.getNickname(),user.getAddress(),user.getBirthday());
                    // 发送完之后更新状态
                    userDao.updateOneStatus(fromUserName);
                }
            }else{
                if(contentToUser.equalsIgnoreCase("")){
                    contentToUser = "请允许我们接受地理位置，方便为你推送当前城市天气";
                }
            }
            // 发送消息
            textMessage.setContent(contentToUser);
            String respMessage = CheckOutUtil.textMessageToXml(textMessage);
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.write(respMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isCheck){
            logger.info("验签成功!");
            return echostr;
        }
        if(!isCheck){
            logger.info("验签失败!");
            return null;
        }

        return null;
    }

    /**
     * 用户关注后推送
     */
    public void push(String fromUserName){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(fromUserName)
                .templateId("kDf15AeObEQhiphKHgUu7rZau1lFuvhHVnyYpCs3gQM")
                .build();
        // 3.发送数据
        templateMessage.addData(new WxMpTemplateData("love","测试公众号-早安推送☺","#4EEE94"));
        templateMessage.addData(new WxMpTemplateData("goodmorning","早上好呀~  小二","#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("enjoy","每天都是元气满满的一天吖~  祝你开心~ ","#40E0D0"));
        templateMessage.addData(new WxMpTemplateData("today", FormatTime.format(),"#CD3278"));
        templateMessage.addData(new WxMpTemplateData("city"," 成都 ️ ","#FFEC8B"));
        templateMessage.addData(new WxMpTemplateData("weatherinfo","多云 ️ ","#BA55D3"));
        templateMessage.addData(new WxMpTemplateData("temperature"," 34°C️ ","#7A378B"));
        templateMessage.addData(new WxMpTemplateData("direct"," 南风 ️ ","#8A2BE2"));
        templateMessage.addData(new WxMpTemplateData("power"," 3级 ️ ","#8B1C62"));
        templateMessage.addData(new WxMpTemplateData("constellation"," 处女座","#8B4C39"));
        templateMessage.addData(new WxMpTemplateData("birthday","距离你的生日还有2天","#FF6347"));
        templateMessage.addData(new WxMpTemplateData("context","“热爱可抵岁月漫长”","#EF8CB7"));
        templateMessage.addData(new WxMpTemplateData("englishContext","Solitude is the soul’s holiday, an opportunity to stop doing for others and to surprise and delight ourselves instead.","#EF8CB7"));
        templateMessage.addData(new WxMpTemplateData("lucky","无论是有无对象的狮子，今天的恋爱运都不是很好。单身的人虽有满腔的热血，但是落花有意流水无情，付出的感情也不太容易得到回应；而有伴侣的人，与对方的感情开始出现考验，情海生变的机率大增，不可不慎！","#00BFFF"));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isFieldNotNull(Object obj){
        //拿到对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        //标识
        boolean flag = true;
        //遍历所有字段
        for (Field field : fields) {
            try {
                //开启权限
                field.setAccessible(true);
                //判断是否有值
                if ((field.get(obj)) == null || (field.get(obj)).equals("")){
                    flag=false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //关闭权限
                field.setAccessible(false);
            }
        }
        return flag;
    }

}
