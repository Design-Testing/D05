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
<spring:message code="student.photo"/>:<br>
<img src="${student.photo}" alt="<spring:message code="student.alt.image"/>" width="20%" height="20%"/>
<br>
<jstl:if test="${not empty student.surname}">
<jstl:forEach items="${student.surname}" var="df">
	<acme:display code="student.surname" value="${df}"/>
</jstl:forEach>
</jstl:if>
<acme:display code="student.email" value="${student.email}"/>
<acme:display code="student.phone" value="${student.phone}"/>
<acme:display code="student.address" value="${student.address}"/>
<acme:display code="student.vat" value="${student.vat}"/>
<acme:display code="student.score" value="${student.score}"/>

<br>

<acme:button url="curriculum/display.do?studentId=${row.id}" name="display" code="student.curriculum"/>
<acme:button url="lesson/myLessons.do?studentId=${row.id}" name="display" code="student.lessons"/>
<acme:button url="assesment/myAssesments.do?studentId=${row.id}" name="display" code="student.assesments"/>
<acme:button url="comment/myComments.do?studentId=${row.id}" name="display" code="student.comment"/>

