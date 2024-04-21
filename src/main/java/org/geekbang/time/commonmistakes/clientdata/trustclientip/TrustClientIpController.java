package org.geekbang.time.commonmistakes.clientdata.trustclientip;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Slf4j
@RequestMapping("trustclientip")
@RestController
public class TrustClientIpController {

    HashSet<String> activityLimit = new HashSet<>();

    @GetMapping("test")
    public String test(HttpServletRequest request) {

        String clientIP = getClientIP();
        return clientIP;

        //String ip = getClientIp(request);
        //if (activityLimit.contains(ip)) {
        //    return "您已经领取过奖品";
        //} else {
        //    activityLimit.add(ip);
        //    return "奖品领取成功";
        //}
    }

    //private String getClientIp(HttpServletRequest request) {
    //    String xff = request.getHeader("X-Forwarded-For");
    //    if (xff == null) {
    //        return request.getRemoteAddr();
    //    } else {
    //        return xff.contains(",") ? xff.split(",")[0] : xff;
    //    }
    //}

    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return ServletUtil.getClientIP(request);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
