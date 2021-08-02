package com.changhong.framework.demo;

import javax.servlet.*;
import java.io.IOException;

public class FirstFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(" first filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("enter first filter");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("leave first filter");
    }

    @Override
    public void destroy() {
        System.out.println(" first filter destroy");
    }
}
