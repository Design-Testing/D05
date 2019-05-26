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
	alert('<spring:message code="display.certifier.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="certifier.name"/> : <jstl:out value="${certifier.name}"/>', 10, 30)
	doc.text('<spring:message code="certifier.surname"/> : <jstl:out value="${certifier.surname}"/>', 10, 40)
	doc.text('<spring:message code="certifier.photo"/> : <jstl:out value="${certifier.photo}"/>', 10, 50)
	doc.text('<spring:message code="certifier.phone"/> : <jstl:out value="${certifier.phone}"/>', 10, 60)
	doc.text('<spring:message code="certifier.email"/> : <jstl:out value="${certifier.email}"/>', 10, 70)
	doc.text('<spring:message code="certifier.address"/> : <jstl:out value="${certifier.address}"/>', 10, 80)
	doc.text('', 10, 90)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "certifier/deletePersonalData.do";
	}
}
</script>


<acme:display code="certifier.name" value="${certifier.name}"/><br>

<jstl:if test="${not empty certifier.surname}">
<spring:message code="certifier.surname"/>
<ul>
<jstl:forEach items="${certifier.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty certifier.photo}">
<spring:message code="certifier.photo"/>:<br>
<img src="${certifier.photo}" alt="<spring:message code="certifier.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="certifier.email" value="${certifier.email}"/><br>
<jstl:if test="${not empty certifier.phone}">
<acme:display code="certifier.phone" value="${certifier.phone}"/><br>
</jstl:if>
<jstl:if test="${not empty certifier.address}">
<acme:display code="certifier.address" value="${certifier.address}"/><br>
</jstl:if>

<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>