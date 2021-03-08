<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="panel-group" id="ajudas" role="tablist" aria-multiselectable="true">
	<h1>Informações</h1>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h1">
			<h3 class="panel-title">
				<a role="button" data-toggle="collapse" data-parent="#ajudas" href="#c1" aria-expanded="true" aria-controls="c1">
					Administradores do sistema
				</a>
			</h3>
		</div>
		<div id="c1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="h1">
			<div class="panel-body text-justify">
				<c:choose>
					<c:when test="${empty administrators}">
						Não existem administradores cadastrados no sistema. Contate a seção de informática da sua unidade.
					</c:when>
					<c:otherwise>
						<ul>
							<c:forEach var="a" items="${administrators}">
								<li>${a.displayName}</li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>