package org.geekbang.time.commonmistakes.productionready.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolHealthIndicator implements HealthIndicator {
    private ThreadPoolExecutor threadPool;

    public ThreadPoolHealthIndicator(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public Health health() {
        //补充信息
        Map<String, Integer> detail = new HashMap<>();
        //队列当前元素个数
        detail.put("queue_size", threadPool.getQueue().size());
        //队列剩余容量
        detail.put("queue_remaining", threadPool.getQueue().remainingCapacity());
        //如果还有剩余量则返回UP，否则返回DOWN
        if (threadPool.getQueue().remainingCapacity() > 0) {
            return Health.up().withDetails(detail).build();
        } else {
            return Health.down().withDetails(detail).build();
        }
    }
}
