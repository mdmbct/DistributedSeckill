package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import cn.mdmbct.seckill.common.lock.ProductLock;

/**
 * 竞争锁过滤，倒数第二个过滤器 <br>
 * 只能通过竞争到锁的线程
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:21
 * @modified mdmbct
 * @since 0.1
 */
public class CompeteLockFilter extends BaseFilter {

    private final ProductLock lock;

    public CompeteLockFilter(ProductLock lock) {
        super(LAST_FILTER_ORDER - 1);
        this.lock = lock;
    }

    @Override
    public String notPassMsg() {
        return "下次加油！";
    }

    @Override
    public void doFilter(Participant participant, String productId) {
        // 创建锁并尝试获取锁
        final boolean b = lock.tryLock(productId);
        if (b) {
            System.out.println(Thread.currentThread().getId());
            getFilterContext().setLock(lock);
            doNextFilter(participant, productId);
        } else {
            getFilterContext().setFilterNotPassed(this);
        }
    }
}
