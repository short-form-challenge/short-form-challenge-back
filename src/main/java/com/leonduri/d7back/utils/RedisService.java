//package com.leonduri.d7back.utils;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//@RequiredArgsConstructor
//public class RedisService {
//    private RedisTemplate redisTemplate;
//
//    public void set(String key, String value) {
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        values.set(key, value, Duration.ofMinutes(1));
//    }
//
//    public String get(String key) {
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        return values.get(key);
//    }
//
//    public void delete(String key) {
//        redisTemplate.delete(key.substring(7));
//    }
//
//}
