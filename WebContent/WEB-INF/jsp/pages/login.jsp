<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="help-block">&nbsp;</div>
<div class="row">
	<div class="col-sm-4 col-sm-offset-4">
		<form method="post">
			<div class="form-group">
				<label for="cpf">Login FAB</label>
				<div class="input-group">
					<span class="input-group-addon"><span class="glyphicon glyphicon-user" aria-hidden="true"></span></span>
					<input type="text" class="form-control" id="cpf" name="cpf" required="required" placeholder="Usuario do portal do militar" pattern=".{11}" data-mask="00000000000" autofocus />
				</div>
			</div>
			<div class="form-group">
				<label for="password">Senha</label>
				<div class="input-group">
					<span class="input-group-addon"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></span>
					<input type="password" class="form-control" id="password" name="password" required="required" placeholder="Senha do portal do militar" pattern=".{1,}" />
				</div>
			</div>
			<input type="submit" class="btn btn-primary btn-block" value="Entrar" />
		</form>
	</div>
</div>