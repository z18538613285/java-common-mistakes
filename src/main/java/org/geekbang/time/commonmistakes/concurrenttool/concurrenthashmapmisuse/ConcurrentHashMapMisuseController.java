package org.geekbang.time.commonmistakes.concurrenttool.concurrenthashmapmisuse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 *
 * ConcurrentHashMap 只能保证提供的原子
 * 性读写操作是线程安全的。
 *
 *
 */

@RestController
@RequestMapping("concurrenthashmapmisuse")
@Slf4j
public class ConcurrentHashMapMisuseController {

    private static int THREAD_COUNT = 10;
    private static int ITEM_COUNT = 1000;

    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }

    /**
     *ConcurrentHashMap 对外提供的方法或能力的限制
     * 使用了 ConcurrentHashMap，不代表对它的多个操作之间的状态是一致的，是没有其
     * 他线程在操作它的，如果需要确保需要手动加锁。
     *
     * 诸如 size、isEmpty 和 containsValue 等聚合方法，在并发情况下可能会反映
     * ConcurrentHashMap 的中间状态。因此在并发情况下，这些方法的返回值只能用作参
     * 考，而不能用于流程控制。显然，利用 size 方法计算差异值，是一个流程控制。
     * 诸如 putAll 这样的聚合方法也不能确保原子性，在 putAll 的过程中去获取数据可能会
     * 获取到部分数据
     * @return String
     */

    @GetMapping("wrong")
    public String wrong() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        log.info("init size:{}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            int gap = ITEM_COUNT - concurrentHashMap.size();
            log.info("gap size:{}", gap);
            concurrentHashMap.putAll(getData(gap));
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }

    /**
     *代码的修改方案很简单，整段逻辑加锁即可：
     * @return String
     */

    @GetMapping("right")
    public String right() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        log.info("init size:{}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            synchronized (concurrentHashMap) {
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap size:{}", gap);
                concurrentHashMap.putAll(getData(gap));
            }
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }
    /**
     *到了这里，你可能又要问了，使用 ConcurrentHashMap 全程加锁，还不如使用普通的
     * HashMap 呢。
     * 其实不完全是这样。
     * ConcurrentHashMap 提供了一些原子性的简单复合逻辑方法，用好这些方法就可以发挥
     * 其威力。这就引申出代码中常见的另一个问题：在使用一些类库提供的高级工具类时，开发
     * 人员可能还是按照旧的方式去使用这些新类，因为没有使用其特性，所以无法发挥其威力。
     */

}
