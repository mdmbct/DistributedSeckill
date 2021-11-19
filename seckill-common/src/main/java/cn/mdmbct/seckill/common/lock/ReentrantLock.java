package cn.mdmbct.seckill.common.lock;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 10:21
 * @modified mdmbct
 * @since 0.1
 */
public class ReentrantLock implements Lock {


    /**
     * 每个商品一个公平锁，提高并发度
     */
    private final Map<String, java.util.concurrent.locks.ReentrantLock> idLocks;

//    private final Set<String>

    private final int lockWaitTime;

    private final TimeUnit timeUnit;

    public ReentrantLock(int lockWaitTime, TimeUnit timeUnit) {
        this.idLocks = new ConcurrentHashMap<>();
        this.lockWaitTime = lockWaitTime;
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean tryLock(String id) {
        try {
            final java.util.concurrent.locks.ReentrantLock lock = idLocks.computeIfAbsent(id, k -> new java.util.concurrent.locks.ReentrantLock(true));
            return lock.tryLock(lockWaitTime, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unLock(String id) {
        final java.util.concurrent.locks.ReentrantLock lock = idLocks.get(id);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
            // 加这个会导致总有几个倒霉线程获取不到锁
//            idLocks.remove(id);
        }
    }

}
