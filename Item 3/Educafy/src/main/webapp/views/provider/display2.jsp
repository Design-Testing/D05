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
	alert('<spring:message code="display.provider.document.alert"/>')
	var doc = new jsPDF()
	doc.text('<spring:message code="display.document.title"/>', 20, 10)
	doc.text('', 10, 20)
	doc.text('<spring:message code="provider.name"/> : <jstl:out value="${provider.name}"/>', 10, 30)
	doc.text('<spring:message code="provider.make"/> : <jstl:out value="${provider.make}"/>', 10, 40)
	doc.text('<spring:message code="provider.surname"/> : <jstl:out value="${provider.surname}"/>', 10, 50)
	doc.text('<spring:message code="provider.photo"/> : <jstl:out value="${provider.photo}"/>', 10, 60)
	doc.text('<spring:message code="provider.phone"/> : <jstl:out value="${provider.phone}"/>', 10, 70)
	doc.text('<spring:message code="provider.email"/> : <jstl:out value="${provider.email}"/>', 10, 80)
	doc.text('<spring:message code="provider.address"/> : <jstl:out value="${provider.address}"/>', 10, 90)
	doc.text('<spring:message code="provider.vat"/> : <jstl:out value="${provider.vat}"/>', 10, 100)
	doc.text('', 10, 100)
	doc.text('<spring:message code="provider.creditCard"/>', 15, 110)
	doc.text('<spring:message code="provider.creditCard.holderName"/> : <jstl:out value="${provider.creditCard.holderName}"/>', 10, 120)
	doc.text('<spring:message code="provider.creditCard.number"/> : <jstl:out value="${provider.creditCard.number}"/>', 10, 130)
	doc.text('<spring:message code="provider.creditCard.make"/> : <jstl:out value="${provider.creditCard.make}"/>', 10, 140)
	doc.text('<spring:message code="provider.creditCard.expirationMonth"/> : <jstl:out value="${provider.creditCard.expirationMonth}"/>', 10, 150)
	doc.text('<spring:message code="provider.creditCard.expirationYear"/> : <jstl:out value="${provider.creditCard.expirationYear}"/>', 10, 160)
	doc.text('<spring:message code="provider.creditCard.cvv"/> : <jstl:out value="${provider.creditCard.cvv}"/>', 10, 170)
	doc.save('<spring:message code="display.document.fileName"/>.pdf')
}
function deletePersonalData(){
	var r = confirm('<spring:message code="display.deletePersonalData"/>');
	if (r == true) {
		location.href = "provider/deletePersonalData.do";
	}
}
</script>


<acme:display code="provider.name" value="${provider.name}"/><br>

<jstl:if test="${not empty provider.surname}">
<spring:message code="provider.surname"/>
<ul>
<jstl:forEach items="${provider.surname}" var="df">
	<li><jstl:out value="${df}"/></li>
</jstl:forEach>
</ul>
</jstl:if>
<jstl:if test="${not empty provider.photo}">
<spring:message code="provider.photo"/>:<br>
<img src="${provider.photo}" alt="<spring:message code="provider.alt.image"/>" width="20%" height="20%"/>
<br><br></jstl:if>
<acme:display code="provider.email" value="${provider.email}"/><br>
<jstl:if test="${not empty provider.phone}">
<acme:display code="provider.phone" value="${provider.phone}"/><br>
</jstl:if>
<acme:display code="provider.make" value="${provider.make}"/><br>
<jstl:if test="${not empty provider.address}">
<acme:display code="provider.address" value="${provider.address}"/><br>
</jstl:if>
<acme:display code="provider.vat" value="${provider.vat}"/><br>

<h3><spring:message code="provider.creditCard"/></h3>
<acme:display code="provider.creditCard.holderName" value="${provider.creditCard.holderName}"/>
<acme:display code="provider.creditCard.number" value="${provider.creditCard.number}"/>
<acme:display code="provider.creditCard.make" value="${provider.creditCard.make}"/>
<acme:display code="provider.creditCard.expirationMonth" value="${provider.creditCard.expirationMonth}"/>
<acme:display code="provider.creditCard.expirationYear" value="${provider.creditCard.expirationYear}"/>
<acme:display code="provider.creditCard.cvv" value="${provider.creditCard.cvv}"/>

<jstl:if test="${displayButtons}">
<br>
	<button onClick="generatePDF()"><spring:message code="display.getData"/></button>
	<button onClick="deletePersonalData()"><spring:message code="display.button.deletePersonalData"/></button>
	
<br>
</jstl:if>