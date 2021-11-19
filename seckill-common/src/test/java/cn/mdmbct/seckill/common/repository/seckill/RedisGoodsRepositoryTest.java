package cn.mdmbct.seckill.common.repository.seckill;

import cn.mdmbct.seckill.common.lock.CompeteResult;
import cn.mdmbct.seckill.common.lock.ReentrantLock;
import cn.mdmbct.seckill.common.redis.JedisProperties;
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
        final ReentrantLock lock = new ReentrantLock(3, TimeUnit.SECONDS);


        final Seckill seckill = new Seckill(
                Arrays.asList(
                        new Goods("1", 1001),
                        new Goods("2", 1001),
                        new Goods("3", 1001),
                        new Goods("4", 1001),
                        new Goods("5", 1001),
                        new Goods("6", 1001),
                        new Goods("7", 1001),
                        new Goods("8", 1001),
                        new Goods("9", 1001),
                        new Goods("10", 1001)
                ),
                1,
                TimeUnit.MINUTES,
                System.currentTimeMillis()
        );


//        this.repository = new RedisGoodsRepository(
//                jedisPool,
//                lock,
//                seckill,
//                "seckill:test_goods_"
//        );
        this.repository = new LocalGoodsRepository(lock, seckill);
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
                final CompeteResult competeResult = repository.decrOne("1");
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

        getLocalRepositoryGoods((LocalGoodsRepository) repository);

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
                    final CompeteResult competeResult = repository.decrOne(String.valueOf(finalJ));
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

        getLocalRepositoryGoods((LocalGoodsRepository) repository);
    }

    @Test
    public void updateCount() {
    }

    private void getLocalRepositoryGoods(LocalGoodsRepository localGoodsRepository) throws NoSuchFieldException, IllegalAccessException {

        final Field goodsCache = localGoodsRepository.getClass().getDeclaredField("goodsCache");
        goodsCache.setAccessible(true);
        final Map<String, Goods> map = (Map<String, Goods>) goodsCache.get(localGoodsRepository);
        map.forEach((k, v) -> {
            System.out.println(v);
        });
    }
}