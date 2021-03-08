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
import javax.servlet.http.HttpServletRequest;

import com.suchorski.siscaq.utils.ServletUtils.ToastUtils;

@WebFilter(dispatcherTypes = {
		DispatcherType.REQUEST, 
		DispatcherType.FORWARD, 
		DispatcherType.INCLUDE, 
		DispatcherType.ERROR
}
, urlPatterns = { "/*" })
public class ToastFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ToastUtils.make((HttpServletRequest) request);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		return;
	}

	public void destroy() {
		return;
	}

}