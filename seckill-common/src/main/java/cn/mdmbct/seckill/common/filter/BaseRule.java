package cn.mdmbct.seckill.common.filter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/20 0:07
 * @modified mdmbct
 * @since 0.1
 */
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class BaseRule implements Rule {

    protected final int order;

    protected String breakMsg;


    @Override
    public String getBreakMsg() {
        return breakMsg;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
