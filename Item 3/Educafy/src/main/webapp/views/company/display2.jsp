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
	alert('<spring:message code="display.company.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="company.name"/> : <jstl:out value="${company.name}"/>', 10, 30)
	doc.text('<spring:message code="company.commercialName"/> : <jstl:out value="${company.commercialName}"/>', 10, 40)
	doc.text('<spring:message code="company.surname"/> : <jstl:out value="${company.surname}"/>', 10, 50)
	doc.text('<spring:message code="company.photo"/> : <jstl:out value="${company.photo}"/>', 10, 60)
	doc.text('<spring:message code="company.phone"/> : <jstl:out value="${company.phone}"/>', 10, 70)
	doc.text('<spring:message code="company.email"/> : <jstl:out value="${company.email}"/>', 10, 80)
	doc.text('<spring:message code="company.address"/> : <jstl:out value="${company.address}"/>', 10, 90)
	doc.text('<spring:message code="company.vat"/> : <jstl:out value="${company.vat}"/>', 10, 100)
	doc.text('', 10, 100)
	doc.text('<spring:message code="company.creditCard"/>', 15, 110)
	doc.text('<spring:message code="company.creditCard.holderName"/> : <jstl:out value="${company.creditCard.holderName}"/>', 10, 120)
	doc.text('<spring:message code="company.creditCard.number"/> : <jstl:out value="${company.creditCard.number}"/>', 10, 130)
	doc.text('<spring:message code="company.creditCard.make"/> : <jstl:out value="${company.creditCard.make}"/>', 10, 140)
	doc.text('<spring:message code="company.creditCard.expirationMonth"/> : <jstl:out value="${company.creditCard.expirationMonth}"/>', 10, 150)
	doc.text('<spring:message code="company.creditCard.expirationYear"/> : <jstl:out value="${company.creditCard.expirationYear}"/>', 10, 160)
	doc.text('<spring:message code="company.creditCard.cvv"/> : <jstl:out value="${company.creditCard.cvv}"/>', 10, 170)
	doc.text('<spring:message code="company.score"/> : <jstl:out value="${company.score}"/>', 10, 180)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "company/deletePersonalData.do";
	}
}
</script>


<acme:display code="company.name" value="${company.name}"/><br>

<jstl:if test="${not empty company.surname}">
<spring:message code="company.surname"/>
<ul>
<jstl:forEach items="${company.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty company.photo}">
<spring:message code="company.photo"/>:<br>
<img src="${company.photo}" alt="<spring:message code="company.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="company.email" value="${company.email}"/><br>
<jstl:if test="${not empty company.phone}">
<acme:display code="company.phone" value="${company.phone}"/><br>
</jstl:if>
<acme:display code="company.commercialName" value="${company.commercialName}"/><br>
<jstl:if test="${not empty company.address}">
<acme:display code="company.address" value="${company.address}"/><br>
</jstl:if>
<acme:display code="company.vat" value="${company.vat}"/><br>
<acme:display code="company.score" value="${company.score}"/>

<h3><spring:message code="company.creditCard"/></h3>
<acme:display code="company.creditCard.holderName" value="${company.creditCard.holderName}"/>
<acme:display code="company.creditCard.number" value="${company.creditCard.number}"/>
<acme:display code="company.creditCard.make" value="${company.creditCard.make}"/>
<acme:display code="company.creditCard.expirationMonth" value="${company.creditCard.expirationMonth}"/>
<acme:display code="company.creditCard.expirationYear" value="${company.creditCard.expirationYear}"/>
<acme:display code="company.creditCard.cvv" value="${company.creditCard.cvv}"/>

<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>