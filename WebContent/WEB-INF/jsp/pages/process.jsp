<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<h1>Processo: ${process.description}</h1>
<h4>NUP: ${process.formatedNup}</h4>
<h4>NUP: ${process.pam}</h4>
<h4>Data (Previsão): <fmt:formatDate pattern = "dd/MM/yyyy" value="${process.date}" /></h4>
<h4>TR (Responsável): ${process.responsibleTr}</h4>
<h4>Edital (Responsável): ${process.responsible.displayName}</h4>
<c:if test="${not empty process.info}">
	<div class="alert alert-info">${process.info}</div>
</c:if>
<h1>Status</h1>
<table class="table table-hover">
	<thead>
		<tr class="active">
			<th class="col-sm-6">Status</th>
			<th class="col-sm-2">Prazo</th>
			<th class="col-sm-2">Prazo flexível</th>
			<th class="col-sm-2">Fechamento</th>
		</tr>
	</thead>
	<tbody>
		<c:set var="lastDateClosed" value="${process.date}" />
		<c:set var="lastDaysClosed" value="0" />
		<c:set var="firstDaysOpen" value="-1" />
		<c:forEach var="s" items="${process.status}">
			<c:choose>
				<c:when test="${not empty s.date}">
					<c:set var="lastDateClosed" value="${s.date}" />
					<c:set var="lastDaysClosed" value="${s.days}" />
					<c:set var="color" value="info"></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="color" value="${process.bootstrapStatus(s.days)}"></c:set>
					<c:if test="${firstDaysOpen eq -1}">
						<c:set var="firstDaysOpen" value="${s.days}" />
					</c:if>
				</c:otherwise>
			</c:choose>
			<tr class="${color}">
				<td>${s.description}<c:if test="${isDeveloper}"> <font class="dev-text">[Prazo: ${s.days} dias]</font></c:if></td>
				<td><fmt:formatDate pattern="dd/MM/yyyy" value="${process.promptStatus(s.days)}" /></td>
				<td><c:if test="${empty s.date}"><fmt:formatDate pattern="dd/MM/yyyy" value="${process.flexPromptStatus(s.days, lastDaysClosed, lastDateClosed, firstDaysOpen)}" /></c:if></td>
				<td><c:if test="${not empty s.date}"><c:set var="date" value="${s.date}" /><fmt:formatDate pattern="dd/MM/yyyy" value="${s.date}" /></c:if></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${not empty notes}">
	<h1>Notas</h1>
	<table class="table table-hover">
		<thead>
			<tr class="active">
				<th class="col-sm-2">Data</th>
				<th class="col-sm-6">Nota</th>
				<th class="col-sm-2">Fase</th>
				<th class="col-sm-2">Usuário</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="n" items="${notes}">
				<tr>
					<td><fmt:formatDate pattern = "dd/MM/yyyy 'às' HH:mm" value="${n.creationDate}" /></td>
					<td>${n.note}</td>
					<td>${n.status.description}</td>
					<td>${n.user.displayName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>
<h1>Regressões</h1>
<c:choose>
	<c:when test="${empty regresses}">
		<div class="alert alert-info">Esse processo não tem regressões</div>
	</c:when>
	<c:otherwise>
		<table class="table table-hover">
			<thead>
				<tr class="active">
					<th class="col-sm-2">Data</th>
					<th class="col-sm-6">Descrição</th>
					<th class="col-sm-2">Status</th>
					<th class="col-sm-2">Usuário</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="regress" items="${regresses}">
					<tr>
						<td><fmt:formatDate pattern="dd/MM/yyy 'às' HH:mm" value="${regress.date}" /></td>
						<td>${regress.description}</td>
						<td>${regress.status.description}</td>
						<td>${regress.user.displayName}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
