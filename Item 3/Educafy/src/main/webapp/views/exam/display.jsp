
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

<acme:display code="exam.title" value="${exam.title}"/>
<security:authorize access="hasRole('TEACHER')">
	<jstl:if test="${exam.status eq 'PENDING' }">
		<acme:button url="exam/edit.do?examId=${exam.id}" name="edit" code="exam.edit"/>
	</jstl:if>
<br><br>
</security:authorize>
<jstl:choose>
<jstl:when test="${not empty questions }">
<display:table name="questions" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="title" titleKey="question.title" />
		<security:authorize access="hasRole('TEACHER')">
			<display:column>
				<acme:button url="question/display.do?questionId=${row.id}&examId=${exam.id}" name="display" code="question.display"/>
			</display:column>
			<jstl:if test="${exam.status eq 'PENDING' }">
				<display:column>
					<acme:button url="question/edit.do?questionId=${row.id}&examId=${exam.id}" name="edit" code="question.edit"/>
				</display:column>
				<display:column>
					<acme:button url="question/delete.do?questionId=${row.id}&examId=${exam.id}" name="delete" code="question.delete"/>
				</display:column>
			</jstl:if>
		</security:authorize>
		<security:authorize access="hasRole('STUDENT')">
			<jstl:if test="${exam.status eq 'INPROGRESS' }">
				<display:column>
					<acme:button url="question/edit.do?questionId=${row.id}&examId=${exam.id}" name="edit" code="question.edit"/>
				</display:column>
			</jstl:if>
		</security:authorize>
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="no.questions"/>
</jstl:otherwise>
</jstl:choose>
<br><br>
<security:authorize access="hasRole('TEACHER')">
<jstl:if test="${exam.status eq 'PENDING' }">
	<acme:button url="question/create.do?examId=${exam.id}" name="create" code="question.create"/>
	<acme:button url="exam/delete.do?examId=${exam.id}" name="delete" code="exam.delete"/>
</jstl:if>
	<acme:button url="reservation/teacher/display.do?reservationId=${exam.reservation.id}" name="back" code="exam.back"/>
<br><br>
</security:authorize>

<security:authorize access="hasRole('STUDENT')">
	<acme:button url="exam/submitted.do?examId=${exam.id}" name="submitted" code="exam.submit"/>
	<br><br>
</security:authorize>
