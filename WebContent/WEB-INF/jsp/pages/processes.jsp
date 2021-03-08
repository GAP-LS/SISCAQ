<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<h1 class="no-print">Processos</h1>
<div class="row no-print">
	<form method="get">
		<div class="col-sm-5">
			<div class="form-group">
				<input type="text" class="form-control" name="busca" placeholder="Busca por: unidade, processo, responsável ou data de início" />
			</div>
		</div>
		<div class="col-sm-1">
			<input type="submit" class="btn btn-primary btn-block" value="Buscar" />
		</div>
	</form>
	<div class="col-sm-2">
		<button class="btn btn-info btn-block" onclick="exportProcess()">Exportar dados</button>
	</div>
	<div class="col-sm-2">
		<button class="btn btn-info btn-block" onclick="window.print();">Imprimir</button>
	</div>
	<div class="col-sm-2">
		<button class="btn btn-success btn-block" onclick="newProcess(${user.id})">Novo processo</button>
	</div>
</div>
<c:choose>
	<c:when test="${not empty message}">
		<div class="alert alert-info">${message}</div>
	</c:when>
	<c:otherwise>
		<table class="table table-hover sticky">
			<thead>
				<tr class="active" style="height: 180px;">
					<th style="width: 6%;" class="text-center">Unidade</th>
					<th style="width: 20%;">Processo</th>
					<th style="width: 13%;">NUP</th>
					<th style="width: 8%;">PAM</th>
					<th style="width: 5%;">Valor Referência</th>
					<th style="width: 10%;">TR (Responsável)</th>
					<th style="width: 10%;">Edital (Responsável)</th>
					<th style="width: 1%;" class="text-center rotate"><div style="margin-top: -40px; white-space: nowrap;">Início (Previsão)</div></th>
					<c:forEach var="s" items="${status}">
						<th style="width: 1%;" class="text-center rotate"><div style="margin-top: -40px; white-space: nowrap;"><a href="#" onclick="filterProcess(${s.id})">${s.description}</a></div></th>
					</c:forEach>
					<th style="width: 10%;" class="text-center no-print">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="p" items="${processes}">
					<c:set var="id_status" value="${-1}"/>
					<tr class="process" id="d${p.id}" data-id="${p.id}" data-id-status="${id_status}" data-id-unity="${p.unity.id}" data-nup="${p.nup}" data-pam="${p.pam}" data-tr="${p.responsibleTr}" data-value="${p.value}" data-id-type="${p.type.id}" data-info="${p.info}" data-id-responsible="${p.responsible.id}" data-id-modality="${p.modality.id}">
						<td class="text-center${sessionScope.highlight eq p.id ? ' info' : ''}">${p.unity.initials}</td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}">${p.description}</td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}">${p.formatedNup}</td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}">${p.pam}</td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}"><fmt:formatNumber type="CURRENCY" maxFractionDigits="2" minFractionDigits="2" value="${p.value}" /></td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}">${p.responsibleTr}</td>
						<td class="${sessionScope.highlight eq p.id ? 'info' : ''}">${p.responsible.displayName}</td>
						<td class="rotate text-center${sessionScope.highlight eq p.id ? ' info' : ' active'}"><div><fmt:formatDate pattern = "dd/MM/yyyy" value="${p.date}" /></div></td>
						<c:set var="actual_status" value="${true}"/>
						<c:forEach var="s" items="${p.status}">
							<c:choose>
								<c:when test="${s.date eq null}">
									<c:choose>
										<c:when test="${actual_status eq true}">
											<td class="${p.bootstrapStatus(s.days)} text-center rotate"><div><b><fmt:formatDate pattern="dd/MM/yyyy" value="${p.promptStatus(s.days)}" /></b></div></td>
											<c:set var="actual_status" value="${false}"/>
											<c:set var="id_status" value="${s.id}"/>
										</c:when>
										<c:otherwise>
											<td class="${p.bootstrapStatus(s.days)} text-center rotate"><div><fmt:formatDate pattern="dd/MM/yyyy" value="${p.promptStatus(s.days)}" /></div></td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td class="info text-center rotate"><div><fmt:formatDate pattern="dd/MM/yyyy" value="${s.date}" /></div></td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<td class="no-print${sessionScope.highlight eq p.id ? ' info' : ''}">
							<form method="post" class="form-inline">
								<input type="hidden" name="id" value="${p.id}" />
								<c:if test="${isEditor and p.countStatus gt 0}">
									<button type="button" class="btn btn-warning" onclick="backwardProcess('#d${p.id}')">
										<span class="glyphicon glyphicon-step-backward" aria-hidden="true"></span>
									</button>
								</c:if>
								<c:if test="${isEditor and p.countStatus lt status.size()}">
									<button type="button" class="btn btn-success" onclick="forwardProcess('#d${p.id}')">
										<span class="glyphicon glyphicon-step-forward" aria-hidden="true"></span>
									</button>
								</c:if>
								<a href="processo?id=${p.id}" class="btn btn-info">
									<span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span>
								</a>
								<button type="button" class="btn btn-default" onclick="infoProcess('#d${p.id}')">
									<span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
								</button>
								<c:if test="${isEditor}">
									<button type="button" class="btn btn-primary" onclick="editProcess('#d${p.id}')">
										<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
									</button>
								</c:if>
								<c:if test="${isManager}">
									<button type="submit" name="action" value="delete" class="btn btn-danger">
										<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
									</button>
								</c:if>
							</form>
						</td>
					</tr>
					<script type="text/javascript">$('#d${p.id}').data('id-status', '${id_status}')</script>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
