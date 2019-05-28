
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="lesson/teacher/edit.do" modelAttribute="lessonForm" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="subjectId" value="${subjectId}" />
	
	
	<acme:textbox code="lesson.title" path="title"/>
	<acme:textarea code="lesson.description" path="description"/>
	<acme:textbox code="lesson.price" path="price"/>
	

<br>

	<input type="submit" name="save"
		value="<spring:message code="lesson.submit" />" />
	
	<jstl:choose>
	<jstl:when test="${lessonForm.id eq 0}">
		<acme:button url="subject/display.do?subjectId=${subjectId}" name="back" code="lesson.back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="lesson/teacher/myLessons.do" name="back" code="lesson.back"/>
	</jstl:otherwise>
	</jstl:choose>
</form:form>