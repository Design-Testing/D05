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

<acme:display code="certifier.name" value="${certifier.name}"/>
<spring:message code="certifier.photo"/>:<br>
<img src="${certifier.photo}" alt="<spring:message code="certifier.alt.image"/>" width="20%" height="20%"/>
<br>
<jstl:if test="${not empty certifier.surname}">
<jstl:forEach items="${certifier.surname}" var="df">
	<acme:display code="certifier.surname" value="${df}"/>
</jstl:forEach>
</jstl:if>
<acme:display code="certifier.email" value="${certifier.email}"/>
<acme:display code="certifier.phone" value="${certifier.phone}"/>
<acme:display code="certifier.address" value="${certifier.address}"/>
<acme:display code="certifier.vat" value="${certifier.vat}"/>

<br>

<acme:button url="curriculum/display.do?certifierId=${row.id}" name="display" code="certifier.curriculum"/>
<acme:button url="lesson/myLessons.do?certifierId=${row.id}" name="display" code="certifier.lessons"/>
<acme:button url="assesment/myAssesments.do?certifierId=${row.id}" name="display" code="certifier.assesments"/>
<acme:button url="comment/myComments.do?certifierId=${row.id}" name="display" code="certifier.comment"/>

<jstl:choose>
	<jstl:when test="${rol eq certifier}">
	</jstl:when>
	<jstl:otherwise>
	<acme:button url="certifier/list.do" name="back" code="certifier.back"/>
	</jstl:otherwise>
</jstl:choose>