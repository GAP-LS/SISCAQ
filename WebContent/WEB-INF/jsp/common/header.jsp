<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-inverse" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navBar">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>                        
			</button>
			<a class="navbar-brand" href="./">SISCAQ</a>
		</div>
		<div class="collapse navbar-collapse" id="navBar">
			<ul class="nav navbar-nav">
				<c:if test="${sessionScope.logged}">
					<li role="presentation" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
							Processos <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="./processos">Listar</a></li>
						</ul>
					</li>
					<li role="presentation" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
							Administração <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="./usuarios">Gerenciar usuários</a></li>
							<li><a href="./planejamentos">Gerenciar planejamentos</a></li>
						</ul>
					</li>
				</c:if>
				<li role="presentation" class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
						Ajuda <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="./informacoes">Informações do sistema</a></li>
						<li class="divider" />
						<li><a href="./ajuda">Ajuda do sistema</a></li>
					</ul>
				</li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<c:if test="${sessionScope.logged}">
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
							${sessionScope.planning.title} <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<c:forEach var="p" items="${plannings}">
								<li><a href="./planejamento?set=${p.id}">${p.title}</a></li>
							</c:forEach>
						</ul>
					</li>
				</c:if>
				<li>
					<c:choose>
						<c:when test="${sessionScope.logged}">
							<a href="./logout"><span class="glyphicon glyphicon-log-out"></span> ${sessionScope.user}</a>
						</c:when>
						<c:otherwise>
							<a href="./login"><span class="glyphicon glyphicon-log-in"></span> Entrar</a>
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
		</div>
	</div>
</nav>