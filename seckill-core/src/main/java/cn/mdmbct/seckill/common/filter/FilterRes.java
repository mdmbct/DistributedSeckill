package cn.mdmbct.seckill.common.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤结果
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 15:01
 * @modified mdmbct
 * @since 0.1
 */
public class FilterRes {

    @Getter
    private List<Filter> filtersPassed;

    @Setter
    @Getter
    private Filter filterNotPassed;

    public FilterRes() {
        this.filtersPassed = new ArrayList<>();
    }

    public void addFilterPassed(Filter filter) {
        filtersPassed.add(filter);
    }

}
