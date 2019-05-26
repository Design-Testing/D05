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

<acme:display code="teacher.name" value="${teacher.name}"/>
<jstl:choose>
	<jstl:when test="${not empty teacher.photo}">
		<spring:message code="teacher.photo"/>:<br>
		<img src="${teacher.photo}" alt="<spring:message code="teacher.alt.image"/>" width="20%" height="20%"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="teacher.no.photo"/>
	</jstl:otherwise>
</jstl:choose>
<br>
<jstl:if test="${not empty teacher.surname}">
<jstl:forEach items="${teacher.surname}" var="df" varStatus="loop">
	<jstl:choose>
		<jstl:when test="${teacher.getSurname().size() gt 1}">
			<spring:message code="teacher.surname"/> <jstl:out value="${loop.index+1}"/>:
			<jstl:out value="${df}"/><br>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="teacher.surname"/>: <jstl:out value="${df}"/><br>
		</jstl:otherwise>
	</jstl:choose>
</jstl:forEach>
</jstl:if>

<acme:display code="teacher.email" value="${teacher.email}"/>
<jstl:if test="${not empty teacher.phone}">
<acme:display code="teacher.phone" value="${teacher.phone}"/>
</jstl:if>
<jstl:if test="${not empty teacher.address}">
<acme:display code="teacher.address" value="${teacher.address}"/>
</jstl:if>


<br>

<acme:button url="curriculum/display.do?teacherId=${row.id}" name="display" code="teacher.curriculum"/>
<acme:button url="lesson/myLessons.do?teacherId=${row.id}" name="display" code="teacher.lessons"/>
<acme:button url="assesment/myAssesments.do?teacherId=${row.id}" name="display" code="teacher.assesments"/>
<acme:button url="comment/myComments.do?teacherId=${row.id}" name="display" code="teacher.comment"/>

<jstl:choose>
	<jstl:when test="${rol eq teacher}">
	</jstl:when>
	<jstl:otherwise>
	<acme:button url="teacher/list.do" name="back" code="teacher.back"/>
	</jstl:otherwise>
</jstl:choose>