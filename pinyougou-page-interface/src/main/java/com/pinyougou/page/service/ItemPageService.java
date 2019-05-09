package com.pinyougou.page.service;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/8 10:03
 * @see
 */
public interface ItemPageService {

    /**
     * 生成商品详情页
     * @param goodsId 商品id
     * @return
     */
    public boolean genItemHtml(Long goodsId);

    /**
     * 删除商品详情页
     * @param goodsIds
     * @return
     */
    public boolean deleteItemHtml(Long[] goodsIds);
}
