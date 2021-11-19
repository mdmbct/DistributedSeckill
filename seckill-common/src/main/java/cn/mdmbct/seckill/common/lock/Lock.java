package cn.mdmbct.seckill.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 商品、奖品锁接口
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:27
 * @modified mdmbct
 * @since 0.1
 */
public interface Lock {

    /**
     *
     * @param id 商品、奖品的id
     * @return
     */
    boolean tryLock(String id);

    /**
     * 释放锁
     *
     * @param id 商品、奖品的id
     */
    void unLock(String id);
}
