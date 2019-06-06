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
	alert('<spring:message code="display.teacher.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="teacher.name"/> : <jstl:out value="${teacher.name}"/>', 10, 30)
	doc.text('<spring:message code="teacher.surname"/> : <jstl:out value="${teacher.surname}"/>', 10, 50)
	doc.text('<spring:message code="teacher.photo"/> : <jstl:out value="${teacher.photo}"/>', 10, 60)
	doc.text('<spring:message code="teacher.phone"/> : <jstl:out value="${teacher.phone}"/>', 10, 70)
	doc.text('<spring:message code="teacher.email"/> : <jstl:out value="${teacher.email}"/>', 10, 80)
	doc.text('<spring:message code="teacher.address"/> : <jstl:out value="${teacher.address}"/>', 10, 90)
	doc.text('<spring:message code="teacher.vat"/> : <jstl:out value="${teacher.vat}"/>', 10, 100)
	doc.text('', 10, 100)
	doc.text('<spring:message code="teacher.score"/> : <jstl:out value="${teacher.score}"/>', 10, 180)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "teacher/deletePersonalData.do";
	}
}
</script>


<acme:display code="teacher.name" value="${teacher.name}"/><br>

<jstl:if test="${not empty teacher.surname}">
<spring:message code="teacher.surname"/>
<ul>
<jstl:forEach items="${teacher.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty teacher.photo}">
<spring:message code="teacher.photo"/>:<br>
<img src="${teacher.photo}" alt="<spring:message code="teacher.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="teacher.email" value="${teacher.email}"/><br>
<jstl:if test="${not empty teacher.phone}">
<acme:display code="teacher.phone" value="${teacher.phone}"/><br>
</jstl:if>
<jstl:if test="${not empty teacher.address}">
<acme:display code="teacher.address" value="${teacher.address}"/><br>
</jstl:if>
<acme:display code="teacher.vat" value="${teacher.vat}"/><br>
<acme:display code="teacher.score" value="${teacher.score}"/>


<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>