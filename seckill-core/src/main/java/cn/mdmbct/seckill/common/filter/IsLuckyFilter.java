package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 是否中奖过滤器 将是否中奖前置到竞争锁之前 提高效率
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 9:02
 * @modified mdmbct
 * @since 0.1
 */
public class IsLuckyFilter extends BaseFilter {
    public IsLuckyFilter(int order) {
        super(order);
    }

    @Override
    public void doFilter(Participant participant, FilterRes res) {
        doNextFilter(participant, res, this);
    }

    @Override
    public String notPassMsg() {
        return "未中奖！";
    }
}
