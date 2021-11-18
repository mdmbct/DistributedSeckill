package cn.mdmbct.seckill.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 商品、奖品锁接口
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:27
 * @modified mdmbct
 * @since 1.0
 */
public interface Lock {

//
//    /**
//     * 尝试加锁
//     *
//     * @param lockKey  锁的key
//     * @param waitTime 等待时间
//     * @param timeUnit {@link TimeUnit}
//     * @return 是否加锁成功
//     */
//    boolean tryLock(String lockKey, int waitTime, TimeUnit timeUnit);
//
//    /**
//     * 尝试加锁
//     *
//     * @param lockKey    锁的key
//     * @param waitTime   等待时间
//     * @param expireTime 锁过期时间 时间过后自动释放锁
//     * @param timeUnit   {@link TimeUnit}
//     * @return 是否解锁成功
//     */
//    boolean tryLock(String lockKey, int waitTime, int expireTime, TimeUnit timeUnit);

    /**
     *
     * @param id 商品、奖品的id
     * @return
     */
    boolean tryLock(String id);

    /**
     * 释放锁
     *
     * @param id 商品、奖品的id
     */
    void unLock(String id);
}
