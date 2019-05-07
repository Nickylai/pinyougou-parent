package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
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
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);
        //3.查询品牌列表和规格列表
        String category = (String) searchMap.get("category");
        if (!category.equals("")) {
            map.putAll(searchBrandAndSpecList(category));
        } else{
            if (categoryList.size() > 0) {
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }


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

        //1.1关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2按照商品分类过滤
        String category = "category";
        if (!"".equals(searchMap.get(category))) {
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3品牌过滤
        String brand = "brand";
        if (!"".equals(searchMap.get(brand))) {
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.4规格过滤
        String spec="spec";
        if (searchMap.get(spec)!=null) {
            Map<String,String> specMap = (Map<String,String>) searchMap.get(spec);
            for (String key : specMap.keySet()) {
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //1.5价格过滤
        String price = "price";
        String greaterPrice = "0";
        String lessPrice = "以上";
        if (!"".equals(searchMap.get(price))) {
            String[] priceList = ((String) searchMap.get(price)).split("-");
            //如果最低价不等于0，将价格数组0位赋予给区间起点
            if (!greaterPrice.equals(priceList[0])) {
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(priceList[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            //如果最高价不等于“以上”，将价格数组1位赋予给区间终点；如果等于“以上”，则没有上限
            if (!lessPrice.equals(priceList[1])) {
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").lessThanEqual(priceList[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

        }

        //1.6分页查询
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null) {
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize = 20;
        }

        query.setOffset((pageNo - 1) * pageSize);
        query.setRows(pageSize);

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
        //查询结果
        map.put("rows", page.getContent());
        //分页查询后的总页数
        map.put("totalPages", page.getTotalPages());
        //分页查询的总记录数
        map.put("totalElements", page.getTotalElements());

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

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 查询品牌和规格列表
     * @return
     */
    private Map searchBrandAndSpecList(String category) {
        Map map = new HashMap();
        //1.根据商品分类名称得到模板id
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (templateId != null) {
            //2.根据模板id或的品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList",brandList);
            //3.根据模板id获得规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList", specList);
        }
        return map;
    }
}
