
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="assesment/student/edit.do" modelAttribute="assesmentForm" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="lessonId" value="${lessonId}" />
	
	<acme:textbox code="assesment.score" path="score"/>
	<acme:textarea code="assesment.comment" path="comment"/>

<br>

	<input type="submit" name="save"
		value="<spring:message code="assesment.submit" />" />
	
	<acme:button url="assesment/student/myAssesments.do" name="back" code="assesment.back"/>

</form:form>