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
	alert('<spring:message code="display.rooky.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="rooky.name"/> : <jstl:out value="${rooky.name}"/>', 10, 30)
		doc.text('<spring:message code="rooky.surname"/> : <jstl:out value="${rooky.surname}"/>', 10, 40)
	doc.text('<spring:message code="rooky.photo"/> : <jstl:out value="${rooky.photo}"/>', 10, 50)
	doc.text('<spring:message code="rooky.phone"/> : <jstl:out value="${rooky.phone}"/>', 10, 60)
	doc.text('<spring:message code="rooky.email"/> : <jstl:out value="${rooky.email}"/>', 10, 70)
	doc.text('<spring:message code="rooky.address"/> : <jstl:out value="${rooky.address}"/>', 10, 80)
	doc.text('<spring:message code="rooky.vat"/> : <jstl:out value="${rooky.vat}"/>', 10, 90)
	doc.text('', 10, 100)
	doc.text('<spring:message code="rooky.creditCard"/>', 15, 110)
	doc.text('<spring:message code="rooky.creditCard.holderName"/> : <jstl:out value="${rooky.creditCard.holderName}"/>', 10, 120)
	doc.text('<spring:message code="rooky.creditCard.number"/> : <jstl:out value="${rooky.creditCard.number}"/>', 10, 130)
	doc.text('<spring:message code="rooky.creditCard.make"/> : <jstl:out value="${rooky.creditCard.make}"/>', 10, 140)
	doc.text('<spring:message code="rooky.creditCard.expirationMonth"/> : <jstl:out value="${rooky.creditCard.expirationMonth}"/>', 10, 150)
	doc.text('<spring:message code="rooky.creditCard.expirationYear"/> : <jstl:out value="${rooky.creditCard.expirationYear}"/>', 10, 160)
	doc.text('<spring:message code="rooky.creditCard.cvv"/> : <jstl:out value="${rooky.creditCard.cvv}"/>', 10, 170)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "rooky/deletePersonalData.do";
	}
}
</script>

<acme:display code="rooky.name" value="${rooky.name}"/><br>
<jstl:if test="${not empty rooky.surname}">
<spring:message code="rooky.surname"/>:
<ul>
<jstl:forEach items="${rooky.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty rooky.photo}">
<spring:message code="rooky.photo"/>:<br>
<img src="${rooky.photo}" alt="<spring:message code="rooky.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="rooky.email" value="${rooky.email}"/><br>
<jstl:if test="${not empty rooky.phone}">
<acme:display code="rooky.phone" value="${rooky.phone}"/><br>
</jstl:if>
<jstl:if test="${not empty rooky.address}">
<acme:display code="rooky.address" value="${rooky.address}"/><br>
</jstl:if>
<acme:display code="rooky.vat" value="${rooky.vat}"/><br>
<h3><spring:message code="rooky.creditCard"/></h3>
<acme:display code="rooky.creditCard.holderName" value="${rooky.creditCard.holderName}"/>
<acme:display code="rooky.creditCard.number" value="${rooky.creditCard.number}"/>
<acme:display code="rooky.creditCard.make" value="${rooky.creditCard.make}"/>
<acme:display code="rooky.creditCard.expirationMonth" value="${rooky.creditCard.expirationMonth}"/>
<acme:display code="rooky.creditCard.expirationYear" value="${rooky.creditCard.expirationYear}"/>
<acme:display code="rooky.creditCard.cvv" value="${rooky.creditCard.cvv}"/>

<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>