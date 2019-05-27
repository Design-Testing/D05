
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
<br>
<acme:display code="reservation.creditCard" value="${reservation.creditCard.number}"/>

<display:table name="periods" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="startHour" titleKey="timePeriod.startHour" />
		<display:column property="endHour" titleKey="timePeriod.endHour" />
		<display:column property="dayNumber" titleKey="timePeriod.day">
			 <jstl:choose>
				<jstl:when test="${row.dayNumber eq 1}"><jstl:out value="Monday"></jstl:out></jstl:when>
				<jstl:when test="${row.dayNumber eq 2}"><jstl:out value="Tuesday"/></jstl:when>
				<jstl:when test="${row.dayNumber eq 3}"><jstl:out value="Wednesday"/></jstl:when>
				<jstl:when test="${row.dayNumber eq 4}"><jstl:out value="Thursday"/></jstl:when>
				<jstl:otherwise><jstl:out value="Friday"/></jstl:otherwise>				
			</jstl:choose> 
		</display:column>
		<jstl:if test="${rol eq 'teacher' }">
			<display:column>
				<acme:button url="timePeriod/teacher/edit.do?timePeriodId=${row.id}" name="edit" code="reservation.edit"/>
			</display:column>
		</jstl:if>
</display:table>
<jstl:if test="${rol eq 'teacher' }">
	<acme:button url="timePeriod/teacher/create.do?reservationId=${reservation.id}" name="create" code="timePeriod.create"/>
</jstl:if>
<br><br>

	<display:table name="exams" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="title" titleKey="exam.title" />
		<display:column property="status" titleKey="exam.status" />
		<jstl:if test="${row.status eq 'EVALUATED' }">
			<display:column property="score" titleKey="exam.score" />		
		</jstl:if>
		<security:authorize access="hasRole('TEACHER')">
			<display:column>
				<acme:button url="exam/display.do?examId=${row.id}" name="display" code="exam.display"/>
			</display:column>
			<jstl:if test="${row.status eq 'SUBMITTED' }">
				<display:column>
					<acme:button url="exam/edit.do?examId=${row.id}" name="evaluate" code="exam.evaluate"/>
				</display:column>
			</jstl:if>
			<jstl:if test="${row.status eq 'PENDING' }">
				<display:column>
					<acme:button url="exam/delete.do?examId=${row.id}" name="delete" code="exam.delete"/>
				</display:column>
			</jstl:if>
		</security:authorize>
		<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${reservation.status eq 'FINAL' }">
			<jstl:if test="${row.status eq 'PENDING' }">
				<display:column>
					<acme:button url="exam/display.do?examId=${row.id}" name="display" code="exam.inProgress"/>
				</display:column>
			</jstl:if>
		</jstl:if>
		</security:authorize>
	</display:table>

<jstl:if test="${rol eq 'teacher' }">
	<acme:button url="exam/create.do?reservationId=${reservation.id}" name="create" code="exam.create"/>
</jstl:if>
<br><br>


<jstl:choose>
	<jstl:when test="${rol eq 'teacher' }">
		<acme:button url="reservation/teacher/myReservations.do" name="back" code="reservation.back"/>
	</jstl:when>
	<jstl:when test="${rol eq 'student'}">
		<acme:button url="reservation/student/myReservations.do" name="back" code="reservation.back"/>
	</jstl:when>
</jstl:choose>
