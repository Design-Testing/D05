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

<script src="https://unpkg.com/jspdf@latest/dist/jspdf.min.js"></script>
<script src="https://unpkg.com/jspdf@latest/dist/jspdf.min.js"></script>

<script>
function generatePDF(){
	alert('<spring:message code="display.student.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="student.name"/> : <jstl:out value="${student.name}"/>', 10, 30)
	doc.text('<spring:message code="student.surname"/> : <jstl:out value="${student.surname}"/>', 10, 40)
	doc.text('<spring:message code="student.photo"/> : <jstl:out value="${student.photo}"/>', 10, 50)
	doc.text('<spring:message code="student.phone"/> : <jstl:out value="${student.phone}"/>', 10, 60)
	doc.text('<spring:message code="student.email"/> : <jstl:out value="${student.email}"/>', 10, 70)
	doc.text('<spring:message code="student.address"/> : <jstl:out value="${student.address}"/>', 10, 80)
	doc.text('', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "student/deletePersonalData.do";
	}
}
</script>


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
<jstl:if test="${not empty student.email}">
	<acme:display code="student.email" value="${student.email}"/>
</jstl:if>
<jstl:if test="${not empty student.phone}">
	<acme:display code="student.phone" value="${student.phone}"/>
</jstl:if>
<acme:display code="student.address" value="${student.address}"/>

<br>


<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>

<!-- 
<acme:button url="assesment/myAssesments.do?studentId=${row.id}" name="display" code="student.assesments"/>
<acme:button url="comment/myComments.do?studentId=${row.id}" name="display" code="student.comment"/>
 -->
