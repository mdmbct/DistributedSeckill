package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.HoldLockState;
import cn.mdmbct.seckill.common.lock.ProductLock;
import cn.mdmbct.seckill.common.redis.JedisRedisOps;
import cn.mdmbct.seckill.common.redis.RedisOps;
import cn.mdmbct.seckill.common.lock.CompeteLockRes;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * 对商品数量的修改直接在Redis中进行 适用于分布式下使用
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:56
 * @modified mdmbct
 * @since 0.1
 */
public class RedisGoodsRepository implements ProductsRepository {

    private final RedisOps redisOps;

    private final String goodsCachePrefix;

    private final ProductLock lock;

    public RedisGoodsRepository(Pool<Jedis> jedisPool, ProductLock lock, Seckill seckill, String goodsCachePrefix) {
        this.redisOps = new JedisRedisOps(jedisPool);
        this.goodsCachePrefix = goodsCachePrefix;
        this.lock = lock;
        init(seckill);
    }

    public RedisGoodsRepository(RedisOps redisOps, ProductLock lock, Seckill seckill, String goodsCachePrefix) {
        this.redisOps = redisOps;
        this.goodsCachePrefix = goodsCachePrefix;
        this.lock = lock;
        init(seckill);
    }


    void init(Seckill seckill) {

        // 商品转Redis
        seckill.getGoods().forEach(goods1 -> redisOps.set(
                goodsCacheKey(goods1.getId()),
                goods1.getRemainCount().toString(),
                seckill.getDuration(),
                seckill.getTimeUnit())
        );

    }


    @Override
    public CompeteLockRes incrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                return new CompeteLockRes(
                        HoldLockState.GET,
                        redisOps.incr(goodsCacheKey(id)).intValue()
                );
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteLockRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteLockRes(HoldLockState.MISS);
    }

    @Override
    public CompeteLockRes decrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                return new CompeteLockRes(
                        HoldLockState.GET,
                        redisOps.decr(goodsCacheKey(id)).intValue()
                );
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteLockRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteLockRes(HoldLockState.MISS);
    }

    @Override
    public CompeteLockRes updateCount(String id, int newCount) {

        if (lock.tryLock(id)) {
            try {
                redisOps.set(goodsCacheKey(id), String.valueOf(newCount));
                return new CompeteLockRes(HoldLockState.GET, newCount);
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteLockRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteLockRes(HoldLockState.MISS);
    }


    private String goodsCacheKey(String goodId) {
        return goodsCachePrefix + goodId;
    }


}
