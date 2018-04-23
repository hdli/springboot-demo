package com.example.demo.common.redis;

import com.example.demo.DemoApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by hdli on 2018-4-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestCRedis {
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private StringRedisTemplate redis0;
    @Autowired
    private StringRedisTemplate redis1;

    @Test
    public void test() throws Exception {
        // 保存字符串
//        stringRedisTemplate.opsForValue().set("aaa", "111");
//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

        redis0.opsForValue().set("bbb","0000");
        redis1.opsForValue().set("bbb","2222");
        Assert.assertEquals("0000", redis0.opsForValue().get("bbb"));
        Assert.assertEquals("2222", redis1.opsForValue().get("bbb"));
    }

    @Test
    public void testDb(){

    }
}
