<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<acme:display code="application.position" value="${application.position.title}"/>

<acme:display code="application.rooky" value="${application.rooky.name}"/>

<acme:display code="application.problem" value="${application.problem.title}"/>

<jstl:if test="${not empty application.explanation and not empty application.link}">
	<acme:display code="application.explanation" value="${application.explanation}"/>
	<acme:display code="application.link" value="${application.link}"/>
</jstl:if>

<spring:message code="application.status" />: <acme:statusChoose status="${application.status}"/>
<br>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="application.moment" />: <fmt:formatDate
			value="${application.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="application.moment" />: <fmt:formatDate
			value="${application.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>
<jstl:if test="${not empty application.submitMoment}">
<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="application.submitMoment" />: <fmt:formatDate
			value="${application.submitMoment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="application.submitMoment" />: <fmt:formatDate
			value="${application.submitMoment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>
</jstl:if>
<br>
<input type="button" name="dsiplay"
                value="<spring:message code="application.curricula.display" />"
                onclick="relativeRedir('curricula/display.do?curriculaId=${application.curricula.id}')" />
<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>

<br><br>

<security:authorize access="hasRole('ROOKY')">

<jstl:if test="${application.status eq 'PENDING'}">
<acme:button url="application${rolURL}/listPending.do" name="back"
	code="application.list.button" />
</jstl:if>
</security:authorize>

<security:authorize access="hasRole('COMPANY')">

<jstl:if test="${application.status eq 'PENDING'}">
<acme:button url="application${rolURL}/list.do" name="back"
	code="application.list.button" />
</jstl:if>
</security:authorize>

<security:authorize access="hasAnyRole('COMPANY','ROOKY')">

<jstl:if test="${application.status eq 'ACCEPTED'}">
<acme:button url="application${rolURL}/listAccepted.do" name="back"
	code="application.list.button" />
</jstl:if>

<jstl:if test="${application.status eq 'REJECTED'}">
<acme:button url="application${rolURL}/listRejected.do" name="back"
	code="application.list.button" />
</jstl:if>

<jstl:if test="${application.status eq 'SUBMITTED'}">
<acme:button url="application${rolURL}/listSubmitted.do" name="back"
	code="application.list.button" />
</jstl:if>
</security:authorize>



