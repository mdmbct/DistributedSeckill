package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 库存过滤器 线程竞争到锁后如果发现库存数（红包数）为0 需反向通知该过滤器 <br>
 * 该过滤器使用ReentrantReadWriteLock保证在更新“是否有库存”的时候，其他线程不能读取。<br>
 * 注：ReentrantReadWriteLock允许多个读线程同时访问，但不允许写线程和读线程、写线程和写线程同时访问
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 9:03
 * @modified mdmbct
 * @since 0.1
 */
public class StockStateFilter extends BaseFilter {

    private boolean haveStock = true;

    private ReentrantReadWriteLock stateLock;

    public StockStateFilter(int order) {
        super(order);
        this.stateLock = new ReentrantReadWriteLock();
    }

    @Override
    public void doFilter(Participant participant, FilterRes res) {

        try {
            stateLock.readLock().lock();
            if (!haveStock) {
                res.setFilterNotPassed(this);
                return;
            }
            doNextFilter(participant, res, this);
        } finally {
            stateLock.readLock().unlock();
        }

    }

    @Override
    public void clear() {

    }

    public void updateStockState(boolean haveStock) {
        stateLock.writeLock().lock();
        this.haveStock = haveStock;
        stateLock.writeLock().unlock();
    }

    @Override
    public String notPassMsg() {
        return "没有库存了";
    }
}
