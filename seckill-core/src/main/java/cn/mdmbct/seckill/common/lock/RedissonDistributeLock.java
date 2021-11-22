package cn.mdmbct.seckill.common.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redisson分布式锁
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:30
 * @modified mdmbct
 * @since 0.1
 */
public class RedissonDistributeLock implements ProductLock {

    private final RedissonClient redissonClient;

    private int lockWaitTime = 3;

    private int lockExpireTime = 10;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private final String lockCachePrefix;

    /**
     * 默认锁等待时间为3s 过期时间为10s
     * @param redissonClient  redisson客户端
     * @param lockCachePrefix 锁前缀
     */
    public RedissonDistributeLock(RedissonClient redissonClient, String lockCachePrefix) {
        if (lockCachePrefix == null || lockCachePrefix.length() == 0) {
            throw new IllegalArgumentException("参数‘lockCachePrefix’不能为空");
        }
        this.redissonClient = redissonClient;
        this.lockCachePrefix = lockCachePrefix;
    }

    public RedissonDistributeLock(RedissonClient redissonClient,
                                  int lockWaitTime,
                                  int lockExpireTime,
                                  TimeUnit timeUnit,
                                  String lockCachePrefix) {

        if (lockWaitTime <= 0 || lockExpireTime <= 0) {
            throw new IllegalArgumentException("参数‘lockWaitTime’和‘lockExpireTime’都必须大于0");
        }

        if (lockCachePrefix == null || lockCachePrefix.length() == 0) {
            throw new IllegalArgumentException("参数‘lockCachePrefix’不能为空");
        }

        this.redissonClient = redissonClient;
        this.lockWaitTime = lockWaitTime;
        this.lockExpireTime = lockExpireTime;
        this.timeUnit = timeUnit;
        this.lockCachePrefix = lockCachePrefix;
    }

    private boolean tryLock(String lockKey, int waitTime, int expireTime, TimeUnit timeUnit) {
        final RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, expireTime, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tryLock(String id) {
        return tryLock(cacheKey(id),
                lockWaitTime,
                lockExpireTime,
                timeUnit
        );
    }

    @Override
    public void unLock(String id) {
        redissonClient.getLock(cacheKey(id)).unlock();
    }


    @Override
    public ProductLockType getType() {
        return ProductLockType.REDISSON;
    }

    private String cacheKey(String id) {
        return lockCachePrefix + id;
    }
}
