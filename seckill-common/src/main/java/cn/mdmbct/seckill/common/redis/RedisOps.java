package cn.mdmbct.seckill.common.redis;

import java.util.concurrent.TimeUnit;

/**
 * 操作Redis的接口
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:57
 * @modified mdmbct
 * @since 1.0
 */
public interface RedisOps {


    void set(String key, String value, long expire, TimeUnit timeUnit);

    void set(String key, String value);

    void incr(String key);

    void decr(String key);
}
