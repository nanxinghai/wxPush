package cn.simon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/23 22:38
 * @description：
 * @modified By：
 * @version: v1.0
 */
@SpringBootApplication
@EnableScheduling   // 定时任务
public class WxTianqiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxTianqiApplication.class,args);
    }
}
