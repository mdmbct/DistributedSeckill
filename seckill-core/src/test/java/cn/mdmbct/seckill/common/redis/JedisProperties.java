package cn.mdmbct.seckill.common.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 22:10
 * @modified mdmbct
 * @since 0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JedisProperties implements Serializable {

    private static final long serialVersionUID = 3956553712294474641L;

    /**
     * 主机地址.
     */
    @Builder.Default
    private String host = "127.0.0.1";

    /**
     * 端口号.
     */
    @Builder.Default
    private int port = 6379;

    /**
     * 密码.
     */
    private String password;

    /**
     * 超时.
     */
    @Builder.Default
    private int timeout = 3000;

    /**
     * 数据库 默认1
     */
    @Builder.Default
    private int database = 0;

    /**
     * 最大活动对象数
     */
    private int maxTotal;

    /**
     * 最大能够保持idel状态的对象数
     */
    private int maxIdle;

    /**
     * 当池内没有返回对象时，最大等待时间   -1没有限制
     */
    private int maxWaitMillis;

    private int minIdle;

    public static JedisPool getJedisPool(JedisProperties jedisConfigProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if (jedisConfigProperties.getMaxTotal() != 0) {
            jedisPoolConfig.setMaxTotal(jedisConfigProperties.getMaxTotal());
        }
        if (jedisConfigProperties.getMaxIdle() != 0) {
            jedisPoolConfig.setMaxIdle(jedisConfigProperties.getMaxIdle());
        }
        if (jedisConfigProperties.getMaxWaitMillis() != 0) {
            jedisPoolConfig.setMaxWaitMillis(jedisConfigProperties.getMaxWaitMillis());
        }
        if (jedisConfigProperties.getMinIdle() != 0) {
            jedisPoolConfig.setMinIdle(jedisConfigProperties.getMinIdle());
        }
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return new JedisPool(jedisPoolConfig,
                jedisConfigProperties.getHost(),
                jedisConfigProperties.getPort(),
                jedisConfigProperties.getTimeout(),
                jedisConfigProperties.getPassword(),
                jedisConfigProperties.getDatabase());
    }
}
