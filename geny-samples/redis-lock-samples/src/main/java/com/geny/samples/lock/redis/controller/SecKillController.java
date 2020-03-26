package com.geny.samples.lock.redis.controller;

import com.geny.samples.lock.redis.domain.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shmily
 */
@RestController
@RequestMapping("skill")
public class SecKillController {

    private static Logger logger = LoggerFactory.getLogger(SecKillController.class);

    @Autowired
    SecKillService secKillService;

    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId) throws Exception {
        return secKillService.querySecKillProductInfo(productId);
    }

    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId) throws Exception {
        logger.info("@skill request, productId:" + productId);
        secKillService.orderProductMockDiffUser(productId);
        return secKillService.querySecKillProductInfo(productId);
    }
}
