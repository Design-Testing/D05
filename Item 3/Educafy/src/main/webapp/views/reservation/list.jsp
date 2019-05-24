<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>


<display:table name="reservation" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">

	<display:column property="lesson.title" titleKey="reservation.lesson" />
	
	<display:column property="status" titleKey="reservation.status" />
	
	<display:column property="student.name" titleKey="reservation.student" />

	<display:column property="hoursWeek" titleKey="reservation.hoursWeek" />
	
	<display:column>
		<acme:button url="reservation/student/display.do?reservationId=${row.id}" name="display" code="reservation.display"/>
	</display:column>
	<security:authorize access="hasRole('STUDENT')">
	<display:column>
		<acme:button url="reservation/student/edit.do?reservationId=${row.id}" name="edit" code="reservation.edit"/>
		<jstl:if test="${reservation.status eq 'FINAL'}">
			<acme:button url="reservation/student/delete.do?reservationId=${row.id}" name="delete" code="reservation.delete"/>
		</jstl:if>
		
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('TEACHER')">
	<display:column>
		<acme:button url="reservation/teacher/edit.do?reservationId=${row.id}" name="edit" code="reservation.edit"/>
	</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${not empty msg}">
	<h3 style="color: red;"><spring:message code="${msg}"/></h3>
</jstl:if>

