package cn.simon.entity;

import lombok.Data;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/27 1:24
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Data
public class TextMessage {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String Content;
}
