<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="creditCard/${rol}/edit.do" method="POST" modelAttribute="creditCard">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox path="holderName" code="creditCard.holderName"/>

	<form:label path="make">
		<spring:message code="creditCard.brandName" />: </form:label>
	<form:select id="brandNames" path="make">
		<form:options path="make" items="${brandNames}" />
	</form:select>
	<form:errors cssClass="error" path="make" />
	<br />
	<br />

	<acme:textbox path="number" code="creditCard.number"/>
	<acme:textbox code="creditCard.expirationMonth" path="expirationMonth" />
	<acme:textbox code="creditCard.expirationYear" path="expirationYear" />
	<acme:numberbox code="creditCard.cvv" path="cvv" min="100" max="999"/>
	
	<input type="submit" name="save"
		value="<spring:message code="creditCard.save" />" />


	<input type="button" name="back"
		value="<spring:message code="creditCard.back"/>"
		onclick="javascript:relativeRedir('creditCard/student/list.do');" />

</form:form>

