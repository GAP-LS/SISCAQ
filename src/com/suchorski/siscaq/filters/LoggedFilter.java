package com.suchorski.siscaq.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.models.User;
import com.suchorski.siscaq.services.UserService;
import com.suchorski.siscaq.utils.SISCAQ;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;

@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/*" })
public class LoggedFilter implements Filter {

	private static final String[] NOT_LOGGED_PAGES = {"/js/", "/css/", "/img/", "/fonts/", "/login", "/ajuda", "/informacoes"};

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			if (Arrays.asList(NOT_LOGGED_PAGES).stream().noneMatch(s -> req.getRequestURI().contains(s))) {
				if ((boolean) req.getSession().getAttribute("logged")) {
					User user = (User) req.getSession().getAttribute("user");
					if (user != null) {
						user = UserService.getById(user.getId());
						if (user.getLevel().getLevel() > 0) {
							req.getSession().setAttribute("user", user);
							req.getSession().setAttribute("userName", user.getDisplayName());
							req.setAttribute("isEditor", user.getLevel().getLevel() >= SISCAQ.LEVELS.EDITOR);
							req.setAttribute("isManager", user.getLevel().getLevel() >= SISCAQ.LEVELS.MANAGER);
							req.setAttribute("isDeveloper", user.getLevel().getLevel() >= SISCAQ.LEVELS.DEVELOPER);
							chain.doFilter(request, response);
						} else {
							req.getSession().invalidate();
							MessageUtils.dispatch("Para acessar o sistema, favor solicitar o acesso a um <a href='./informacoes'>administrador</a>.", TYPE.danger, (HttpServletRequest) request, (HttpServletResponse) response);
						}
					} else {
						req.getSession().invalidate();
						((HttpServletResponse) response).sendRedirect("./login");
					}
				} else {
					((HttpServletResponse) response).sendRedirect("./login");
				}
			} else {
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, (HttpServletRequest) request, (HttpServletResponse) response);
		}
	}

	public void destroy() {
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		return;
	}

}
