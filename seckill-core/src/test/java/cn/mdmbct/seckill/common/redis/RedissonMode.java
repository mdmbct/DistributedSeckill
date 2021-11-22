package cn.mdmbct.seckill.common.redis;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 22:09
 * @modified mdmbct
 * @since 0.1
 */
public enum RedissonMode {


    /**
     * Redis使用单机模式
     */
    SINGLE,

    /**
     * Redis使用集群模式
     */
    CLUSTER,

    /**
     * 主从模式
     */
    MASTER_SLAVE,

    /**
     * 哨兵模式
     */
    Sentinel

}
