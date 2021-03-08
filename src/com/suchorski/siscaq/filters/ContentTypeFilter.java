package com.suchorski.siscaq.filters;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/*" })
public class ContentTypeFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		((HttpServletResponse) response).setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		((HttpServletResponse) response).setHeader("Pragma", "no-cache");
		chain.doFilter(request, response);
	}
	
	public void destroy() {
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		return;
	}

}
