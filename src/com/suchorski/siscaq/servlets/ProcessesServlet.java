package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.digitgenerator.exception.InvalidFormatException;
import com.suchorski.digitgenerator.exception.InvalidNumberException;
import com.suchorski.digitgenerator.generator.custom.NUPGenerator;
import com.suchorski.digitgenerator.number.custom.NUPNumber;
import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.exceptions.InfoException;
import com.suchorski.siscaq.models.Modality;
import com.suchorski.siscaq.models.Planning;
import com.suchorski.siscaq.models.Process;
import com.suchorski.siscaq.models.Type;
import com.suchorski.siscaq.models.Unity;
import com.suchorski.siscaq.models.User;
import com.suchorski.siscaq.services.ModalityService;
import com.suchorski.siscaq.services.NoteService;
import com.suchorski.siscaq.services.ProcessService;
import com.suchorski.siscaq.services.StatusService;
import com.suchorski.siscaq.services.TypeService;
import com.suchorski.siscaq.services.UnityService;
import com.suchorski.siscaq.services.UserService;
import com.suchorski.siscaq.servlets.helpers.PlanningHelper;
import com.suchorski.siscaq.utils.SISCAQ;
import com.suchorski.siscaq.utils.ServletUtils.AccessUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;
import com.suchorski.siscaq.utils.ServletUtils.ToastUtils;

@WebServlet({"/processos", "/index"})
public class ProcessesServlet extends HttpServlet {

