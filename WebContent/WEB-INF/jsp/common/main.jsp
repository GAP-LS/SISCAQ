<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt_BR">
	<%@ include file="./head.jsp" %>
	<body>
		<%@ include file="./header.jsp" %>
		<div class="container-fluid print">    
			<c:import url="../pages/${template}.jsp" />
		</div>
		<%@ include file="./footer.jsp" %>
		<div id="spinner"><div id="spinnerContainer"></div></div>
		<c:if test="${not empty toastMessage}">
			<script type="text/javascript">toast('${toastMessage}', '${toastType}')</script>
		</c:if>
	</body>
</html>