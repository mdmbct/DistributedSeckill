package cn.mdmbct.seckill.common.repository.lottery;

import cn.mdmbct.seckill.common.repository.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 抽奖
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 20:03
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class LotteryDraw {

    private final List<Prize> prizes;

    /**
     * 持续时间
     */
    private final long duration;

    /**
     * 持续时间的单位
     */
    private final TimeUnit timeUnit;

    /**
     * 开始时间
     */
    private final long startTime;
}
