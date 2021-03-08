<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>Aviso</h1>
<div class="alert alert-${infoType}">
	<strong>${infoMessage}</strong>
</div>
<button class="btn btn-primary" onclick="window.history.back();">
	<span class="glyphicon glyphicon glyphicon-chevron-left"></span>
	 Voltar
</button>