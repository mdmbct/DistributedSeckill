package cn.mdmbct.seckill.common.limiter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 规则限制 针对具体某个请求做限制
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:22
 * @modified mdmbct
 * @since 0.1
 */
public interface Limiter extends Comparable<Limiter> {

    int FIRST_LIMITER_ORDER = Integer.MIN_VALUE;
    int LAST_LIMITER_ORDER = Integer.MAX_VALUE;

    /**
     * 获取调用顺序
     *
     * @return 调用顺序
     */
    int getOrder();

    /**
     * 设置下一个限制 {@link Limiter#getOrder()}获取的到值越小 限制越靠前
     *
     * @param limiter 下一个过滤器
     */
    void nextLimiter(Limiter limiter);

    /**
     * 限制逻辑
     *
     * @param participant 参与抽奖的用户
     */
    void doLimit(Participant participant, LimitContext context);

    /**
     * 清理工作 如果需要做清理工作必须重写此方法
     */
    default void clear() {

    }

    @Override
    default int compareTo(Limiter o) {
        return o.getOrder() - this.getOrder();
    }


    /**
     * 执行下一个限制的逻辑
     * @param participant 参与抽奖的用户
     *
     */
    void doNextLimit(Participant participant, LimitContext res);

    String notPassMsg();
}
