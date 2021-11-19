package cn.mdmbct.seckill.common.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper分布式锁
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 20:21
 * @modified mdmbct
 * @since 0.1
 */
public class ZkDistributeLock implements ProductLock {

    private int baseSleepTimeMs = 1000;

    private int maxRetries = 3;

    private long lockWaitTime = 3;

    private TimeUnit lockWaitTimeTimeUnit = TimeUnit.SECONDS;

    private String address = "localhost:2181";

    private CuratorFramework client;

    private final String lockNode;

    private final Map<String, InterProcessMutex> mutexMap;


    /**
     * 默认 <br>
     * baseSleepTimeMs = 1000 <br>
     * maxRetries = 3 <br>
     * lockWaitTime = 3s <br>
     * address = "localhost:2181"
     *
     * @param lockNode zk分布式锁节点，比如：“/curator/lock/seckill“，注意节点最后不能有”/“
     */
    public ZkDistributeLock(String lockNode, List<String> productIds) {
        this.lockNode = lockNode;
        this.mutexMap = new HashMap<>(productIds.size());
        init(productIds);
    }

    public ZkDistributeLock(String lockNode,
                            int baseSleepTimeMs,
                            int maxRetries,
                            String address,
                            long lockWaitTime,
                            TimeUnit lockWaitTimeTimeUnit,
                            List<String> productIds) {
        this.lockNode = lockNode;
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxRetries = maxRetries;
        this.address = address;
        this.lockWaitTime = lockWaitTime;
        this.lockWaitTimeTimeUnit = lockWaitTimeTimeUnit;
        this.mutexMap = new HashMap<>(productIds.size());
        init(productIds);
    }

    private void init(List<String> productIds) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.newClient(address, retryPolicy);
        client.start();
        productIds.forEach(productId -> {
            mutexMap.put(productId, new InterProcessMutex(client, lockNode + "/" + productId));
        });
    }


    @Override
    public boolean tryLock(String id) {

        try {
            return mutexMap.get(id).acquire(lockWaitTime, lockWaitTimeTimeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unLock(String id) {

        try {
            mutexMap.get(id).release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
