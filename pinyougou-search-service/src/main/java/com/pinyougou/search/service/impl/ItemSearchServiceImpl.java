package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.search.service.ItemSearchService;

import java.util.Map;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/5 19:48
 * @see
 */
@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {


    @Override
    public Map<String, String> search(Map searchMap) {
        return null;
    }
}
