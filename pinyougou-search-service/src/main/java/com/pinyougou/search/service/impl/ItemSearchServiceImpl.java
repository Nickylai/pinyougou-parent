package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/5 19:48
 * @see
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, String> search(Map searchMap) {
        Map map = new HashMap();
//        Query query = new SimpleQuery("*:*");
//        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
//        query.addCriteria(criteria);
//        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
//        map.put("rows", page.getContent());

        //1.查询列表
        map.putAll(searchList(searchMap));
        //2.分组查询商品分类列表
        map.put("categoryList",searchCategoryList(searchMap));
        return map;
    }
    /**
     * 查询列表
     */

    private Map searchList(Map searchMap) {
        Map map = new HashMap();
        //高亮显示
        HighlightQuery query=new SimpleHighlightQuery();
        //高亮域
        HighlightOptions highlightOptions=new HighlightOptions().addField("item_title");
        //前缀后缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        //为查询对象设置高亮
        query.setHighlightOptions(highlightOptions);

        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮集合入口
        List<HighlightEntry<TbItem>> list = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : list) {
            //获取高亮列（高亮域的数量）
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            /*
            for (HighlightEntry.Highlight h : highlightList) {
                //每个域可能有多个存储值
                List<String> sns = h.getSnipplets();
                System.out.println(sns);
            }*/
            if (highlightList.size()>0&&highlightList.get(0).getSnipplets().size()>0) {
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }
        map.put("rows", page.getContent());
        return map;

    }

    /**
     * 分组查询
     */
    private List<String> searchCategoryList(Map searchMap) {
        List<String> list = new ArrayList<>();
        Query query=new SimpleQuery("*:*");
        //关键字查询，相当于sql的where查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置分组查询的选项
        GroupOptions groupOptions=new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //获取分组查询到的页面
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //获取分组结果
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

        for (GroupEntry<TbItem> entry : entryList) {
            //将分页的结果添加到返回值
            list.add(entry.getGroupValue());
        }

        return list;
    }
}
