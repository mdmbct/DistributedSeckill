package cn.mdmbct.seckill.common.limiter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 中奖次数过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:17
 * @modified mdmbct
 * @since 0.1
 */
public class LuckyTimesFilter extends BaseLimiter {


    public LuckyTimesFilter(int order) {
        super(order);
    }

    @Override
    public void doLimit(Participant participant, LimitContext context) {

    }

    @Override
    public String notPassMsg() {
        return null;
    }
}
