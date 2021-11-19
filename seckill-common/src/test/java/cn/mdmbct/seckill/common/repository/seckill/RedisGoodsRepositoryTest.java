package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.ReentrantLock;
import cn.mdmbct.seckill.common.lock.ZkDistributeLock;
import cn.mdmbct.seckill.common.redis.JedisProperties;
import cn.mdmbct.seckill.common.repository.CompeteRes;
import cn.mdmbct.seckill.common.repository.Product;
import cn.mdmbct.seckill.common.repository.ProductsRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RedisGoodsRepositoryTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisGoodsRepositoryTest.class);

    private ProductsRepository repository;

    @Before
    public void setUp() throws Exception {

        final JedisProperties jedisProperties = new JedisProperties();
        jedisProperties.setMaxIdle(-1);

        final JedisPool jedisPool = JedisProperties.getJedisPool(jedisProperties);


//        final RedissonDistributeLock lock = new RedissonDistributeLock(Redisson.create(RedissonConfigFactory.createRedissonConfig(new RedissonProperties())),
//                3,
//                20,
//                TimeUnit.SECONDS,
//                "seckill:test_lock_"
//        );
//        final ReentrantLock lock = new ReentrantLock(3, TimeUnit.SECONDS);

        final ZkDistributeLock lock = new ZkDistributeLock("/curator/lock/seckill", Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

        final Seckill seckill = new Seckill(
                Arrays.asList(
                        new Product("1", 1001),
                        new Product("2", 1001),
                        new Product("3", 1001),
                        new Product("4", 1001),
                        new Product("5", 1001),
                        new Product("6", 1001),
                        new Product("7", 1001),
                        new Product("8", 1001),
                        new Product("9", 1001),
                        new Product("10", 100)
                ),
                1,
                TimeUnit.MINUTES,
                System.currentTimeMillis()
        );


        this.repository = new RedisGoodsRepository(
                jedisPool,
                lock,
                seckill,
                "seckill:test_Product_"
        );
//        this.repository = new LocalProductRepository(lock, seckill);
    }

    @Test
    public void incrOne() {

    }

    @Test
    public void decrOne() throws NoSuchFieldException, IllegalAccessException {

        int corePoolSize = Runtime.getRuntime().availableProcessors();
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000));


        int killNum = 1000;
        final CountDownLatch latch = new CountDownLatch(killNum);
        for (int i = 0; i < killNum; i++) {
            long userId = i;
            final Runnable task = () -> {
                final CompeteRes competeResult = repository.decrOne("1");
//                LOGGER.info("用户id: " + userId + competeResult);
                System.out.println("用户id: " + userId + competeResult);
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        getLocalRepositoryProduct((LocalProductRepository) repository);

    }

    @Test
    public void testDecrOneTimes() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        int corePoolSize = Runtime.getRuntime().availableProcessors();

        for (int j = 1; j < 11; j++) {
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1000));

            int killNum = 1000;
            final CountDownLatch latch = new CountDownLatch(killNum);
            for (int i = 0; i < killNum; i++) {
                long userId = i;
                int finalJ = j;
                final Runnable task = () -> {
                    final CompeteRes competeResult = repository.decrOne(String.valueOf(finalJ));
//                LOGGER.info("用户id: " + userId + competeResult);
                    System.out.println("用户id: " + userId + competeResult);
                    latch.countDown();
                };
                executor.execute(task);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("--------------------------");
            Thread.sleep(1000);
        }

//        getLocalRepositoryProduct((LocalProductRepository) repository);
    }

    @Test
    public void updateCount() {
    }

    private void getLocalRepositoryProduct(MapGoodsRepository localProductRepository) throws NoSuchFieldException, IllegalAccessException {

        final Field ProductCache = localProductRepository.getClass().getDeclaredField("ProductCache");
        ProductCache.setAccessible(true);
        final Map<String, Product> map = (Map<String, Product>) ProductCache.get(localProductRepository);
        map.forEach((k, v) -> {
            System.out.println(v);
        });
    }
}