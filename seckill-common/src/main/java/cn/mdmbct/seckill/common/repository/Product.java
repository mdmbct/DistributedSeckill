package cn.mdmbct.seckill.common.repository;

import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 产品
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 20:05
 * @modified mdmbct
 * @since 0.1
 */
@Getter
public class Product implements Serializable {

    private static final long serialVersionUID = -93160346279104104L;

    protected final String id;

    protected final AtomicInteger count;

    public Product(String id, int count) {
        this.id = id;
        this.count = new AtomicInteger(count);
    }

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
