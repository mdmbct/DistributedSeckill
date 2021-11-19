package cn.mdmbct.seckill.common.repository.money;

import cn.mdmbct.seckill.common.repository.lottery.Prize;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 抢红包配置
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 22:58
 * @modified mdmbct
 * @since 0.1
 */
@RequiredArgsConstructor
public class GrabRedPacket {

    private final List<RedPacket> packets;

    /**
     * 开始时间
     */
    private final long startTime;
}
