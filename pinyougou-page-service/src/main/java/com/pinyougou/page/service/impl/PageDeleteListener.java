package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;


/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/9 18:50
 * @see
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("收到消息："+goodsIds);
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("删除："+(b?"ok":"no"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
