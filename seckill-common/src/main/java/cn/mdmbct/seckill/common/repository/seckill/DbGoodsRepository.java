package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.HoldLockState;
import cn.mdmbct.seckill.common.lock.Lock;
import cn.mdmbct.seckill.common.repository.CompeteRes;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import lombok.Getter;
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

    /**
     * 数量加1 并返回新的数量
     * 输入: 产品id
     * 输出: 产品新的数量
     */
    private final Function<String, Integer> dbDecrOneOp;

    /**
     * 数量减1 并返回新的数量
     * 输入: 产品id
     * 输出: 产品新的数量
     */
    private final Function<String, Integer> dbIncrOneOp;

    private final Consumer<CountUpdateParams> dbUpdateOp;

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
                dbUpdateOp.accept(new CountUpdateParams(id, newCount));
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

    @Getter
    @RequiredArgsConstructor
    public static class CountUpdateParams {

        /**
         * 产品id
         */
        private final String id;

        /**
         * 新的数量
         */
        private final int newCount;
    }
}
