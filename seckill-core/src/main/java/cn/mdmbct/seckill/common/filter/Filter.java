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

    public static final int FIRST_FILTER_ORDER = -10;

    /**
     * 获取调用顺序
     *
     * @return 调用顺序
     */
    int getOrder();

    /**
     * 设置下一个过滤器 {@link Filter#getOrder()}获取的到值越小 过滤器越靠前
     *
     * @param filter 下一个过滤器
     */
    void nextFilter(Filter filter);

    /**
     * 过滤逻辑 未抛出{@link NotPassFilterException}异常则表示通过过滤器
     *
     * @param participant 参与抽奖的用户
     * @param competeRes  秒杀、抽奖等的结果，未通过的过滤器应设置在其中
     * @throws NotPassFilterException 未通过过滤器后抛出异常
     */
    void doFilter(Participant participant, CompeteRes competeRes) throws NotPassFilterException;

    /**
     * 清理工作
     */
    void clear();

    @Override
    default int compareTo(Filter o) {
        return o.getOrder() - this.getOrder();
    }


    /**
     * 执行下一个过滤器的逻辑
     *
     * @param participant 参与抽奖的用户
     * @param competeRes  秒杀、抽奖等的结果，未通过的过滤器应设置在其中
     * @throws NotPassFilterException 未通过过滤器后抛出异常
     */
    void doNextFilter(Participant participant, CompeteRes competeRes) throws NotPassFilterException;

    String notPassMsg();
}