<div class="modal fade no-print" id="modal_export">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Selecione o formato</h4>
			</div>
			<div class="modal-body">
				<a href="export?format=csv&search=${search}" class="btn btn-primary btn-block">CSV</a>
			</div>
		</div>
	</div>
</div>
<div class="modal fade no-print" id="modal_process">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="modal_process_title">&nbsp;</h4>
			</div>
			<form method="post">
				<input type="hidden" id="modal_process_action" name="action" value="" />
				<input type="hidden" id="modal_process_id" name="id" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_process_unity">Unidade</label>
						<select class="form-control selectpicker" id="modal_process_unity" name="id_unity">
							<c:forEach var="unity" items="${units}">
								<option value="${unity.id}">${unity.initials}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_process_description">Descrição</label>
						<input type="text" class="form-control" id="modal_process_description" name="description" pattern=".{1,256}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_process_nup">NUP</label>
						<input type="text" class="form-control" id="modal_process_nup" name="nup" data-mask="00000.000000/0000-00" pattern="\d{5}\.\d{6}\/\d{4}-\d{2}" />
					</div>
					<div class="form-group">
						<label for="modal_process_pam">PAM</label>
						<input type="text" class="form-control" id="modal_process_pam" name="pam" pattern=".{0,20}" />
					</div>
					<div class="form-group">
						<label for="modal_process_value">Valor Referência</label>
						<input type="text" class="form-control currency" id="modal_process_value" name="value" />
					</div>
					<div class="form-group">
						<label for="modal_process_date">Data</label>
						<input type="text" class="form-control datepicker" id="modal_process_date" name="date" required="required" pattern="\d{2}/\d{2}/\d{4}" />
					</div>
					<div class="form-group">
						<label for="modal_process_tr">TR (Responsável)</label>
						<input type="text" class="form-control" id="modal_process_tr" name="responsible_tr" pattern=".{1,64}" />
					</div>
					<div class="form-group">
						<label for="modal_process_type">Tipo</label>
						<select class="form-control selectpicker" id="modal_process_type" name="id_type">
							<c:forEach var="type" items="${types}">
								<option value="${type.id}">${type.title}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_process_modality">Modalidade</label>
						<select class="form-control selectpicker" id="modal_process_modality" name="id_modality">
							<c:forEach var="modality" items="${modalitys}">
								<option value="${modality.id}">${modality.title}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_process_info">Informação</label>
						<input type="text" class="form-control" id="modal_process_info" name="info" pattern=".{0,1024}" />
					</div>
					<div class="form-group">
						<label for="modal_process_unity">Edital (Responsável)</label>
						<select class="form-control selectpicker" id="modal_process_responsible" name="id_responsible">
							<c:forEach var="responsible" items="${responsibles}">
								<option value="${responsible.id}">${responsible.displayName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="modal fade no-print" id="modal_progress">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Progresso de processo</h4>
			</div>
			<form method="post">
				<input type="hidden" name="action" value="forward" />
				<input type="hidden" id="modal_progress_id" name="id" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_progress_date">Data</label>
						<input type="text" class="form-control datepicker" id="modal_progress_date" name="date" value='<fmt:formatDate pattern="dd/MM/yyyy" value="<%= new java.util.Date() %>" />' required="required" pattern="\d{2}/\d{2}/\d{4}" />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="modal fade no-print" id="modal_regress">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Regressão de processo</h4>
			</div>
			<form method="post">
				<input type="hidden" name="action" value="backward" />
				<input type="hidden" id="modal_regress_process" name="id" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_regress_description">Descrição</label>
						<input type="text" class="form-control" id="modal_regress_description" name="description" pattern=".{1,256}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_regress_user">Usuário</label>
						<input type="text" class="form-control" id="modal_regress_user" value="${sessionScope.user.displayName}" readonly />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="modal fade no-print" id="modal_info">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Anotação de processo</h4>
			</div>
			<form method="post">
				<input type="hidden" name="action" value="info" />
				<input type="hidden" id="modal_info_process" name="id" value="" />
				<input type="hidden" id="modal_info_status" name="id_status" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_regress_description">Anotação</label>
						<input type="text" class="form-control" id="modal_info_note" name="note" pattern=".{1,512}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_info_user">Usuário</label>
						<input type="text" class="form-control" id="modal_info_user" value="${sessionScope.user.displayName}" readonly />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>