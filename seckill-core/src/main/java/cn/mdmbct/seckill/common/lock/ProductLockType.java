package cn.mdmbct.seckill.common.lock;

/**
 * 产品锁类型
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:45
 * @modified mdmbct
 * @since 0.1
 */
public enum ProductLockType {

    /**
     * Zookeeper分布式锁
     */
    ZK,

    /**
     * Redisson分布式锁
     */
    REDISSON,

    /**
     * Java Reentrant公平锁
     */
    REENTRANT;
}
