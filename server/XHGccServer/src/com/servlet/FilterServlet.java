package com.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.domain.User;

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
		
		HttpServletRequest request = ((HttpServletRequest)req);
		String servletPah = request.getServletPath();
		
		if (servletPah == null) return;
		
		if (!servletPah.equals("/crossdomain.xml") && !"/login".equals(servletPah)) {
			//IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
			
			User user = (User) request.getSession().getAttribute(request.getSession().getId());
			if (user == null) return;
			
			// 验证权限
			/*List<Integer> authorityIDList = userService.getAuthorityIDListByRoleID(user.getRoleID());
			Integer optType = Integer.parseInt(req.getParameter("optType"));
			if (!authorityIDList.contains(optType)) return;*/
			
		}
		chain.doFilter(req, resp);
	}

	
	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
}
