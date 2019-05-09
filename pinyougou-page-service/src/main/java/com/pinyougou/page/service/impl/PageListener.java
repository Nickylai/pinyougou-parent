package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description 监听类，收到消息调用网页生成方法
 * @time 2019/5/9 18:10
 * @see
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            Long text = Long.valueOf(textMessage.getText());
            System.out.println("收到消息："+text);
            boolean b = itemPageService.genItemHtml(text);
            System.out.println("是否生成页面："+(b?"ok":"no"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
