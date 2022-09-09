package cn.simon.test;

import cn.simon.controller.SendController;
import cn.simon.entity.Weather;
import cn.simon.utils.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/23 22:51
 * @description：
 * @modified By：
 * @version: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {cn.simon.WxTianqiApplication.class})
public class SendTest {

    @Autowired
    private SendController sendController;
    @Autowired
    private GetAccessToken getAccessToken;
    @Autowired
    private TXMapUtil txMapUtil;
    @Autowired
    private TianQiUtil tianQiUtil;
    @Autowired
    private Dom4jUtil dom4jUtil;

    @Test
    public void test01(){
        Constellation.getSummary("处女座");
    }

    @Test
    public void testSend(){
        sendController.push();
    }

    @Test
    public void testGetToken(){
        getAccessToken.getToken();
    }

    @Test
    public void testGetLocation(){
        txMapUtil.getLocation("39.984154","116.307490");
    }

    @Test
    public void testGetTianQi(){
        String city = "成都市";
        Weather all = tianQiUtil.getAll(city);
        System.out.println(all);
//        tianQiUtil.get();

    }

    @Test
    public void testGetYIjuhua(){
        YiJuHuaUtil.getContent();
    }
}
