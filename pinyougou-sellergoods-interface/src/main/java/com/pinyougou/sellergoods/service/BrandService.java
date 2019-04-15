package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @author Nickylai
 * @version 0.0.1
 * @description 品牌接口
 * @time 2019/4/15 16:16
 * @see
 */
public interface BrandService {
    public List<TbBrand> findAll();
}