	private static final long serialVersionUID = -6420062686141356155L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String message = "";
			try {
				PlanningHelper.initialize(request);
				Planning planning = (Planning) request.getSession().getAttribute("planning");
				String search = ParamUtils.parseString(request, "busca", "");
				request.setAttribute("search", search);
				request.setAttribute("status", StatusService.list());
				request.setAttribute("units", UnityService.list());
				request.setAttribute("types", TypeService.list());
				request.setAttribute("modalitys", ModalityService.list());
				request.setAttribute("responsibles", UserService.listResponsibles());
				if ((boolean) request.getSession().getAttribute("unhighlight")) {
					request.getSession().removeAttribute("highlight");					
				} else {
					request.getSession().setAttribute("unhighlight", true);
				}
				request.setAttribute("processes", ProcessService.list(search, planning.getId()));
			} catch (InfoException e) {
				message = e.getMessage();
			}
			request.setAttribute("message", message);
			request.setAttribute("template", "processes");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		} catch (SQLException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		} catch (DatabaseException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String action = ParamUtils.parseString(request, "action", "");
			long id = ParamUtils.parseLong(request, "id", 0);
			long unityId = ParamUtils.parseLong(request, "id_unity", 0);
			String description = ParamUtils.parseString(request, "description", "");
			String note = ParamUtils.parseString(request, "note", "");
			String nup = ParamUtils.parseString(request, "nup", "").replaceAll("\\D", "");
			String pam = ParamUtils.parseString(request, "pam", "");
			String responsibleTr = ParamUtils.parseString(request, "responsible_tr", "");
			Date date = ParamUtils.parseDate(request, "date");
			double value = ParamUtils.parseCurrency(request, "value", 0);
			long responsibleId = ParamUtils.parseLong(request, "id_responsible", 0);
			String info = ParamUtils.parseString(request, "info", "");
			long typeId = ParamUtils.parseLong(request, "id_type", 0);
			long modalityId = ParamUtils.parseLong(request, "id_modality", 0);
			Planning planning = (Planning) request.getSession().getAttribute("planning");
			long statusId = ParamUtils.parseLong(request, "id_status", 0);
			switch (action) {
			case "insert":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.EDITOR) {
					ParamUtils.checkLongs(unityId, responsibleId);
					ParamUtils.checkStrings(description);
					if (!nup.isEmpty()) { new NUPGenerator().check(new NUPNumber(nup)); }
					ProcessService.insert(unityId, description, nup, pam, date, responsibleTr, value, info, responsibleId, typeId, planning.getId(), modalityId);
					ToastUtils.insert(request, "Processo inserido", TYPE.success);
					response.sendRedirect(request.getRequestURI());
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.EDITOR_NAME), TYPE.danger, request, response);					
				}
				break;
			case "edit":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.EDITOR) {
					ParamUtils.checkLongs(id, unityId, responsibleId);
					ParamUtils.checkStrings(description);
					if (!nup.isEmpty()) { new NUPGenerator().check(new NUPNumber(nup)); }
					ProcessService.update(new Process(id, new Unity(unityId, null), description, nup, pam, null, value, date, responsibleTr, info, new User(responsibleId, ""), new Type(typeId, ""), planning, new Modality(modalityId, ""), null));
					ToastUtils.insert(request, "Processo atualizado", TYPE.success);
					request.getSession().setAttribute("unhighlight", false);
					request.getSession().setAttribute("highlight", id);
					String redirect = request.getRequestURI();
					if (request.getQueryString() != null) {
						redirect += "?" + request.getQueryString();
					}
					response.sendRedirect(redirect + "#d" + id);
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.EDITOR_NAME), TYPE.danger, request, response);					
				}
				break;
			case "forward":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.EDITOR) {
					ParamUtils.checkLongs(id);
					ProcessService.forward(id, date);
					ToastUtils.insert(request, "Processo avançado", TYPE.success);
					request.getSession().setAttribute("unhighlight", false);
					request.getSession().setAttribute("highlight", id);
					String redirect = request.getRequestURI();
					if (request.getQueryString() != null) {
						redirect += "?" + request.getQueryString();
					}
					response.sendRedirect(redirect + "#d" + id);
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.EDITOR_NAME), TYPE.danger, request, response);					
				}
				break;
			case "backward":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.EDITOR) {
					ParamUtils.checkLongs(id);
					ParamUtils.checkStrings(description);
					ProcessService.backward(id, user.getId(), description);
					ToastUtils.insert(request, "Processo regredido", TYPE.success);
					request.getSession().setAttribute("unhighlight", false);
					request.getSession().setAttribute("highlight", id);
					String redirect = request.getRequestURI();
					if (request.getQueryString() != null) {
						redirect += "?" + request.getQueryString();
					}
					response.sendRedirect(redirect + "#d" + id);
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.EDITOR_NAME), TYPE.danger, request, response);
				}
				break;
			case "info":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.READER) {
					ParamUtils.checkLongs(id, statusId);
					ParamUtils.checkStrings(note);
					NoteService.insert(id, statusId, user.getId(), note);
					ToastUtils.insert(request, "Informação adicionada", TYPE.success);
					request.getSession().setAttribute("unhighlight", false);
					request.getSession().setAttribute("highlight", id);
					String redirect = request.getRequestURI();
					if (request.getQueryString() != null) {
						redirect += "?" + request.getQueryString();
					}
					response.sendRedirect(redirect + "#d" + id);
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.READER_NAME), TYPE.danger, request, response);
				}
				break;
			case "delete":
				if (user.getLevel().getLevel() >= SISCAQ.LEVELS.MANAGER) {
					ParamUtils.checkLongs(id);
					ProcessService.deleteById(id);
					ToastUtils.insert(request, "Processo removido", TYPE.success);
					String redirect = request.getRequestURI();
					if (request.getQueryString() != null) {
						redirect += "?" + request.getQueryString();
					}
					response.sendRedirect(redirect);
				} else {
					MessageUtils.dispatch(AccessUtils.generateMessage(SISCAQ.LEVELS.MANAGER_NAME), TYPE.danger, request, response);					
				}
				break;
			default:
				response.sendRedirect(request.getRequestURI());
				break;
			}
		} catch (InvalidFormatException | InvalidNumberException e) {
			MessageUtils.dispatch("O número NUP está incorreto", TYPE.danger, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		}
	}

}
