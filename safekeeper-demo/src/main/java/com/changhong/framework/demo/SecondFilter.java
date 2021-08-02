package com.changhong.framework.demo;

import javax.servlet.*;
import java.io.IOException;

public class SecondFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(" second filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("enter second filter");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("leave second filter");
    }

    @Override
    public void destroy() {
        System.out.println(" second filter destroy");
    }
}
