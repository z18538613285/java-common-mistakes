package org.geekbang.time.commonmistakes.productionready.health;

import jodd.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProvider {
    //一个工作线程的线程池，队列长度10
    private static ThreadPoolExecutor demoThreadPool = new ThreadPoolExecutor(
            1, 1,
            2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").get());
    //核心线程数10，最大线程数50的线程池，队列长度50
    private static ThreadPoolExecutor ioThreadPool = new ThreadPoolExecutor(
            10, 50,
            2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("io-threadpool-%d").get());

    public static ThreadPoolExecutor getDemoThreadPool() {
        return demoThreadPool;
    }

    public static ThreadPoolExecutor getIOThreadPool() {
        return ioThreadPool;
    }
}
