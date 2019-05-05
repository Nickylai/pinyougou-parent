package com.pinyougou.solrutil;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/5 19:04
 * @see
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    public void importItemData() {
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //条件审核通过的商品
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("--------商品列表---------");
        for (TbItem item : itemList) {
            System.out.println(item.getId()+" "+item.getTitle()+" "+item.getPrice());
        }
        System.out.println("------------结束------------");
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }
}
