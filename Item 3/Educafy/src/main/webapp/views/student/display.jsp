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

<acme:display code="student.name" value="${student.name}"/>
<jstl:choose>
	<jstl:when test="${not empty student.photo}">
		<spring:message code="student.photo"/>:<br>
		<img src="${student.photo}" alt="<spring:message code="student.alt.image"/>" width="20%" height="20%"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="student.no.photo"/>
	</jstl:otherwise>
</jstl:choose>
<br>
<jstl:if test="${not empty student.surname}">
<jstl:forEach items="${student.surname}" var="df" varStatus="loop">
	<jstl:choose>
		<jstl:when test="${student.getSurname().size() gt 1}">
			<spring:message code="student.surname"/> <jstl:out value="${loop.index+1}"/>:
			<jstl:out value="${df}"/><br>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="student.surname"/>: <jstl:out value="${df}"/><br>
		</jstl:otherwise>
	</jstl:choose>
</jstl:forEach>
</jstl:if>
<acme:display code="student.email" value="${student.email}"/>
<acme:display code="student.phone" value="${student.phone}"/>
<acme:display code="student.address" value="${student.address}"/>
<acme:display code="student.vat" value="${student.vat}"/>

<br>

<acme:button url="assesment/myAssesments.do?studentId=${row.id}" name="display" code="student.assesments"/>
<acme:button url="comment/myComments.do?studentId=${row.id}" name="display" code="student.comment"/>

