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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:choose>
<jstl:when test="${not empty errortrace}">
	<h2>You cannot rebrand system</h2>
	<h3><spring:message code="${errortrace}"/></h3>
</jstl:when>
<jstl:otherwise>
<jstl:set var="newSysName"/>

<form action="configurationParameters/administrator/rebranding.do?newSysName=${newSysName}" method="GET">
	<label for="newSysName"><spring:message code="configurationParameters.sysName" />:</label>
	<input type="text" id="newSysName" name="newSysName"/><br>
	<br><br>
	
	<input type="submit" value="<spring:message code="configurationParameters.rebrand" />" />
	<br>
</form>

</jstl:otherwise>
</jstl:choose>