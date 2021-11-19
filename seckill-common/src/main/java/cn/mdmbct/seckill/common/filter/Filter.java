package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.Participant;

/**
 * 规则过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:22
 * @modified mdmbct
 * @since 0.1
 */
public interface Filter extends Comparable<Filter> {

    /**
     * 获取调用顺序
     * @return 调用顺序
     */
    int getOrder();

    /**
     * 设置下一个过滤器 {@link Filter#getOrder()}获取的到值越大 过滤器越靠前
     * @param filter 下一个过滤器
     */
    void nextFilter(Filter filter);

    /**
     * 过滤逻辑 未抛出{@link BreakRuleException}异常则表示通过过滤器
     * @param participant 参与抽奖的用户
     * @return 秒杀、抽奖等的结果
     */
    CompeteRes doFilter(Participant participant);

    /**
     * 获取该过滤器处理的限制规则
     * @return 该过滤器处理的限制规则
     */
    Rule getRule();

    /**
     * 清理工作
     */
    void clear();

    @Override
    default int compareTo(Filter o) {
        return this.getOrder() - o.getOrder();
    }


    CompeteRes doNextFilter(Participant participant);
}
