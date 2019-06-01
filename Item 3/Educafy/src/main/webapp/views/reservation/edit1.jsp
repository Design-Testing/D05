
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
	<form:hidden path="student"/>
	<form:hidden path="lesson"/>
	<form:hidden path="status"/>
	
	<acme:textbox code="reservation.hoursWeek" path="hoursWeek"/>
	<br>
	<form:label path="creditCard">
                    <spring:message code="reservation.creditCard" />:
            </form:label>
                <form:select id="cards" path="creditCard">
                    <jstl:forEach var="card" items="${myCards}">
                        <form:option value="${card.id}">
                            <jstl:out value='${card.make} - number: ${card.number}' />
                        </form:option>
                    </jstl:forEach>
                </form:select>
                <form:errors cssClass="error" path="creditCard" />
                <br />
                <br />

	<input type="submit" name="save"
		value="<spring:message code="reservation.submit" />" />
	
	<security:authorize access="hasRole('TEACHER')">
		<acme:button url="reservation/teacher/myReservations.do" name="back" code="reservation.back"/>
	</security:authorize>
	<security:authorize access="hasRole('STUDENT')">
	<jstl:choose>
	<jstl:when test="${not empty subjectId}">
		<acme:button url="subject/display.do?subjectId=${subjectId}" name="back" code="reservation.back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="subject/list.do" name="back" code="reservation.subject.list"/>
	</jstl:otherwise>
	</jstl:choose>
	</security:authorize>

</form:form>