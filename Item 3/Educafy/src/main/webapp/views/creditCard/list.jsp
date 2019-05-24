<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="myCards" id="row"
	requestURI="creditCard/${rol}/list.do" pagesize="5" class="displaytag">
	
	<display:column>
		<a href="creditCard/${rol}/edit.do?creditCardId=${row.id}">
			<spring:message code="creditCard.edit" />
		</a>
	</display:column>
	<display:column property="holderName" titleKey="creditCard.holderName" />
	<display:column property="brandName" titleKey="creditCard.brandName" />

	<display:column property="expirationMonth"
		titleKey="creditCard.expirationMonth" />

	<display:column property="expirationYear"
		titleKey="creditCard.expirationYear" />

	<display:column>
		<a href="creditCard/${rol}/display.do?creditCardId=${row.id}">
			<spring:message code="creditCard.display" />
		</a>
	</display:column>


</display:table>

<input type="button" name="create"
	value="<spring:message code="creditCard.create" />"
	onclick="javascript: relativeRedir('creditCard/${rol}/create.do');" />