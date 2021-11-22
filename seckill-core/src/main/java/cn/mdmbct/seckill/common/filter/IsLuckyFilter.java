package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:14
 * @modified mdmbct
 * @since 0.1
 */
public class IsLuckyFilter extends BaseFilter {
    public IsLuckyFilter(int order) {
        super(order);
    }

    @Override
    public String notPassMsg() {
        return "未中奖！";
    }

    @Override
    public void doFilter(Participant participant, String productId) {
        doNextFilter(participant, productId);
    }
}
