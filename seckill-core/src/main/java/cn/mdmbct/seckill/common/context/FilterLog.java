package cn.mdmbct.seckill.common.context;

import cn.mdmbct.seckill.common.filter.Filter;
import lombok.Getter;
import lombok.Setter;

/**
 * 线程通过过滤器的信息日志
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 9:22
 * @modified mdmbct
 * @since 0.1
 */

@Getter
public class FilterLog {

    private final Filter filter;

    @Setter
    private long start;

    @Setter
    private long end;

    public FilterLog(Filter filter) {
        this.filter = filter;
    }
    
}
