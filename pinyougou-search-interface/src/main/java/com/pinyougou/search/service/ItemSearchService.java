package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/5 19:45
 * @see
 */
public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap 关键字
     * @return
     */
    public Map<String, String> search(Map searchMap);

    /**
     * 导入数据
     * @param list
     */
    public void importList(List list);

    /**
     * 删除数据
     * @param goodsIdList
     */
    public void deleteByGoodsIds(List goodsIdList);

}
