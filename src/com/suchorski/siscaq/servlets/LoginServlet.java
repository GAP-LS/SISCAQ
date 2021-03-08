package com.suchorski.siscaq.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.models.User;
import com.suchorski.siscaq.services.PlanningService;
import com.suchorski.siscaq.services.UserService;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
       
	private static final long serialVersionUID = 117223261874984789L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		request.setAttribute("template", "login");
		request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String cpf = ParamUtils.parseString(request, "cpf", "");
			String password = ParamUtils.parseString(request, "password", "");
			User user = UserService.login(cpf, password);
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("logged", true);
			request.getSession().setAttribute("planning", PlanningService.getLast());
			response.sendRedirect("./index");
		} catch (Exception e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

}
