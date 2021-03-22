package com.hubbbs.user.utils;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/3/31
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StringRedisTemplateUtil{

//    用scan代替key
    public Set<String> scan(StringRedisTemplate redisTemplate, String pattern){
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan( new ScanOptions.ScanOptionsBuilder().match(pattern).count(1000).build());
            while (cursor.hasNext()) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        });
    }
}
