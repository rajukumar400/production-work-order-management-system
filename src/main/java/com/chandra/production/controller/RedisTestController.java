package com.chandra.production.controller;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/set")
    public String setValue(
            @RequestParam String key,
            @RequestParam String value) {

        redisTemplate.opsForValue().set(key, value);

        return "Value stored successfully in Redis";
    }

    @GetMapping("/get")
    public Object getValue(@RequestParam String key) {

        return redisTemplate.opsForValue().get(key);
    }
}
