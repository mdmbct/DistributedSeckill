package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.CompeteRes;
import cn.mdmbct.seckill.common.Participant;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FilterChainTest {

    private FilterChain filterChain;

    @Before
    public void setUp() throws Exception {

        List<Filter> filters = new ArrayList<>();
        filters.add(new TokenBucketLimitingFilter(
                Filter.FIRST_FILTER_ORDER,
                100,
                100,
                null
        ));

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
                filterChain.doFilter(new Participant(userId + ""));
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