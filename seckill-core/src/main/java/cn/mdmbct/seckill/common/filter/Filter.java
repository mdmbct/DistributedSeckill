package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 过滤器 针对所有请求做过滤
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 17:24
 * @modified mdmbct
 * @since 0.1
 */
public interface Filter extends Comparable<Filter>{

    int FIRST_FILTER_ORDER = Integer.MIN_VALUE;
    int LAST_FILTER_ORDER = Integer.MAX_VALUE;

    void setFilterContext(FilterContext context);

    FilterContext getFilterContext();

    String notPassMsg();


    /**
     * 获取调用顺序
     *
     * @return 调用顺序
     */
    int getOrder();

    /**
     * 设置下一个过滤器 {@link Limiter#getOrder()}获取的到值越小 过滤器越靠前
     *
     * @param filter 下一个过滤器
     */
    void nextFilter(Filter filter);

    /**
     * 过滤逻辑
     *
     * @param participant 参与抽奖的用户
     */
    void doFilter(Participant participant);

    /**
     * 清理工作 如果需要做清理工作必须重写此方法
     */
    default void clear() {

    }

    @Override
    default int compareTo(Filter o) {
        return o.getOrder() - this.getOrder();
    }


    /**
     * 执行下一个过滤器的逻辑
     * @param participant 参与抽奖的用户
     *
     */
    void doNextFilter(Participant participant);
}
