package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.CompeteResult;
import cn.mdmbct.seckill.common.lock.Lock;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

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

    private final Consumer<String> dbDecrOneOp;

    private final Consumer<String> dbIncrOneOp;

    /**
     * 数组第一个参数为商品id 第二个参数为商品数量
     */
    private final Consumer<Object[]> dbUpdateOp;

    @Override
    public CompeteResult incrOne(String id) {
        if (lock.tryLock(id)) {
            try {
                // 操作数据库
                dbIncrOneOp.accept(id);
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
        }
        return CompeteResult.UNLUCKY;
    }

    @Override
    public CompeteResult decrOne(String id) {

        if (lock.tryLock(id)) {
            try {
                // 操作数据库
                dbDecrOneOp.accept(id);
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
        }
        return CompeteResult.UNLUCKY;
    }

    @Override
    public CompeteResult updateCount(String id, int newCount) {
        if (lock.tryLock(id)) {
            try {
                // 操作数据库
                dbUpdateOp.accept(new Object[]{id, newCount});
            } catch (Exception e) {
                e.printStackTrace();
                return CompeteResult.EXCEPTION;
            } finally {
                lock.unLock(id);
            }
        }
        return CompeteResult.UNLUCKY;
    }
}
