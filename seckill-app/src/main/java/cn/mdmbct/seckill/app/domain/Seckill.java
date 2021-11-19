package cn.mdmbct.seckill.app.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 被秒杀的商品
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 9:30
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "seckill")
public class Seckill implements Serializable {

    private static final long serialVersionUID = -5894580329502320379L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "count", nullable = false)
    private Integer count;

    public Seckill(Long activityId, String name, Integer count) {
        this.activityId = activityId;
        this.name = name;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Seckill seckill = (Seckill) o;
        return id != null && Objects.equals(id, seckill.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
