package com.deng.service;

import com.deng.spring.Autowired;
import com.deng.spring.Component;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -06 11:50
 * @Modified By:
 */

@Component
public class BService {
    @Autowired
    public AService aService;

}
