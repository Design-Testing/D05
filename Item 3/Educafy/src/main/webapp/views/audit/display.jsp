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

<acme:display code="audit.position" value="${audit.position.title}"/>

<acme:display code="audit.auditor" value="${audit.auditor.name}"/>

<acme:display code="audit.text" value="${audit.text}"/>

<acme:display code="audit.score" value="${audit.score}"/>



<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="audit.moment" />: <fmt:formatDate
			value="${audit.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="audit.moment" />: <fmt:formatDate
			value="${audit.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>


<br><br>

<input type="button" name="dsiplay"
                value="<spring:message code="audit.position.display" />"
                onclick="relativeRedir('position/display.do?positionId=${audit.position.id}')" />


<br><br>


<security:authorize access="hasRole('AUDITOR')">
	<jstl:if test="${audit.isDraft eq true }">
            <jstl:set var="toListDraft" value="1"/>
	</jstl:if>	
	<jstl:if test="${audit.isDraft eq false }">
            <jstl:set var="toListFinal" value="1"/>
	</jstl:if>
</security:authorize>

	
<jstl:choose>
	<jstl:when test="${toListDraft eq 1}">
		<acme:button url="audit/auditor/listDraft.do" name="back"
		code="audit.back" />
	</jstl:when>
	<jstl:when test="${toListFinal eq 1}">
		<acme:button url="audit/auditor/listFinal.do" name="back"
		code="audit.back" />
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="position/list.do" name="back" 
		code="audit.back"/>
	</jstl:otherwise>
</jstl:choose>


