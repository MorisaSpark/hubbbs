package com.hubbbs.user.scheduler;

import com.hubbbs.user.dao.AccountDao;
import com.hubbbs.user.dao.ReplyDao;
import com.hubbbs.user.dao.ReplyMyDao;
import com.hubbbs.user.pojo.Attenter;
import com.hubbbs.user.service.PostService;
import com.hubbbs.user.utils.StringRedisTemplateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import util.IdWorker;
import util.JwtUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/3/31
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisScheduledTasksTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplateUtil redisTemplateUtil;

    @Autowired
    private PostService postService;

    @Test
    public void persistRedisToMysqlDB() {
        String s1 = "1234";
        String s2 = "11111";
        System.out.println("1234====encoder===="+encoder.encode("1234"));
        System.out.println("11111====encoder===="+encoder.encode("11111"));
        System.out.println("encoder.matches(\"1234\",encoder.encode(\"1234\"))===="+encoder.matches("1234",encoder.encode("1234")));
        System.out.println("encoder.matches(\"11111\",encoder.encode(\"11111\"))===="+encoder.matches("11111",encoder.encode("11111")));
        System.out.println("encoder.matches(\"1234\",encoder.encode(\"11111\"))===="+encoder.matches("1234",encoder.encode("11111")));
        System.out.println("encoder.matches(\"11111\",encoder.encode(\"1234\"))===="+encoder.matches("11111",encoder.encode("1234")));
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private ReplyMyDao replyMyDao;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void synchrodata() {
        Attenter attenter = new Attenter("1", "2", "3", "2");
        Attenter attenter2 = new Attenter("2", "2", "4", "2");
        Attenter attenter3 = new Attenter("2", "3", "4", "2");
        Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
            Set range = redisTemplate.opsForZSet().rangeByScore(key, Long.MIN_VALUE, Long.MAX_VALUE);
            System.out.println(range);
        }
        System.out.println("---------------------------------");
        redisTemplate.delete(keys);
        redisTemplate.opsForZSet().add("attenter::user::" + attenter.getUserId(), attenter.getFanId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::fan::" + attenter.getFanId(), attenter.getUserId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::user::" + attenter2.getUserId(), attenter2.getFanId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::fan::" + attenter2.getFanId(), attenter2.getUserId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::user::" + attenter3.getUserId(), attenter3.getFanId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::fan::" + attenter3.getFanId(), attenter3.getUserId(), Instant.now().toEpochMilli());
        Set keys1 = redisTemplate.keys("attenter::*");
        for (Object key : keys1) {
            Set range = redisTemplate.opsForZSet().rangeByScore(key, Long.MIN_VALUE, Long.MAX_VALUE);
            System.out.println(range);
        }
        System.out.println("---------------------------------");
        redisTemplate.delete(keys1);
        System.out.println("over");
    }
}