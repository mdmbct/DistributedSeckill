package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.Participant;

/**
 * 限流过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 8:57
 * @modified mdmbct
 * @since 0.1
 */
public class CurrentLimitingFilter extends BaseFilter {

    public CurrentLimitingFilter(int order) {
        super(order);
    }

    public CurrentLimitingFilter() {
        super(FIRST_FILTER_ORDER);
    }

    @Override
    public void doFilter(Participant participant, CompeteRes competeRes) {

    }

    @Override
    public void clear() {

    }

    @Override
    public String notPassMsg() {
        return "当前人数过多，请稍后重试";
    }
}
