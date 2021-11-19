package cn.mdmbct.seckill.common.redis;

import lombok.RequiredArgsConstructor;
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
        //  try-with-resources statement => try中的资源自动关闭
        try (Jedis jedis = jedisPool.getResource()) {
            if (expire <= 0) {
                set(key, value);
            } else {
                jedis.psetex(key, timeUnit.toMillis(expire), value);
            }
        }
    }

    @Override
    public void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    @Override
    public Long incr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        }
    }

    @Override
    public Long decr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(key);
        }
    }


}
