package cn.mdmbct.seckill.common.filter;

import java.io.Serializable;

/**
 * 参与最终秒杀等的规则
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:27
 * @modified mdmbct
 * @since 0.1
 */
public interface Rule extends Serializable {

    /**
     * 返回打破该条限制的信息
     * @return 打破该条限制的信息
     */
    String getBreakMsg();

    /**
     * 获取该限制规则生效顺序 越小越先生效
     * @return 该限制规则生效顺序
     */
    int getOrder();

}
