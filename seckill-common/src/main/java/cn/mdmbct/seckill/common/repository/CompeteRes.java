package cn.mdmbct.seckill.common.repository;

import cn.mdmbct.seckill.common.lock.HoldLockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 从仓库中竞争商品、奖品的结果
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 19:17
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@ToString
@RequiredArgsConstructor
public class CompeteRes {

    /**
     * 线程获取锁的状态
     */
    private final HoldLockState holdLockState;

    /**
     * 当前商品剩余数量
     * 如果是{@link Integer.MIN_VALUE}的话 则代表无法获取当前剩余多少
     */
    private final int count;

    public CompeteRes(HoldLockState holdLockState) {
        this.holdLockState = holdLockState;
        this.count = Integer.MIN_VALUE;
    }
}
