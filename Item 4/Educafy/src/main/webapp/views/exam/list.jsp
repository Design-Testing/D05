
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jstl:choose>
	<jstl:when test="${not empty exam }">
	<display:table name="exam" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="title" titleKey="exam.title" />
		<display:column titleKey="exam.status" >
			<acme:statusChoose status="${row.status}"/>
		</display:column>
		
		<display:column titleKey="exam.score">
			<jstl:if test="${row.status eq 'EVALUATED' }">
				<jstl:out value="${row.score}"/>
			</jstl:if>
		</display:column>
		<security:authorize access="hasRole('STUDENT')">
		<display:column>
		<jstl:if test="${row.status eq 'INPROGRESS' }">
				<acme:button url="exam/display.do?examId=${row.id}" name="display" code="exam.inprogress"/>
		</jstl:if>
		</display:column>
		</security:authorize>
	</display:table>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="no.exams"/>
		<br><br>
	</jstl:otherwise>
</jstl:choose>