package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

/**
 * @author Nickylai
 * @version 0.0.1
 * @description 品牌接口
 * @time 2019/4/15 16:16
 * @see
 */
public interface BrandService {
    /**
     * 查询所有
     * @return 结果集
     */
    public List<TbBrand> findAll();

    /**
     * 分页查询
     * @param pageNum 当前页
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 增加品牌
     * @param brand 品牌实体
     */
    public void add(TbBrand brand);

    /**
     * 根据id查询产品
     * @param id 产品id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 更新修改实体
     * @param brand 实体
     */
    public void update(TbBrand brand);
}
