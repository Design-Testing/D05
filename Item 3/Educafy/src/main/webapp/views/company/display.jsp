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

<acme:display code="company.name" value="${company.name}"/>
<spring:message code="company.photo"/>:<br>
<img src="${company.photo}" alt="<spring:message code="company.alt.image"/>" width="20%" height="20%"/>
<br>
<jstl:if test="${not empty company.surname}">
<jstl:forEach items="${company.surname}" var="df">
	<acme:display code="company.surname" value="${df}"/>
</jstl:forEach>
</jstl:if>
<acme:display code="company.email" value="${company.email}"/>
<acme:display code="company.phone" value="${company.phone}"/>
<acme:display code="company.commercialName" value="${company.commercialName}"/>
<acme:display code="company.address" value="${company.address}"/>
<acme:display code="company.vat" value="${company.vat}"/>
<acme:display code="company.score" value="${company.score}"/>

<br>
<security:authorize access="hasRole('HACKER')">
	<jstl:set var="hk" value="1"/>
</security:authorize>
<jstl:choose>
	<jstl:when test="${hk eq 1}">
		<acme:button url="position/hacker/list.do" name="back" code="back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="position/list.do" name="listPositions" code="back"/>
	</jstl:otherwise>
</jstl:choose>
<input type="button" name="companyList" value="<spring:message code="companyList" /> ${company.commercialName}" onclick="javascript: relativeRedir('position/companyList.do?companyId=${company.id}');" />