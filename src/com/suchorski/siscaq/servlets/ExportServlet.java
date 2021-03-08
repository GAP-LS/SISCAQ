package com.suchorski.siscaq.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.exceptions.InfoException;
import com.suchorski.siscaq.models.Planning;
import com.suchorski.siscaq.models.Process;
import com.suchorski.siscaq.models.Status;
import com.suchorski.siscaq.services.ProcessService;
import com.suchorski.siscaq.services.StatusService;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils;
import com.suchorski.siscaq.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.siscaq.utils.ServletUtils.ParamUtils;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {

	private static final long serialVersionUID = -5152183809907659613L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String format = ParamUtils.parseString(request, "format", "csv");
			String search = ParamUtils.parseString(request, "search", "");
			String output = "";
			Planning planning = (Planning) request.getSession().getAttribute("planning");
			switch (format) {
			default: // CSV
				output = generateCSV(search, planning.getId());
				response.setContentType("text/csv");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=\"report.csv\"");
				break;
			}
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(output.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (SQLException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		} catch (DatabaseException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		} catch (InfoException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.info, request, response);
		}
	}

	private static String generateCSV(String search, long idPlanning) throws SQLException, DatabaseException, InfoException {
		StringBuilder sb = new StringBuilder();
		List<Process> processes = ProcessService.list(search, idPlanning);
		List<Status> statuses = StatusService.list();
		sb.append("UNIDADE,DESCRIÇÃO,NUP,INÍCIO");
		statuses.forEach(s -> sb.append("," + s.getDescription()));
		sb.append("\n");
		processes.forEach(p -> sb.append(p.toCSV()));
		return sb.toString();
	}

}
