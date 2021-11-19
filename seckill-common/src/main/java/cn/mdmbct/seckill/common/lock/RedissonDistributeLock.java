package cn.mdmbct.seckill.common.lock;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.nio.file.Watchable;
import java.util.concurrent.TimeUnit;

/**
 * Redisson分布式锁
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:30
 * @modified mdmbct
 * @since 0.1
 */
public class RedissonDistributeLock implements Lock {

    private final RedissonClient redissonClient;

    private final int lockWaitTime;

    private final int lockExpireTime;

    private final TimeUnit timeUnit;

    private final String lockCachePrefix;

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

    private String cacheKey(String id) {
        return lockCachePrefix + id;
    }
}
