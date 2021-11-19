package cn.mdmbct.seckill.common.redis;

import org.redisson.config.*;

/**
 * 不同{@link RedissonMode}下{@link Config}的创建
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/18 22:15
 * @modified mdmbct
 * @since 0.1
 */
public class RedissonConfigFactory {


    public static Config createRedissonConfig(RedissonProperties properties) {

        switch (properties.getRedissonMode()) {
            case CLUSTER:
                return createClusterRedissonConfig(properties);
            case Sentinel:
                return createSentinelRedissonConfig(properties);
            case MASTER_SLAVE:
                return createMasterSlaveRedissonConfig(properties);
            default:
                return createSingleRedissonConfig(properties);
        }

    }

    /**
     * 创建单机模式redisson配置
     *
     * @param properties jedis配置
     * @return redisson配置
     */
    public static Config createSingleRedissonConfig(RedissonProperties properties) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();

        singleServerConfig.setAddress(properties.getAddress());
        if (properties.getMasterMinIdle() > 0) {
            singleServerConfig.setConnectionMinimumIdleSize(properties.getMasterMinIdle());
        }
        if (properties.getDatabase() >= 0) {
            singleServerConfig.setDatabase(properties.getDatabase());
        }
        setRedissonConfig(singleServerConfig, properties);

        return config;
    }


    /**
     * 创建哨兵模式redisson配置
     *
     * @param properties jedis配置
     * @return redisson配置
     */
    public static Config createSentinelRedissonConfig(RedissonProperties properties) {

        if (properties.getHosts() == null || properties.getHosts().length <= 1) {
            throw new NullPointerException("There must have more than one redis host when the redisson mode is SENTINEL");
        }

        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        sentinelServersConfig.addSentinelAddress(properties.getHosts());
        if (properties.getDatabase() >= 0) {
            sentinelServersConfig.setDatabase(properties.getDatabase());
        }
        setRedissonConfig(sentinelServersConfig, properties);

        return config;
    }

    /**
     * 创建集群模式redisson配置
     *
     * @param properties jedis配置
     * @return redisson配置
     */
    public static Config createClusterRedissonConfig(RedissonProperties properties) {
        Config config = new Config();

        if (properties.getHosts() == null || properties.getHosts().length <= 1) {
            throw new NullPointerException("There must have more than one redis host when the redisson mode is CLUSTER");
        }

        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        if (properties.getScanInterval() > 0) {
            clusterServersConfig.setScanInterval(properties.getScanInterval());
        }

        setRedissonConfig(clusterServersConfig, properties);

        return config;
    }

    /**
     * 创建主从模式redisson配置
     *
     * @param properties jedis配置
     * @return redisson配置
     */
    public static Config createMasterSlaveRedissonConfig(RedissonProperties properties) {
        Config config = new Config();

        if (properties.getHosts().length == 0) {
            throw new IllegalArgumentException("Slave host's number can't be zero when redisson mode is MASTER_SLAVE");
        }

        if (properties.getMasterHost() == null || properties.getMasterHost().trim().length() == 0) {
            throw new NullPointerException("Master host can't be null when redisson mode is MASTER_SLAVE");
        }

        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
                masterSlaveServersConfig.setMasterAddress(properties.getMasterHost())
                .addSlaveAddress(properties.getHosts());

        if (properties.getMasterMinIdle() != 0) {
            masterSlaveServersConfig.setMasterConnectionMinimumIdleSize(properties.getMasterMinIdle());
        }

        if (properties.getSlaveMinIdle() != 0) {
            masterSlaveServersConfig.setSlaveConnectionMinimumIdleSize(properties.getSlaveMinIdle());
        }

        if (properties.getDatabase() >= 0) {
            masterSlaveServersConfig.setDatabase(properties.getDatabase());
        }
        setRedissonConfig(masterSlaveServersConfig, properties);

        return config;
    }

    private static <T extends BaseConfig<T>> void setRedissonConfig(BaseConfig<T> baseConfig, RedissonProperties properties) {

        if (properties.getUsername() != null && properties.getUsername().trim().length() != 0) {
            baseConfig.setUsername(properties.getUsername());
        }

        if (properties.getTimeout() != 0) {
            baseConfig.setConnectTimeout(properties.getTimeout());
        }

        if (properties.getPassword() != null && properties.getPassword().trim().length() != 0) {
            baseConfig.setPassword(properties.getPassword());
        }

    }


}
