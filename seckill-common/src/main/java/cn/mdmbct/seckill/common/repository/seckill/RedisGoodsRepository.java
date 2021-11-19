package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.CompeteResult;
import cn.mdmbct.seckill.common.lock.Lock;
import cn.mdmbct.seckill.common.redis.JedisRedisOps;
import cn.mdmbct.seckill.common.redis.RedisOps;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import redis.clients.jedis.JedisPool;

/**
 * 对商品数量的修改直接在Redis中进行
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 19:56
 * @modified mdmbct
 * @since 0.1
 */
public class RedisGoodsRepository implements ProductsRepository {

    private final RedisOps redisOps;

    private final String goodsCachePrefix;

    private final Lock lock;

    public RedisGoodsRepository(JedisPool jedisPool, Lock lock, Seckill seckill, String goodsCachePrefix) {
        this.redisOps = new JedisRedisOps(jedisPool);
        this.goodsCachePrefix = goodsCachePrefix;
        this.lock = lock;
        init(seckill);
    }

    public RedisGoodsRepository(RedisOps redisOps, Lock lock, Seckill seckill, String goodsCachePrefix) {
        this.redisOps = redisOps;
        this.goodsCachePrefix = goodsCachePrefix;
        this.lock = lock;
        init(seckill);
    }


    void init(Seckill seckill) {

        // 商品转Redis
        seckill.getGoods().forEach(goods1 -> redisOps.set(
                goodsCacheKey(goods1.getId()),
                goods1.getCount().toString(),
                seckill.getDuration(),
                seckill.getTimeUnit())
        );

    }


    @Override
    public CompeteResult incrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                redisOps.incr(goodsCacheKey(id));
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
            return CompeteResult.LUCKY;
        }
        return CompeteResult.UNLUCKY;
    }

    @Override
    public CompeteResult decrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                redisOps.decr(goodsCacheKey(id));
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
            return CompeteResult.LUCKY;
        }

        return CompeteResult.UNLUCKY;
    }

    @Override
    public CompeteResult updateCount(String id, int count) {

        if (lock.tryLock(id)) {
            try {
                redisOps.set(goodsCacheKey(id), String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
            return CompeteResult.LUCKY;
        }
        return CompeteResult.UNLUCKY;
    }


    private String goodsCacheKey(String goodId) {
        return goodsCachePrefix + goodId;
    }


}
