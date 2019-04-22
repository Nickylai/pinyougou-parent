package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description 显示登录名
 * @time 2019/4/22 16:34
 * @see
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map name() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("===================================");
        System.out.println(name);
        System.out.println("===================================");

        Map map = new HashMap<>();
        map.put("loginName", name);
        return map;
    }
}
