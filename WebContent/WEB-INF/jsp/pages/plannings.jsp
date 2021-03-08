<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<h1 class="no-print">Processos</h1>
<div class="row">
	<div class="col-sm-offset-9 col-sm-3">
		<button class="btn btn-primary btn-block" onclick="newPlanning()">Novo planejamento</button>
	</div>
</div>
<c:choose>
	<c:when test="${not empty message}">
		<div class="alert alert-info">${message}</div>
	</c:when>
	<c:otherwise>
		<table class="table table-hover sticky">
			<thead>
				<tr>
					<th style="width: 80%;">Planejamento</th>
					<th style="width: 20%;">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="p" items="${plannings}">
					<tr id="p${p.id}" data-id="${p.id}">
						<td>${p.title}</td>
						<td>
							<form method="post" class="form-inline">
								<input type="hidden" name="id_planning" value="${p.id}" />
								<button type="button" class="btn btn-warning" onclick="editPlanning('#p${p.id}');">
									<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</button>
								<button type="submit" name="action" value="delete" class="btn btn-danger">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
<div class="modal fade no-print" id="modal_planning">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="modal_planning_title"></h4>
			</div>
			<form method="post">
				<input type="hidden" id="planning_action" name="action" value="" />
				<input type="hidden" id="planning_id" name="id_planning" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_regress_description">Planejamento</label>
						<input type="text" class="form-control" id="planning_title" name="title" pattern=".{1,64}" required="required" />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>