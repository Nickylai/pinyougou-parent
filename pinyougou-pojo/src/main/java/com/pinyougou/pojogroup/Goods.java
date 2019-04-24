package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description 商品的组合实体类
 * @time 2019/4/24 14:37
 * @see
 */
public class Goods implements Serializable {
    /**
     * 商品基本信息
     */
    private TbGoods goods;
    /**
     * 商品spu扩展信息
     */
    private TbGoodsDesc goodsDesc;
    /**
     * 商品的sku信息列表
     */
    private List<TbItem> itemList;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
