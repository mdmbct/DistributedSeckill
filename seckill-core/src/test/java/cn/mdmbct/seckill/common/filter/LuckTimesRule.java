package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.filter.BaseRule;
import lombok.Getter;

/**
 * 中奖次数限制
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:09
 * @modified mdmbct
 * @since 0.1
 */
@Getter
public class LuckTimesRule extends BaseRule {

    private final long daysMillisecond;

    private final int days;

    private final int times;

    public LuckTimesRule(int days, int times, int order) {
        super(order);
        this.times = times < 0 ? Integer.MAX_VALUE : times;
        this.days = days < 0 ? Integer.MAX_VALUE : days;
        this.daysMillisecond = days * 24L * 60 * 60 * 1_000;
    }

    @Override
    public String getBreakMsg() {
        return "每人每" + days + "天最多可中奖" + times + "次";
    }

}
