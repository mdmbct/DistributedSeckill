package cn.mdmbct.seckill.common.filter;

/**
 * 破坏规则后抛出异常
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:26
 * @modified mdmbct
 * @since 0.1
 */
public class BreakRuleException extends RuntimeException  {

    private static final long serialVersionUID = 4151951668625008121L;

    public BreakRuleException(String message) {
        super(message);
    }
}
