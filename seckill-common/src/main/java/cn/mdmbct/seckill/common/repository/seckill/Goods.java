package cn.mdmbct.seckill.common.repository.seckill;

import lombok.*;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 商品
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 20:17
 * @modified mdmbct
 * @since 0.1
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Goods implements Serializable {

    private static final long serialVersionUID = -3462725598723343681L;

    public Goods(String id, int count) {
        this.id = id;
        this.count = new AtomicInteger(count);
    }

    private final String id;

    private final AtomicInteger count;

    /**
     * 增加1并返回新值
     * @return 新的数量
     */
    public int incrOne() {
        return count.incrementAndGet();
    }

    /**
     * 减少1并返回新值
     * @return 新的数量
     */
    public int decrOne() {
        return count.decrementAndGet();
    }

    public void update(int newCount) {
        count.getAndSet(newCount);
    }





}
