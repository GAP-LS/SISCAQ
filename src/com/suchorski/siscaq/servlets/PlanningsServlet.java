package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.services.PlanningService;
import com.suchorski.siscaq.servlets.helpers.PlanningHelper;
import com.suchorski.siscaq.utils.SISCAQ;
import com.suchorski.siscaq.utils.ServletUtils.AccessUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;
import com.suchorski.siscaq.utils.ServletUtils.ToastUtils;

@WebServlet("/planejamentos")
public class PlanningsServlet extends HttpServlet {

	private static final long serialVersionUID = 8897490365024385865L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, SISCAQ.LEVELS.MANAGER, SISCAQ.LEVELS.MANAGER_NAME)) {
			try {
				try {
					PlanningHelper.initialize(request);
				} catch (DatabaseException e) {
					request.setAttribute("message", e.getLocalizedMessage());
				}
				request.setAttribute("template", "plannings");
				request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
			} catch (SQLException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, SISCAQ.LEVELS.MANAGER, SISCAQ.LEVELS.MANAGER_NAME)) {
			try {
				String action = ParamUtils.parseString(request, "action", "");
				long idPlanning = ParamUtils.parseLong(request, "id_planning", 0);
				String title = ParamUtils.parseString(request, "title", "");
				switch (action) {
				case "insert":
					ParamUtils.checkStrings(title);
					PlanningService.insert(title);
					ToastUtils.insert(request, "Planejamento incluído", TYPE.success);
					break;
				case "edit":
					ParamUtils.checkLongs(idPlanning);
					ParamUtils.checkStrings(title);
					PlanningService.update(idPlanning, title);
					ToastUtils.insert(request, "Planejamento alterado", TYPE.success);
					break;
				case "delete":
					ParamUtils.checkLongs(idPlanning);
					PlanningService.deleteById(idPlanning);
					ToastUtils.insert(request, "Planejamento removido", TYPE.success);
					break;
				default:
					break;
				}
				response.sendRedirect("./planejamentos");
			} catch (SQLException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
			}
		}
	}

}
