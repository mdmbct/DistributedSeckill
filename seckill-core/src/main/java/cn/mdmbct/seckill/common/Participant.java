package cn.mdmbct.seckill.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 参与用户
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 23:24
 * @modified mdmbct
 * @since 0.1
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Participant implements Serializable {

    private static final long serialVersionUID = 5543328376022261131L;

    private final String id;

    /**
     * 参与抽奖的用户名 空 另外设置
     */
    private final String name;

    public Participant(String id) {
        this.id = id;
        this.name = "";
    }
}
