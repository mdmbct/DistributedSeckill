package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * 限流过滤器 使用令牌桶 <br>
 * 该限流对引用该模块的微服务有效 即是单机的限流 默认为第一个过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 8:57
 * @modified mdmbct
 * @since 0.1
 */
public class TokenBucketLimitingFilter extends BaseFilter {

    private final RateLimiter rateLimiter;

    /**
     * 获取抽奖令牌超时时间 超时无法获取到令牌 单位ms
     */
    private final long timeout;

    public TokenBucketLimitingFilter(int tokenPerSec, long timeout) {
        super(FIRST_FILTER_ORDER);
        this.rateLimiter = RateLimiter.create(tokenPerSec);
        this.timeout = timeout;
    }

    public TokenBucketLimitingFilter(int tokenPerSec, int warmupTime, TimeUnit unit, long timeout) {
        super(FIRST_FILTER_ORDER);
        this.rateLimiter = RateLimiter.create(tokenPerSec, warmupTime, unit);
        this.timeout = timeout;
    }


    @Override
    public void doFilter(Participant participant, FilterRes res) {
        if (!rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
            res.setFilterNotPassed(this);
        }
        doNextFilter(participant, res, this);
    }

    @Override
    public void clear() {

    }

    @Override
    public String notPassMsg() {
        return "当前人数过多，请稍后重试";
    }
}
