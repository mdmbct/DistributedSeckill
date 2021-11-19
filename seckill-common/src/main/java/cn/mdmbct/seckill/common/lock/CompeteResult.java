package cn.mdmbct.seckill.common.lock;

/**
 * 竞争结果
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 21:18
 * @modified mdmbct
 * @since 0.1
 */
public enum CompeteResult {


    /**
     * 竞争到锁
     */
    LUCKY,

    /**
     * 未竞争到锁
     */
    UNLUCKY,

    /**
     * 发生异常
     */
    EXCEPTION,
}
