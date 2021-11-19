package cn.mdmbct.seckill.common.repository;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Product implements Serializable {

    private static final long serialVersionUID = -93160346279104104L;

    protected final String id;

    /**
     * 剩余数量
     */
    protected final AtomicInteger remainCount;

    public Product(String id, int remainCount) {
        this.id = id;
        this.remainCount = new AtomicInteger(remainCount);
    }

    /**
     * 增加1并返回新值
     * @return 新的数量
     */
    public int incrOne() {
        return remainCount.incrementAndGet();
    }

    /**
     * 减少1并返回新值
     * @return 新的数量
     */
    public int decrOne() {
        return remainCount.decrementAndGet();
    }

    public void update(int newCount) {
        remainCount.getAndSet(newCount);
    }
}
