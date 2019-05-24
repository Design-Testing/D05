<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="creditCard.holderName" />
:
<jstl:out value="${creditCard.holderName}" />
<br />

<spring:message code="creditCard.brandName" />
:
<jstl:out value="${creditCard.make}" />
<br />

<spring:message code="creditCard.number" />
:
<jstl:out value="${creditCard.number}" />
<br />

<spring:message code="creditCard.expirationMonth" />
:
<jstl:out value="${creditCard.expirationMonth}" />
<br />

<spring:message code="creditCard.expirationYear" />
:
<jstl:out value="${creditCard.expirationYear}" />
<br />

<spring:message code="creditCard.cvv" />
:
<jstl:out value="${creditCard.cvv}" />
<br />

<input type="button" name="back"
	value="<spring:message code="creditCard.back"/>"
	onclick="javascript:relativeRedir('creditCard/student/list.do');" />
