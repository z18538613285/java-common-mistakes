package org.geekbang.time.commonmistakes.dataandcode.xss;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        //获取多个参数值的时候对所有参数值应用clean方法逐一清洁
        return Arrays.stream(super.getParameterValues(parameter)).map(this::clean).toArray(String[]::new);
    }

    @Override
    public String getHeader(String name) {
        //同样清洁请求头
        return clean(super.getHeader(name));
    }

    @Override
    public String getParameter(String parameter) {
        //获取参数单一值也要处理
        return clean(super.getParameter(parameter));
    }
    //clean方法就是对值进行HTML转义
    private String clean(String value) {
        return StringUtils.isEmpty(value) ? "" : HtmlUtils.htmlEscape(value);
    }
}
