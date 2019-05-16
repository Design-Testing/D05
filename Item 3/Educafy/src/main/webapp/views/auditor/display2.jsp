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
	alert('<spring:message code="display.auditor.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="auditor.name"/> : <jstl:out value="${auditor.name}"/>', 10, 30)
	doc.text('<spring:message code="auditor.surname"/> : <jstl:out value="${auditor.surname}"/>', 10, 50)
	doc.text('<spring:message code="auditor.photo"/> : <jstl:out value="${auditor.photo}"/>', 10, 60)
	doc.text('<spring:message code="auditor.phone"/> : <jstl:out value="${auditor.phone}"/>', 10, 70)
	doc.text('<spring:message code="auditor.email"/> : <jstl:out value="${auditor.email}"/>', 10, 80)
	doc.text('<spring:message code="auditor.address"/> : <jstl:out value="${auditor.address}"/>', 10, 90)
	doc.text('<spring:message code="auditor.vat"/> : <jstl:out value="${auditor.vat}"/>', 10, 100)
	doc.text('', 10, 100)
	doc.text('<spring:message code="auditor.creditCard"/>', 15, 110)
	doc.text('<spring:message code="auditor.creditCard.holderName"/> : <jstl:out value="${auditor.creditCard.holderName}"/>', 10, 120)
	doc.text('<spring:message code="auditor.creditCard.number"/> : <jstl:out value="${auditor.creditCard.number}"/>', 10, 130)
	doc.text('<spring:message code="auditor.creditCard.make"/> : <jstl:out value="${auditor.creditCard.make}"/>', 10, 140)
	doc.text('<spring:message code="auditor.creditCard.expirationMonth"/> : <jstl:out value="${auditor.creditCard.expirationMonth}"/>', 10, 150)
	doc.text('<spring:message code="auditor.creditCard.expirationYear"/> : <jstl:out value="${auditor.creditCard.expirationYear}"/>', 10, 160)
	doc.text('<spring:message code="auditor.creditCard.cvv"/> : <jstl:out value="${auditor.creditCard.cvv}"/>', 10, 170)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "auditor/deletePersonalData.do";
	}
}
</script>


<acme:display code="auditor.name" value="${auditor.name}"/><br>

<jstl:if test="${not empty auditor.surname}">
<spring:message code="auditor.surname"/>
<ul>
<jstl:forEach items="${auditor.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty auditor.photo}">
<spring:message code="auditor.photo"/>:<br>
<img src="${auditor.photo}" alt="<spring:message code="auditor.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="auditor.email" value="${auditor.email}"/><br>
<jstl:if test="${not empty auditor.phone}">
<acme:display code="auditor.phone" value="${auditor.phone}"/><br>
</jstl:if>
<jstl:if test="${not empty auditor.address}">
<acme:display code="auditor.address" value="${auditor.address}"/><br>
</jstl:if>
<acme:display code="auditor.vat" value="${auditor.vat}"/><br>

<h3><spring:message code="auditor.creditCard"/></h3>
<acme:display code="auditor.creditCard.holderName" value="${auditor.creditCard.holderName}"/>
<acme:display code="auditor.creditCard.number" value="${auditor.creditCard.number}"/>
<acme:display code="auditor.creditCard.make" value="${auditor.creditCard.make}"/>
<acme:display code="auditor.creditCard.expirationMonth" value="${auditor.creditCard.expirationMonth}"/>
<acme:display code="auditor.creditCard.expirationYear" value="${auditor.creditCard.expirationYear}"/>
<acme:display code="auditor.creditCard.cvv" value="${auditor.creditCard.cvv}"/>

<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>