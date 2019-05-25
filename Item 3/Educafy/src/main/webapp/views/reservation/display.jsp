
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
img.resize {
  max-width:10%;
  max-height:10%;
}
</style>

<acme:display code="reservation.status" value="${reservation.status}"/>

<acme:display code="reservation.moment" value="${reservation.moment}"/>

<acme:display code="reservation.explanation" value="${reservation.explanation}"/>

<acme:display code="reservation.hoursWeek" value="${reservation.hoursWeek}"/>

<acme:display code="reservation.cost" value="${reservation.cost}"/>

<acme:display code="reservation.student" value="${reservation.student.name}"/>

<acme:display code="reservation.lesson" value="${reservation.lesson.title}"/>
<acme:button url="lesson/display.do?lessonId=${reservation.lesson.id}" name="displayStudent" code="reservation.display"/>

<acme:display code="reservation.creditCard" value="${reservation.creditCard.number}"/>


<br><br>


<jstl:choose>
	<jstl:when test="${rol eq 'teacher' }">
		<acme:button url="reservation/teacher/myReservations.do" name="back" code="reservation.back"/>
	</jstl:when>
	<jstl:when test="${rol eq 'student'}">
		<acme:button url="reservation/student/myReservations.do" name="back" code="reservation.back"/>
	</jstl:when>
</jstl:choose>
