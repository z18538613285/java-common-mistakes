package org.geekbang.time.commonmistakes.concurrenttool.threadlocal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("threadlocal")
public class ThreadLocalMisuseController {

    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 线程池会重用固定的几个线程，一旦线程重用，那么很可能首次从
     * ThreadLocal 获取的值是之前其他用户的请求遗留的值。这时，ThreadLocal 中的用户信
     * 息就是其他用户的信息。
     *
     * 在 Tomcat 这种 Web 服务器下跑的业务代码，本来就运行在一个多线
     * 程环境（否则接口也不可能支持这么高的并发），并不能认为没有显式开启多线程就不
     * 会有线程安全问题。
     * @param userId
     * @return Map
     */

    /**
     * 使用类似 ThreadLocal 工具来存放一些数据时，需要特别注意在
     * 代码运行完后，显式地去清空设置的数据。
     * @param userId
     * @return Map
     */

    @GetMapping("wrong")
    public Map wrong(@RequestParam("userId") Integer userId) {
        String before  = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        String after  = Thread.currentThread().getName() + ":" + currentUser.get();
        Map result = new HashMap();
        result.put("before", before);
        result.put("after", after);
        return result;
    }

    @GetMapping("right")
    public Map right(@RequestParam("userId") Integer userId) {
        String before  = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            Map result = new HashMap();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            currentUser.remove();
        }
    }
}
