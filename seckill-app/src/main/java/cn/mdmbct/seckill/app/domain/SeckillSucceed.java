package cn.mdmbct.seckill.app.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 成功秒杀的商品
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 9:49
 * @modified mdmbct
 * @since 0.1
 */
public class SeckillSucceed {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(name = "seckill_id", nullable = false)
//    private Long seckillId;
//
//
//    private Long userId;
}
