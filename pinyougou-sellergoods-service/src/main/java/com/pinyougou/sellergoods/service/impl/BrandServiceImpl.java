package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/4/15 16:20
 * @see
 */
@Service(timeout = 12000)
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        System.out.println(22222222);
        return brandMapper.selectByExample(null);
    }

    /**
     * 品牌分页
     * @param pageNum 当前页面
     * @param pageSize 当前页记录
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {

        //分页
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        long total = page.getTotal();
        List<TbBrand> result = page.getResult();
        
        return new PageResult(total,result);
    }
}
