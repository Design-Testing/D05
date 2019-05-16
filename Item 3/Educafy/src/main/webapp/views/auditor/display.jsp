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

<acme:display code="auditor.name" value="${auditor.name}"/>
<spring:message code="auditor.photo"/>:<br>
<img src="${auditor.photo}" alt="<spring:message code="auditor.alt.image"/>" width="20%" height="20%"/>
<br>
<jstl:if test="${not empty auditor.surname}">
<jstl:forEach items="${auditor.surname}" var="df">
	<acme:display code="auditor.surname" value="${df}"/>
</jstl:forEach>
</jstl:if>
<acme:display code="auditor.email" value="${auditor.email}"/>
<acme:display code="auditor.phone" value="${auditor.phone}"/>
<acme:display code="auditor.address" value="${auditor.address}"/>
<acme:display code="auditor.vat" value="${auditor.vat}"/>

<br>
