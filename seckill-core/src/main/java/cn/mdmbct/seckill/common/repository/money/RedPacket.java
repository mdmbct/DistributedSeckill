package cn.mdmbct.seckill.common.repository.money;

import cn.mdmbct.seckill.common.repository.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 红包
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 22:49
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class RedPacket extends Product implements Serializable {

    private static final long serialVersionUID = 3931744600494223642L;

    /**
     * 总金额 单位：分
     */
    private final int totalAmount;

    /**
     * 红包个数
     */
    private final int totalPackets;

    public RedPacket(String id,int totalAmount, int totalPackets) {
        super(id, totalPackets);
        this.totalAmount = totalAmount;
        this.totalPackets = totalPackets;
    }
}
