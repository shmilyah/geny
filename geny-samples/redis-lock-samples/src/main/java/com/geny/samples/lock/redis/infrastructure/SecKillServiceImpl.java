package com.geny.samples.lock.redis.infrastructure;

import com.geny.components.redis.lock.annotation.Lockable;
import com.geny.samples.lock.redis.domain.SecKillService;
import com.geny.samples.lock.redis.infrastructure.exception.SecKillException;
import com.geny.samples.lock.redis.infrastructure.utils.KeyUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shmily
 */
@Service
public class SecKillServiceImpl implements SecKillService {

    // 模拟产品表
    static Map<String,Integer> products;

    // 模拟库存表
    static Map<String,Integer> stock;

    // 模拟订单表
    static Map<String,String> orders;

    static {
        products = new HashMap<String, Integer>();
        stock = new HashMap<String, Integer>();
        orders = new HashMap<String, String>();
        products.put("123456", 500);
        stock.put("123456", 500);
    }

    private String queryMap(String productId) {
        return "国庆活动，iPhone XSM 特价，限量份"
                + products.get(productId)
                + " 还剩：" + stock.get(productId) + " 份"
                + " 该商品成功下单用户数目："
                + orders.size() + " 人";
    }


    public String querySecKillProductInfo(String productInfo) {
        return this.queryMap(productInfo);
    }

    @Lockable(expiration = 60000,maxWait = 1000,key = {"#productId"})
    public void orderProductMockDiffUser(String productId) {

        //1.查询商品库存，为 0 则活动结束
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new SecKillException(100, "活动结束");
        } else {
            //2.下单（模拟不同用户 id 不同）
            orders.put(KeyUtil.genUniqueKey(), productId);
            //3.减库存
            stockNum = stockNum - 1;
            try {
                // 模拟耗时请求
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }

    }
}
