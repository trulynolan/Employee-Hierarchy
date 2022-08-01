<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- c:out ; c:forEach ; c:if -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Formatting (like dates) -->
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard; first page when you log in.</title>
<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>
	<nav class="navbar navbar-expand-sm navbar-dark bg-dark mb-3">
		<div class="container">
			<span class="navbar-brand mb-0 h1">Welcome, ${user.userName}</span>
			<a class="btn btn-primary ml-auto" href="/logout">Logout</a>
		</div>
	</nav>
	<div class="container">
		<table class="table table-dark table-striped table-hover">
			<thead>
				<tr>
					<th>User Name</th>
					<th>Email</th>
					<th>Level</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="teamMember" items="${team}">
					<tr>
						<td>${teamMember.userName}</td>
						<td>${teamMember.email}</td>
						<td>${teamMember.level}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>