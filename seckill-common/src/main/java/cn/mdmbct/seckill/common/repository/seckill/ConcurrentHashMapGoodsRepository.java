package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.CompeteResult;
import cn.mdmbct.seckill.common.lock.Lock;
import cn.mdmbct.seckill.common.repository.ProductsRepository;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 21:46
 * @modified mdmbct
 * @since 1.0
 */
public class ConcurrentHashMapGoodsRepository implements ProductsRepository {

    private final ConcurrentHashMap<String, Integer> goodsRepository;

    private final Lock lock;


    public ConcurrentHashMapGoodsRepository(Lock lock, Seckill seckill) {
        this.lock = lock;
        goodsRepository = new ConcurrentHashMap<>(seckill.getGoods().size());
        seckill.getGoods().forEach(goods -> goodsRepository.put(goods.getId(), goods.getCount().intValue()));
    }

    @Override
    public CompeteResult incrOne(String id) {
        return null;
    }

    @Override
    public CompeteResult decrOne(String id) {
        return null;
    }

    @Override
    public CompeteResult updateCount(String id, int count) {
        return null;
    }
}
