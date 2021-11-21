package cn.mdmbct.seckill.common.limiter;

import cn.mdmbct.seckill.common.Participant;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 限制链
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:50
 * @modified mdmbct
 * @since 0.1
 */
public class LimiterChain {

    private final List<Limiter> limiters;

    @Getter
    private final LimitContext context;

    public LimiterChain(List<Limiter> limiters) {
        this.limiters = limiters;
        init();
        this.context = new LimitContext();
    }

    public void doLimit(Participant participant) {
        if (limiters.size() != 0) {
            // 执行限制的逻辑 并更新context
            limiters.get(0).doLimit(participant, context);
        }
    }


    private void init() {
        Collections.sort(limiters);
        for (int i = 0; i < limiters.size(); i++) {

            Limiter filter = limiters.get(i);
            if (i == limiters.size() - 1) {
                filter.nextLimiter(null);
                return;
            }
            filter.nextLimiter(limiters.get(i + 1));
        }
    }

    /**
     * 清理工作
     */
    public void clear() {
       limiters.forEach(Limiter::clear);
    }

}
