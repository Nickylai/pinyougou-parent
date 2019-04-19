package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/4/19 22:15
 * @see
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map name() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map=new HashMap<>();
        map.put("loginName", name);
        return map;
    }
}
