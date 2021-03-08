<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>Gerenciar usuários</h1>
<table class="table">
	<thead>
		<tr>
			<th class="col-sm-8">Usuário</th>
			<th class="col-sm-4">Nível</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${users}">
			<tr>
				<td>${u.displayName}</td>
				<td>
					<form method="post">
						<input type="hidden" name="action" value="edit_user_level" />
						<input type="hidden" name="id_user" value="${u.id}" />
						<select class="form-control selectpicker" name="id_level" onchange="showSpinner(this.form.submit())">
							<c:forEach var="level" items="${levels}">
								<option value="${level.id}"${u.level.id eq level.id ? ' data-t="1" selected' : ''}${sessionScope.user.level.level < level.level ? ' disabled' : ''}>${level.level} - ${level.description}</option>
							</c:forEach>
						</select>
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>