package com.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author ken
 * 2014-3-24
 * 拦截
 */
public class FilterServlet implements Filter {

	@Override
	public void destroy() {
		System.out.println("destroy");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		chain.doFilter(req, resp);
	}

	
	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
}
