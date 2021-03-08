package com.suchorski.siscaq.servlets.helpers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.services.PlanningService;

public class PlanningHelper {
	
	public static void initialize(HttpServletRequest request) throws SQLException, DatabaseException {
		request.setAttribute("plannings", PlanningService.list());
	}

}
