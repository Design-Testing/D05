
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="reservation/student/edit.do" modelAttribute="reservation" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="cost"/>
	<form:hidden path="hoursWeek"/>
	<form:hidden path="student"/>
	<form:hidden path="lesson"/>
	<form:hidden path="exams"/>
	<form:hidden path="status"/>
	
	<security:authorize access="hasRole('STUDENT')">
	<jstl:choose>
		<jstl:when test="${reservation.status eq 'ACCEPTED' }">
			<acme:textarea code="reservation.explanation" path="explanation"/>
			<acme:button url="reservation/student/reviwing.do" name="reviewing" code="reservation.reviewing"/>
			<acme:button url="reservation/student/final.do" name="final" code="reservation.final"/>
		</jstl:when>
	</jstl:choose>
	</security:authorize>
	<security:authorize access="hasRole('TEACHER')">
	<jstl:choose>
		<jstl:when test="${reservation.status eq 'PENDING' }">
			<acme:textarea code="reservation.explanation" path="explanation"/>
			<acme:button url="reservation/teacher/accepted.do" name="accepted" code="reservation.accepted"/>
			<acme:button url="reservation/teacher/rejected.do" name="rejected" code="reservation.rejected"/>
		</jstl:when>
	</jstl:choose>
	</security:authorize>

<br>

	<input type="submit" name="save"
		value="<spring:message code="reservation.submit" />" />
	<security:authorize access="hasRole('TEACHER')">
		<acme:button url="reservation/teacher/myReservation.do" name="back" code="reservation.back"/>
	</security:authorize>
	<security:authorize access="hasRole('STUDENT')">
		<acme:button url="reservation/student/myReservation.do" name="back" code="reservation.back"/>
	</security:authorize>
</form:form>