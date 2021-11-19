package cn.mdmbct.seckill.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

import java.util.concurrent.TimeUnit;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 20:03
 * @modified mdmbct
 * @since 0.1
 */
@RequiredArgsConstructor
public class JedisRedisOps implements RedisOps {

    private final Pool<Jedis> jedisPool;


    @Override
    public void set(String key, String value, long expire, TimeUnit timeUnit) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (expire <= 0) {
                set(key, value);
            } else {
                jedis.psetex(key, timeUnit.toMillis(expire), value);
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.incr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.decr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
