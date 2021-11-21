package cn.mdmbct.seckill.common.limiter;

import cn.mdmbct.seckill.common.Participant;
import lombok.RequiredArgsConstructor;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:12
 * @modified mdmbct
 * @since 0.1
 */
@RequiredArgsConstructor
public abstract class BaseLimiter implements Limiter {

    protected Limiter nextLimiter;

    protected final int order;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void nextLimiter(Limiter limiter) {
        this.nextLimiter = limiter;
    }

    @Override
    public void doNextLimit(Participant participant, LimitContext res) {
        res.addLimiterPassed(this);
        if (nextLimiter != null) {
           nextLimiter.doLimit(participant, res);
        }
    }
}
