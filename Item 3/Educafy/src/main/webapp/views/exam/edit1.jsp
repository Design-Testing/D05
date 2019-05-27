
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${exam.id eq 0 }">
<form:form action="exam/edit.do?reservationId=${reservation.id}" modelAttribute="exam" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="score"/>
	<form:hidden path="questions"/>
	<form:hidden path="status"/>
	<acme:textbox code="exam.title" path="title"/>
	<br>
	<input type="submit" name="create"
		value="<spring:message code="exam.create" />" />
	<acme:button url="reservation/teacher/display.do?reservationId=${reservation.id}" name="back" code="exam.back"/>
</form:form>
</jstl:if>

<jstl:if test="${exam.status eq 'PENDING' && exam.id != 0}">
<form:form action="exam/edit.do" modelAttribute="exam" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="score"/>
	<form:hidden path="questions"/>
	<form:hidden path="status"/>
	<acme:textbox code="exam.title" path="title"/>
	<br>
	<input type="submit" name="save"
		value="<spring:message code="exam.save" />" />
	<acme:button url="exam/display.do?examId=${exam.id}" name="back" code="exam.back"/>
</form:form>
</jstl:if>

<jstl:if test="${exam.status eq 'SUBMITTED' }">
<form:form action="exam/evaluated.do" modelAttribute="exam" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="title"/>
	<form:hidden path="questions"/>
	<form:hidden path="status"/>
	<acme:display code="exam.title" value="${exam.title}"/>
	<br>
	<acme:textbox code="exam.score" path="score"/>
	<br>
	<input type="submit" name="evaluate"
		value="<spring:message code="exam.evaluate" />" />
	<acme:button url="exam/display.do?examId=${exam.id}" name="back" code="exam.back"/>
</form:form>
</jstl:if>