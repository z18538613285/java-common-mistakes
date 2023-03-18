package org.geekbang.time.commonmistakes.concurrenttool.concurrenthashmapperformance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("concurrenthashmapperformance")
@Slf4j
public class ConcurrentHashMapPerformanceController {

    private static int LOOP_COUNT = 10000000;
    private static int THREAD_COUNT = 10;
    private static int ITEM_COUNT = 10;

    @GetMapping("good")
    public String good() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normaluse");
        Map<String, Long> normaluse = normaluse();
        stopWatch.stop();
        Assert.isTrue(normaluse.size() == ITEM_COUNT, "normaluse size error");
        Assert.isTrue(normaluse.entrySet().stream()
                        .mapToLong(item -> item.getValue()).reduce(0, Long::sum) == LOOP_COUNT
                , "normaluse count error");
        stopWatch.start("gooduse");
        Map<String, Long> gooduse = gooduse();
        stopWatch.stop();
        Assert.isTrue(gooduse.size() == ITEM_COUNT, "gooduse size error");
        Assert.isTrue(gooduse.entrySet().stream()
                        .mapToLong(item -> item.getValue())
                        .reduce(0, Long::sum) == LOOP_COUNT
                , "gooduse count error");
        log.info(stopWatch.prettyPrint());
        return "OK";
    }

    /**
     *这段代码在功能上没有问题，但无法充分发挥
     * ConcurrentHashMap 的威力
     * @return Map<Long>
     */

    private Map<String, Long> normaluse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    synchronized (freqs) {
                        if (freqs.containsKey(key)) {
                            freqs.put(key, freqs.get(key) + 1);
                        } else {
                            freqs.put(key, 1L);
                        }
                    }
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    /**
     *使用 ConcurrentHashMap 的原子性方法 computeIfAbsent 来做复合逻辑操作，判断
     * Key 是否存在 Value，如果不存在则把 Lambda 表达式运行后的结果放入 Map 作为
     * Value，也就是新创建一个 LongAdder 对象，最后返回 Value。
     * 由于 computeIfAbsent 方法返回的 Value 是 LongAdder，是一个线程安全的累加器，
     * 因此可以直接调用其 increment 方法进行累加。
     *
     * AtomicLong实际是多个线程竞争修改这个value属性的机会
     * LongAdder就是第二种操作思路，将原AtomicLong里的value拆成多个value，这样在并发情况下，
     * 就将多个线程竞争修改【一个】value属性的机会 --> 多个线程竞争修改【多个】value属性的机会，这样就相当于将性能翻倍。
     * @return Map<Long>
     */

    private Map<String, Long> gooduse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    freqs.computeIfAbsent(key, k -> new LongAdder()).increment();
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().longValue())
                );
    }

    /**
     *优化后的代码，相比使用锁来操作 ConcurrentHashMap 的方式，性能提升
     * 了 10 倍。
     * 你可能会问，computeIfAbsent 为什么如此高效呢？
     * 答案就在源码最核心的部分，也就是 Java 自带的 Unsafe 实现的 CAS。它在虚拟机层面确
     * 保了写入数据的原子性，比加锁的效率高得多：
     *
     *
     * computeIfAbsent 和 putIfAbsent 区别有以下三点：
     *
     * 当key 存在的时候，如果value获取比较昂贵的话，putifAbsent 就白白浪费时间在获取这个昂贵的value上。
     * key 不存在的时候，putIfAbsent 返回null, 小心空指针，而computeIfAbsent 返回计算后的值。
     * 当key 不存在的时候，putifAbsent 允许put null 进去，而 computeIfAbsent 不能，之后进行containsKey查询是有区别的。
     */

}
