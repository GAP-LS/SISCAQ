package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.services.NoteService;
import com.suchorski.siscaq.services.ProcessService;
import com.suchorski.siscaq.services.RegressService;
import com.suchorski.siscaq.services.StatusService;
import com.suchorski.siscaq.servlets.helpers.PlanningHelper;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;

@WebServlet("/processo")
public class ProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 5697772022782991168L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PlanningHelper.initialize(request);
			long id = ParamUtils.parseLong(request, "id", 0);
			request.setAttribute("process", ProcessService.getById(id));
			request.setAttribute("status", StatusService.list());
			request.setAttribute("regresses", RegressService.listByProcessId(id));
			request.setAttribute("notes", NoteService.listByProcessId(id));
			request.setAttribute("template", "process");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		} catch (SQLException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		} catch (DatabaseException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

}
