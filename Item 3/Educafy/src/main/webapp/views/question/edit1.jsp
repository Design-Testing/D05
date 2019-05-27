
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('TEACHER')">
<jstl:if test="${question.id eq 0 }">
<form:form action="question/edit.do?examId=${exam.id}" modelAttribute="question" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="answer"/>
	
	<acme:textarea code="question.title" path="title"/>
	
	<br>
	<input type="submit" name="create"
		value="<spring:message code="question.create" />" />
	<acme:button url="exam/display.do?examId=${exam.id}" name="back" code="question.back"/>
</form:form>
</jstl:if>

<jstl:if test="${question.id != 0}">
<form:form action="question/edit.do?examId=${exam.id }" modelAttribute="question" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="answer"/>
	
	<acme:textarea code="question.title" path="title"/>
	
	<br>
	<input type="submit" name="save"
		value="<spring:message code="question.save" />" />
	<acme:button url="exam/display.do?examId=${exam.id}" name="back" code="question.back"/>
</form:form>
</jstl:if>
</security:authorize>

<security:authorize access="hasRole('STUDENT')">
<form:form action="question/resolve.do?examId=${exam.id }" modelAttribute="question" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="title"/>
	<acme:display code="question.title" value="${question.title}"/>
	<br>
	<acme:textarea code="question.answer" path="answer"/>
	
	<br>
	<input type="submit" name="resolve"
		value="<spring:message code="question.save" />" />
	<acme:button url="exam/display.do?examId=${exam.id}" name="back" code="question.back"/>
</form:form>
</security:authorize>