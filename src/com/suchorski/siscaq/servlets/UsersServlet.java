package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Level;
import com.suchorski.siscaq.models.User;
import com.suchorski.siscaq.services.LevelService;
import com.suchorski.siscaq.services.UserService;
import com.suchorski.siscaq.servlets.helpers.PlanningHelper;
import com.suchorski.siscaq.utils.SISCAQ;
import com.suchorski.siscaq.utils.ServletUtils.AccessUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;
import com.suchorski.siscaq.utils.ServletUtils.ToastUtils;

@WebServlet("/usuarios")
public class UsersServlet extends HttpServlet {

	private static final long serialVersionUID = -8978155022283950353L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, SISCAQ.LEVELS.MANAGER, SISCAQ.LEVELS.MANAGER_NAME)) {
			try {
				PlanningHelper.initialize(request);
				request.setAttribute("users", UserService.list());
				request.setAttribute("levels", LevelService.list());
				request.setAttribute("template", "users");
				request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
			} catch (SQLException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			} catch (DatabaseException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, SISCAQ.LEVELS.MANAGER, SISCAQ.LEVELS.MANAGER_NAME)) {
			try {
				String action = ParamUtils.parseString(request, "action", "");
				long idUser = ParamUtils.parseLong(request, "id_user", 0);
				long idLevel = ParamUtils.parseLong(request, "id_level", 0);
				switch (action) {
				case "edit_user_level":
					ParamUtils.checkLongs(idUser, idLevel);
					User loggedUser = (User) request.getSession().getAttribute("user");
					User editUser = UserService.getById(idUser);
					Level level = LevelService.getById(idLevel);
					if (loggedUser.getLevel().getLevel() < level.getLevel()) {
						ToastUtils.insert(request, "O nível de acesso solicitado é maior que o seu.", TYPE.danger);
					} else if (editUser.getLevel().getLevel() > loggedUser.getLevel().getLevel()) {
						ToastUtils.insert(request, "O nível de acesso do usuário é maior que o seu.", TYPE.danger);
					} else {
						UserService.updateLevel(idUser, idLevel);
						ToastUtils.insert(request, "Nível de acesso alterado.", TYPE.success);
					}
					break;
				default:
					break;
				}
				response.sendRedirect("./usuarios");
			} catch (SQLException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
			}
		}
	}

}
