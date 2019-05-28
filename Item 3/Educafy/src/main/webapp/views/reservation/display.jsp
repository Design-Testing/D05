
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


<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="reservation.moment"/>: <fmt:formatDate value="${reservation.moment}" type="both" pattern="yyyy-MM-dd HH:mm"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="reservation.moment"/>: <fmt:formatDate value="${reservation.moment}" type="both" pattern="dd-MM-yyyy HH:mm"/>
	</jstl:otherwise>
</jstl:choose>
<br>

<acme:display code="reservation.explanation" value="${reservation.explanation}"/>

<acme:display code="reservation.hoursWeek" value="${reservation.hoursWeek}"/>

<acme:display code="reservation.cost" value="${reservation.cost}"/>

<acme:display code="reservation.student" value="${reservation.student.name}"/>

<acme:display code="reservation.lesson" value="${reservation.lesson.title}"/>
<acme:button url="lesson/display.do?lessonId=${reservation.lesson.id}" name="displayStudent" code="reservation.display"/>
<br>
<acme:display code="reservation.creditCard" value="${reservation.creditCard.number}"/>

<h3><spring:message code="reservation.timePeriod"/></h3>

<security:authorize access="hasRole('TEACHER')">
	<jstl:if test="${reservation.status eq 'PENDING' && empty periods}">
	<acme:button url="reservation/teacher/suggest.do?reservationId=${reservation.id}" name="suggest" code="reservation.suggest.timePeriods"/>
	</jstl:if>
</security:authorize>

<display:table name="periods"  id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="startHour" titleKey="timePeriod.startHour" />
		<display:column property="endHour" titleKey="timePeriod.endHour" />
		<display:column titleKey="timePeriod.day">
			 <jstl:choose>
				<jstl:when test="${row.dayNumber eq 1}"><spring:message code="monday"/></jstl:when>
				<jstl:when test="${row.dayNumber eq 2}"><spring:message code="tuesday"/></jstl:when>
				<jstl:when test="${row.dayNumber eq 3}"><spring:message code="wednesday"/></jstl:when>
				<jstl:when test="${row.dayNumber eq 4}"><spring:message code="thursday"/></jstl:when>
				<jstl:otherwise><spring:message code="friday"/></jstl:otherwise>				
			</jstl:choose> 
		</display:column>
		<jstl:if test="${rol eq 'teacher' && reservation.status eq 'PENDING'}">
			<display:column>
				<acme:button url="timePeriod/edit.do?timePeriodId=${row.id}" name="edit" code="reservation.edit"/>
			</display:column>
		</jstl:if>
</display:table>

<jstl:if test="${not empty error}">
			<h4 style="color: red;"><jstl:out value="${error.message}" /></h4>
</jstl:if>

<br><br>
<h3><spring:message code="reservation.exams"/></h3>
	<display:table name="exams" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="title" titleKey="exam.title" />
		<display:column property="status" titleKey="exam.status" />
		
		<display:column titleKey="exam.score">
			<jstl:if test="${row.status eq 'EVALUATED' }">
				<jstl:out value="${row.score}"/>
			</jstl:if>
		</display:column>		
		
		<security:authorize access="hasRole('TEACHER')">
			<display:column>
				<acme:button url="exam/display.do?examId=${row.id}" name="display" code="exam.display"/>
			</display:column>
			<display:column>
				<jstl:if test="${row.status eq 'SUBMITTED' }">
					<acme:button url="exam/edit.do?examId=${row.id}" name="evaluate" code="exam.evaluate"/>
				</jstl:if>
				<jstl:if test="${row.status eq 'PENDING' }">
					<acme:button url="exam/inprogress.do?examId=${row.id}" name="inprogress" code="exam.inprogress"/>
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.status eq 'PENDING' }">
					<acme:button url="exam/delete.do?examId=${row.id}" name="delete" code="exam.delete"/>
				</jstl:if>
			</display:column>
		</security:authorize>
		<security:authorize access="hasRole('STUDENT')">
		<jstl:if test="${reservation.status eq 'FINAL' && row.status eq 'INPROGRESS' }">
			<display:column>
				
					<acme:button url="exam/display.do?examId=${row.id}" name="display" code="exam.inprogress"/>
				
			</display:column>
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
