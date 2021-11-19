package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.HoldLockState;
import cn.mdmbct.seckill.common.lock.ProductLock;
import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.repository.Product;
import cn.mdmbct.seckill.common.repository.ProductsRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 21:46
 * @modified mdmbct
 * @since 0.1
 */
public class MapGoodsRepository implements ProductsRepository {

    private final Map<String, Product> goodsCache;

    private final ProductLock lock;

    // 加不加读写锁效果没什么区别 按理来说是不需要读写锁的 因为某线程更新商品信息前必须拥有该商品的锁
//    private final ReentrantReadWriteLock readWriteLock;

    public MapGoodsRepository(ProductLock lock, Seckill seckill) {
        this.lock = lock;
//        this.readWriteLock = new ReentrantReadWriteLock(true);
        this.goodsCache = new HashMap<>(seckill.getGoods().size());
        seckill.getGoods().forEach(goods -> goodsCache.put(goods.getId(), goods));
    }

//    private Goods get(String id) {
//        readWriteLock.readLock().lock();
//        try {
//            return goodsCache.get(id);
//        } finally {
//            readWriteLock.readLock().unlock();
//        }
//    }

    @Override
    public CompeteRes incrOne(String id) {
        if (lock.tryLock(id)) {
            try {
                return new CompeteRes(HoldLockState.GET, goodsCache.get(id).incrOne());
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteRes(HoldLockState.MISS);
    }

    @Override
    public CompeteRes decrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                return new CompeteRes(HoldLockState.GET, goodsCache.get(id).decrOne());
//                final Goods goods = get(id);
//                readWriteLock.writeLock().lock();
//                try {
//                    goods.decrOne();
//                } finally {
//                    readWriteLock.writeLock().unlock();
//                }
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteRes(HoldLockState.MISS);
    }

    @Override
    public CompeteRes updateCount(String id, int newCount) {
        if (lock.tryLock(id)) {
            try {
                goodsCache.get(id).update(newCount);
                return new CompeteRes(HoldLockState.GET, newCount);
            } catch (Exception e) {
                e.printStackTrace();
                return new CompeteRes(HoldLockState.EXCEPTION);
            } finally {
                lock.unLock(id);
            }
        }
        return new CompeteRes(HoldLockState.MISS);
    }
}
