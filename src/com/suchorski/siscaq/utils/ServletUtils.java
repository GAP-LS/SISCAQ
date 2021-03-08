package com.suchorski.siscaq.utils;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.suchorski.siscaq.models.User;

public class ServletUtils {

	public static class MessageUtils {

		public enum TYPE {
			success, info, warning, danger
		}

		public static void dispatch(String message, TYPE type, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
			request.setAttribute("template", "info");
			request.setAttribute("infoMessage", message);
			request.setAttribute("infoType", type);
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		}

	}

	public static class ParamUtils {

		public static String parseString(HttpServletRequest request, String param, String defaultValue) {
			String value = (String) request.getParameter(param);
			return value != null ? value : defaultValue;
		}

		public static long parseLong(HttpServletRequest request, String param, long defaultValue) {
			if (request.getParameter(param) == null) {
				return defaultValue;
			}
			try {
				return Long.parseLong((String) request.getParameter(param));
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		
		public static double parseDouble(HttpServletRequest request, String param, double defaultValue) {
			if (request.getParameter(param) == null) {
				return defaultValue;
			}
			try {
				return Double.parseDouble((String) request.getParameter(param));
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}

		public static double parseCurrency(HttpServletRequest request, String param, double defaultValue) {
			if (request.getParameter(param) == null) {
				return defaultValue;
			}
			String currency = request.getParameter(param).replaceAll("[R$\\s\\.]", "").replaceAll(",", ".");
			try {
				return Double.parseDouble(currency);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}

		public static List<Integer> parseInts(HttpServletRequest request, String param) throws Exception {
			try {
				List<Integer> values = new ArrayList<Integer>();
				String[] strings = request.getParameterValues(param);
				if (strings != null) {
					for (String s : strings) {
						values.add(Integer.parseInt(s));
					}
				}
				return values;
			} catch (NumberFormatException e) {
				throw new Exception("Dados inválidos");
			}
		}
		
		public static Date parseDate(HttpServletRequest request, String param) throws Exception {
			if (request.getParameter(param) == null) {
				return new Date(new java.util.Date().getTime());
			}
			try {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				format.setLenient(false);
				return new Date(format.parse(request.getParameter(param)).getTime());
			} catch (ParseException e) {
				throw new Exception("Data com formato inválido");
			} catch (Exception e) {
				return new Date(new java.util.Date().getTime());
			}
		}

		public static void checkLongs(long... longs) throws Exception {
			for (long i : longs) {
				if (i == 0) {
					throw new Exception("Dados inválidos");
				}
			}
		}

		public static void checkStrings(String... strings) throws Exception {
			for (String s : strings) {
				if (s == null || s.isEmpty()) {
					throw new Exception("Dados inválidos");
				}
			}
		}

	}

	public static class ToastUtils {

		public static void insert(HttpServletRequest request, String message, MessageUtils.TYPE type) {
			HttpSession session = request.getSession();
			session.setAttribute("toastMessage", message);
			session.setAttribute("toastType", type);
		}

		public static void make(HttpServletRequest request) {
			HttpSession session = request.getSession();
			if (session.getAttribute("toastMessage") != null) {				
				request.setAttribute("toastMessage", session.getAttribute("toastMessage"));
				request.setAttribute("toastType", session.getAttribute("toastType"));
			}
			session.removeAttribute("toastMessage");
			session.removeAttribute("toastType");
		}

	}

	public static class AccessUtils {

		private static final String deniedMessage = "Para acessar essa função, favor solicitar o nível de %s a um <a href='./informacoes'>administrador</a> do sistema.";

		public static boolean hasAccess(HttpServletRequest request, HttpServletResponse response, long level, String name) throws ServletException, IOException {
			User u = (User) request.getSession().getAttribute("user");
			if (u.getLevel().getLevel() >= level) {
				return true;
			}
			MessageUtils.dispatch(generateMessage(name), MessageUtils.TYPE.danger, request, response);
			return false;
		}
		
		public static String generateMessage(String levelName) {

			return String.format(deniedMessage, levelName);
			
		}

	}

}
