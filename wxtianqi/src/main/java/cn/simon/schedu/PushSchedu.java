package cn.simon.schedu;

import cn.simon.dao.UserDao;
import cn.simon.entity.User;
import cn.simon.timer.TimePush;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：Simon
 * @date ：Created in 2022/9/2 1:48
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Component
public class PushSchedu {
    @Autowired
    private UserDao userDao;
    @Autowired
    private TimePush timePush;

    @Scheduled(cron = "0 30 9 * * ? ")
    public void push(){
        List<User> userList = userDao.getAll();
        for (User user : userList) {
            timePush.pushTianQi(user.getUsername(),user.getNickname(),user.getAddress(),user.getBirthday());
        }
    }
}
