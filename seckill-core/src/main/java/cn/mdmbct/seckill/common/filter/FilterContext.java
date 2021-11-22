package cn.mdmbct.seckill.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器数据上下文 贯穿线程经过的过滤器链
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 17:29
 * @modified mdmbct
 * @since 0.1
 */
@ToString
public class FilterContext {

    @Getter
    private final Thread thread;

    @Getter
    private final List<Filter> filtersPassed;

    @Setter
    @Getter
    private Filter filterNotPassed;

    public FilterContext(Thread thread) {
        this.thread = thread;
        this.filtersPassed = new ArrayList<>();
    }

    public void addFilterPassed(Filter filter) {
        filtersPassed.add(filter);
    }

    public String getVisualInfo() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n")
                .append("ThreadId: ").append(thread.getId()).append("\n")
                .append("ThreadName: ").append(thread.getName()).append("\n")
                .append("FilterNotPassed: ");

        if (filterNotPassed != null) {
            sb.append(filterNotPassed.getClass()).append("\n");
        } else {
            sb.append("\n");
        }

        sb.append("FiltersPassed: ");
        if (filtersPassed.size() != 0) {
            for (Filter filter : filtersPassed) {
                sb.append(filter.getClass()).append(" ");
            }

        }
        sb.append("\n");


        return sb.toString();
    }
}
