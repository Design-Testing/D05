
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
	
	<acme:numberbox code="assesment.score" path="score" min="0" max="5"/>
	<acme:textarea code="assesment.comment" path="comment"/>

<br>

	<input type="submit" name="save"
		value="<spring:message code="assesment.submit" />" />
	
	<acme:button url="lesson/display.do?lessonId=${lessonId}" name="back" code="assesment.back"/>

</form:form>