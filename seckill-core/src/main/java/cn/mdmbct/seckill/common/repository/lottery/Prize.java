package cn.mdmbct.seckill.common.repository.lottery;

import cn.mdmbct.seckill.common.repository.Product;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 奖项 比如：一等奖 二等奖 三等奖 ...
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 19:55
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@ToString
public class Prize implements Serializable {

    private static final long serialVersionUID = -3006748485917076001L;

    private final int level;

    private final String name;

    private final double probability;

    private final String id;

    private AtomicInteger count;

    private List<Product> awards;

    /**
     * 奖项自己就作为奖品
     *
     * @param level       几等奖
     * @param name        名字
     * @param probability 概率
     * @param id          id
     * @param count       数量
     */
    public Prize(int level, String name, double probability, String id, int count) {
        this.level = level;
        this.name = name;
        this.probability = probability;
        this.id = id;
        this.count = new AtomicInteger(count);
    }

    /**
     * 奖项下有设有奖品
     *
     * @param level       几等奖
     * @param name        名字
     * @param probability 概率
     * @param id          id
     * @param awards      奖品
     */
    public Prize(int level, String name, double probability, String id, List<Product> awards) {
        this.level = level;
        this.name = name;
        this.probability = probability;
        this.id = id;
        this.awards = awards;
    }
}
