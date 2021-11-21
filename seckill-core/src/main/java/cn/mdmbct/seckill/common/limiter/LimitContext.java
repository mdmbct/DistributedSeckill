package cn.mdmbct.seckill.common.limiter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则限制上下文 贯穿某线程经过的限制链
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 15:01
 * @modified mdmbct
 * @since 0.1
 */
@ToString
public class LimitContext {

    @Getter
    private final List<Limiter> limitersPassed;

    @Setter
    @Getter
    private Limiter limiterNotPassed;

    public LimitContext() {
        this.limitersPassed = new ArrayList<>();
    }

    public void addLimiterPassed(Limiter limiter) {
        limitersPassed.add(limiter);
    }

}
