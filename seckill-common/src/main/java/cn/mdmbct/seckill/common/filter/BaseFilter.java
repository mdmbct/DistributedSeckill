package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.Participant;
import lombok.RequiredArgsConstructor;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:12
 * @modified mdmbct
 * @since 0.1
 */
@RequiredArgsConstructor
public abstract class BaseFilter implements Filter {

    protected Filter nextFilter;

    protected final Rule rule;

    @Override
    public int getOrder() {
        return getRule().getOrder();
    }

    @Override
    public Rule getRule() {
        return rule;
    }



    @Override
    public void nextFilter(Filter filter) {
        this.nextFilter = filter;
    }

    @Override
    public CompeteRes doNextFilter(Participant participant) {
        if (nextFilter != null) {
           return nextFilter.doFilter(participant);
        }
        return null;
    }
}
