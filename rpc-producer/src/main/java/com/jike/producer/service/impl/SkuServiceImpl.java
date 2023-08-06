package com.jike.producer.service.impl;

import com.jike.producer.service.SkuService;

public class SkuServiceImpl implements SkuService {

    @Override
    public String findByName(String name) {
        return "SKU{}: " + name;
    }
}
