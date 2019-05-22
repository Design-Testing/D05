
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="comment/teacher/edit.do" modelAttribute="commentForm" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="assesmentId" value="${assesmentId}" />
	
	<acme:textarea code="comment.text" path="text"/>

<br>

	<input type="submit" name="save"
		value="<spring:message code="comment.submit" />" />
	
	<acme:button url="assesment/teacher/myAssesments.do" name="back" code="comment.back"/>

</form:form>