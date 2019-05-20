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
<spring:message code="teacher.photo"/>:<br>
<img src="${teacher.photo}" alt="<spring:message code="teacher.alt.image"/>" width="20%" height="20%"/>
<br>
<jstl:if test="${not empty teacher.surname}">
<jstl:forEach items="${teacher.surname}" var="df">
	<acme:display code="teacher.surname" value="${df}"/>
</jstl:forEach>
</jstl:if>
<acme:display code="teacher.email" value="${teacher.email}"/>
<acme:display code="teacher.phone" value="${teacher.phone}"/>
<acme:display code="teacher.commercialName" value="${teacher.commercialName}"/>
<acme:display code="teacher.address" value="${teacher.address}"/>
<acme:display code="teacher.vat" value="${teacher.vat}"/>
<acme:display code="teacher.score" value="${teacher.score}"/>

<br>
<security:authorize access="hasRole('HACKER')">
	<jstl:set var="hk" value="1"/>
</security:authorize>
<jstl:choose>
	<jstl:when test="${hk eq 1}">
		<acme:button url="lesson/teacher/list.do" name="back" code="back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="lesson/list.do" name="listPositions" code="back"/>
	</jstl:otherwise>
</jstl:choose>
<input type="button" name="teacherList" value="<spring:message code="teacherList" /> ${teacher.commercialName}" onclick="javascript: relativeRedir('position/teacherList.do?companyId=${company.id}');" />