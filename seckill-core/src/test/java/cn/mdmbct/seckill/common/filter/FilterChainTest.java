package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import cn.mdmbct.seckill.common.lock.ReentrantLock;
import cn.mdmbct.seckill.common.repository.Product;
import cn.mdmbct.seckill.common.repository.seckill.Seckill;
import cn.mdmbct.seckill.common.repository.seckill.SingleGoodsRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FilterChainTest {

    private FilterChain filterChain;

    @Before
    public void setUp() throws Exception {

        List<Filter> filters = new ArrayList<>();
        filters.add(new TokenBucketLimitingFilter(
                90,
                100,
                null
        ));
        filters.add(new CompeteLockFilter(new ReentrantLock()));
        final Seckill seckill = new Seckill("1",
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

        filters.add(new AwardFilter(new SingleGoodsRepository(seckill)));

        this.filterChain = new FilterChain(filters);

    }

    @Test
    public void doFilter() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000));


        int killNum = 1000;
        final CountDownLatch latch = new CountDownLatch(killNum);
        for (int i = 0; i < killNum; i++) {
            long userId = i;
            final Runnable task = () -> {
                filterChain.doFilter(new Participant(userId + ""), "1");
                System.out.println("用户id: " + userId + filterChain.getFilterContext().getVisualInfo());
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}