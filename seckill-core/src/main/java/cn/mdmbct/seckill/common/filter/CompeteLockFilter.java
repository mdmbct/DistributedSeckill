package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

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

    public CompeteLockFilter() {
        super(LAST_FILTER_ORDER - 1);
    }

    @Override
    public String notPassMsg() {
        return null;
    }

    @Override
    public void doFilter(Participant participant) {

    }
}
