package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.Participant;

/**
 * 中奖次数过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:17
 * @modified mdmbct
 * @since 0.1
 */
public class LuckyTimesFilter extends BaseFilter {

    public LuckyTimesFilter(Rule rule) {
        super(rule);
    }

    @Override
    public CompeteRes doFilter(Participant participant) {
        return null;
    }


    @Override
    public void clear() {

    }
}
