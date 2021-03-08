package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.servlets.helpers.PlanningHelper;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;

@WebServlet("/ajuda")
public class HelpServlet extends HttpServlet {

	private static final long serialVersionUID = -6166577348502951895L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PlanningHelper.initialize(request);
			request.setAttribute("template", "help");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		} catch (SQLException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		} catch (DatabaseException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

}
