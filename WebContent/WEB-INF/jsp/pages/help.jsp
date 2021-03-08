<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="panel-group" id="ajudas" role="tablist" aria-multiselectable="true">
	<h1>Ajuda aos usuários</h1>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h1">
			<h3 class="panel-title">
				<a role="button" data-toggle="collapse" data-parent="#ajudas" href="#c1" aria-expanded="true" aria-controls="c1">
					Primeiro acesso
				</a>
			</h3>
		</div>
		<div id="c1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="h1">
			<div class="panel-body text-justify">
				Para o primeiro acesso ao sistema, o usuário deverá entrar com dados do Acesso Único do CCA-RJ (Portal do militar, zimbra, SILOMS) e solicitar a um administrador do sistema seu nícel de acesso.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h2">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse" data-parent="#ajudas" href="#c2" aria-expanded="false" aria-controls="c2">
					Listagem de processos
				</a>
			</h3>
		</div>
		<div id="c2" class="panel-collapse collapse" role="tabpanel" aria-labelledby="h2">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para listar os processos, deve-se ter o nível 1.</pre> A listagem de processos mostra todos os processos adicionados no sistema.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h3">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse" data-parent="#ajudas" href="#c3" aria-expanded="false" aria-controls="c3">
					Gerenciamento de usuários
				</a>
			</h3>
		</div>
		<div id="c3" class="panel-collapse collapse" role="tabpanel" aria-labelledby="h3">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para gerenciar os usuários, deve-se ter o nível 8.</pre> O gerenciamento de usuários serve para gerenciar os níveis dos usuários que já acessaram o sistema.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h3">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse" data-parent="#ajudas" href="#c3" aria-expanded="false" aria-controls="c3">
					Gerenciamento de planejamentos
				</a>
			</h3>
		</div>
		<div id="c3" class="panel-collapse collapse" role="tabpanel" aria-labelledby="h3">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para gerenciar os planejamentos, deve-se ter o nível 8.</pre> O gerenciamento de planejamentos serve para gerenciar os planejamentos do sistema.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h4">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse" data-parent="#ajudas" href="#c4" aria-expanded="false" aria-controls="c4">
					Cores do sistema
				</a>
			</h3>
		</div>
		<div id="c4" class="panel-collapse collapse" role="tabpanel" aria-labelledby="h4">
			<div class="panel-body text-justify">
				<pre class="alert alert-info">As datas em azul são as datas incluídas no sistema. As demais cores são as datas previstas para a conclusão.</pre>
				<table class="table">
					<thead>
						<tr><th>Cor</th><th>Descrição</th></tr>
					</thead>
					<tbody>
						<tr><td>Azul</td><td>Etapa finalizada</td></tr>
						<tr><td>Verde</td><td>Etapa no tempo previsto</td></tr>
						<tr><td>Amarelo</td><td>Etapa prestes a expirar o prazo</td></tr>
						<tr><td>Vermelho</td><td>Etapa com prazo finalizado</td></tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<h1>Sobre o sistema</h1>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h5">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse" data-parent="#ajudas" href="#c5" aria-expanded="false" aria-controls="c5">
					Perfis de Acesso
				</a>
			</h3>
		</div>
		<div id="c5" class="panel-collapse collapse" role="tabpanel" aria-labelledby="h5">
			<div class="panel-body text-justify">
				<table class="table">
					<thead>
						<tr><th>Nível</th><th>Título</th><th>Descrição</th></tr>
					</thead>
					<tbody>
						<tr><td>100</td><td>Desenvolvedor</td><td>Usuário com acesso de desenvolvimento</td></tr>
						<tr><td>8</td><td>Gerente</td><td>Usuário pode excluir os processos, gerenciar usuários e planejamentos</td></tr>
						<tr><td>4</td><td>Editor</td><td>Usuário pode editar os processos</td></tr>
						<tr><td>1</td><td>Leitor</td><td>Usuário pode visualizar os processos</td></tr>
						<tr><td>0</td><td>Usuário sem acesso</td><td>Usuário fica sem acesso ao sistema</td></tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>