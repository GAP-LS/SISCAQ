package com.suchorski.siscaq.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Planning;
import com.suchorski.siscaq.services.PlanningService;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;

@WebServlet("/planejamento")
public class PlanningServlet extends HttpServlet {

	private static final long serialVersionUID = 6161555576759475141L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			long id = ParamUtils.parseLong(request, "set", 0);
			ParamUtils.checkLongs(id);
			Planning planning = null;
			try {
				planning = PlanningService.getById(id);
			} catch (DatabaseException e) {
				planning = PlanningService.getLast();
			}
			request.getSession().setAttribute("planning", planning);
			response.sendRedirect("./processos");
		} catch (Exception e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

}
