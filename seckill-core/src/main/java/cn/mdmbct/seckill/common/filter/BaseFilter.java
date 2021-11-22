package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import cn.mdmbct.seckill.common.context.FilterContext;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 17:26
 * @modified mdmbct
 * @since 0.1
 */
public abstract class BaseFilter implements Filter {

    ThreadLocal<FilterContext> contextThreadLocal;

    protected Filter nextFilter;

    protected final int order;

    public BaseFilter(int order) {

        if (order < 0) {
            throw new IllegalArgumentException("The param 'order' must more than zero");
        }

        this.order = order;
        contextThreadLocal = new ThreadLocal<>();
    }

    @Override
    public void setFilterContext(FilterContext context) {
        contextThreadLocal.remove();
        contextThreadLocal.set(context);
    }

    @Override
    public FilterContext getFilterContext() {
        return contextThreadLocal.get();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void nextFilter(Filter filter) {
        this.nextFilter = filter;
    }

    @Override
    public void doNextFilter(Participant participant, String productId) {
        getFilterContext().addFilterPassed(this);
        if (nextFilter != null) {
            nextFilter.doFilter(participant, productId);
        }
    }

    @Override
    public void clear() {
        contextThreadLocal.remove();
    }
}
