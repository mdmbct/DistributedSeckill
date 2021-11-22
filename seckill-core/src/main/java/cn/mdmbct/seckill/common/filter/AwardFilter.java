package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 奖励过滤器，最后一个过滤器 <br>
 * 得到锁之后，操作仓库数量减一 <br>
 * 只能通过从仓库中拿到奖励的线程 <br>
 * 注：奖励可以是抽奖时的奖品、秒杀到的商品、抢到的红包
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:22
 * @modified mdmbct
 * @since 0.1
 */
public class AwardFilter extends BaseFilter {
    public AwardFilter() {
        super(LAST_FILTER_ORDER);
    }

    @Override
    public String notPassMsg() {
        return null;
    }

    @Override
    public void doFilter(Participant participant) {

    }
}
