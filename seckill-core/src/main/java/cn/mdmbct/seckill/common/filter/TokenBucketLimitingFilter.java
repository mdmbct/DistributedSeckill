package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    private final NoTokenParticipantCache cache;

    /**
     * 平滑突发限流
     *
     * @param tokenPerSec 每秒令牌数
     * @param timeout     获取令牌超时时间 超时无法获取到令牌 单位ms
     * @param cache       没有拿到令牌的用户id缓存 <br>
     *                    如果不设为null 则某用户未拿到令牌时会将其放入缓存 <br>
     *                    下次该用户的请求过来 不申请令牌 直接通过 <br>
     *                    如果为空 则忽略上面  <br>
     *                    缓存目前有2个实现类 ：<br>
     *                    {@link SingleNoTokenParticipantCache} <br>
     *                    {@link RedisNoTokenParticipantCache}
     */
    public TokenBucketLimitingFilter(int tokenPerSec,
                                     long timeout,
                                     NoTokenParticipantCache cache) {
        super(FIRST_FILTER_ORDER);
        this.rateLimiter = RateLimiter.create(tokenPerSec);
        this.timeout = timeout;
        this.cache = cache;
    }

    /**
     * 平滑预热限流
     *
     * @param tokenPerSec 每秒令牌数
     * @param warmupTime  预热时间
     * @param unit        预热时间单位
     * @param timeout     获取令牌超时时间 超时无法获取到令牌 单位ms
     * @param cache       没有拿到令牌的用户id缓存 <br>
     *                    如果不设为null 则某用户未拿到令牌时会将其放入缓存 <br>
     *                    下次该用户的请求过来 不申请令牌 直接通过 <br>
     *                    如果为空 则忽略上面  <br>
     *                    缓存目前有2个实现类 ：<br>
     *                    {@link SingleNoTokenParticipantCache} <br>
     *                    {@link RedisNoTokenParticipantCache}
     */
    public TokenBucketLimitingFilter(int tokenPerSec,
                                     int warmupTime,
                                     TimeUnit unit,
                                     long timeout,
                                     NoTokenParticipantCache cache) {
        super(FIRST_FILTER_ORDER);
        this.rateLimiter = RateLimiter.create(tokenPerSec, warmupTime, unit);
        this.timeout = timeout;
        this.cache = cache;
    }


    @Override
    public void doFilter(Participant participant, FilterRes res) {

        if (cache == null) {
            // 缓存为空
            if (!rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                res.setFilterNotPassed(this);
            } else {
                doNextFilter(participant, res, this);
            }
        } else {
            // 缓存不为空
            // 在缓存里直接通过
            if (cache.check(participant.getId())) {
                doNextFilter(participant, res, this);
            } else if (!rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                res.setFilterNotPassed(this);
                // 没拿到令牌 添加到缓存
                cache.add(participant.getId());
            } else {
                doNextFilter(participant, res, this);
            }
        }
    }

    @Override
    public String notPassMsg() {
        return "当前人数过多，请稍后重试";
    }

    private interface NoTokenParticipantCache {

        /**
         * 将没有拿到令牌的人id放入其中
         *
         * @param participantId 用户id
         */
        void add(String participantId);

        /**
         * 检查是否在缓存里
         *
         * @param participantId 用户id
         * @return 是否在缓存里
         */
        boolean check(String participantId);
    }

    /**
     * @author mdmbct mdmbct@outlook.com
     * 分布式下无令牌用户缓存
     */
    public static class RedisNoTokenParticipantCache implements NoTokenParticipantCache {

        @Override
        public void add(String participantId) {

        }

        @Override
        public boolean check(String participantId) {
            return false;
        }
    }

    /**
     * @author mdmbct mdmbct@outlook.com
     * 单机下无令牌用户缓存
     */
    public static class SingleNoTokenParticipantCache implements NoTokenParticipantCache {


        /**
         * 依然是每个key一个锁 且只能单线程读或者写 key:用户id
         */
        private final Map<String, ReentrantLock> lockMap;

        private final Map<String, Object> cache;

        private static final Object V = new Object();

        public SingleNoTokenParticipantCache() {
            this.lockMap = new ConcurrentHashMap<>();
            this.cache = new HashMap<>();
        }

        @Override
        public void add(String participantId) {
            cache.put(participantId, V);
        }

        @Override
        public boolean check(String participantId) {

            final ReentrantLock lock = lockMap.computeIfAbsent(participantId, k -> new ReentrantLock());

            try {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    return cache.remove(participantId) != null;
                }
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } finally {
                lock.unlock();
            }
        }
    }
}
