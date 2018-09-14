package com.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.domain.User;

public class RequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();

		String key = request.getParameter("key");
		if (key != null && "ABC123XYZ".equals(key)) { // 免登陆
			chain.doFilter(request, response);
			return;
		}
		
		// 获得用户请求的URI
		String path = servletRequest.getRequestURI();

		// 从session中获取用户
		User user = (User) session.getAttribute(session.getId());

		// 登陆页面无需过滤
		if (path.indexOf("/login") > -1) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		if (user == null) {
			// 未登录跳转到登陆页面
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} else {
			// 已经登陆,继续此次请求
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}