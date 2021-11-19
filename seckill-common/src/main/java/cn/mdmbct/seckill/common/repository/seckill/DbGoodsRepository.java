package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.HoldLockState;
import cn.mdmbct.seckill.common.lock.Lock;
import cn.mdmbct.seckill.common.repository.CompeteRes;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 对商品数量修改在数据库中进行
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 21:35
 * @modified mdmbct
 * @since 0.1
 */
@RequiredArgsConstructor
public class DbGoodsRepository implements ProductsRepository {

    private final Lock lock;

    private final Function<String, Integer> dbDecrOneOp;

    private final Function<String, Integer> dbIncrOneOp;

    /**
     * 数组第一个参数为商品id 第二个参数为商品数量
     */
    private final Consumer<Object[]> dbUpdateOp;

    @Override
    public CompeteRes incrOne(String id) {
        if (lock.tryLock(id)) {
            try {
                // 操作数据库
                return new CompeteRes(HoldLockState.GET, dbIncrOneOp.apply(id));
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
                return new CompeteRes(HoldLockState.GET, dbDecrOneOp.apply(id));
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
                // 操作数据库
                dbUpdateOp.accept(new Object[]{id, newCount});
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
