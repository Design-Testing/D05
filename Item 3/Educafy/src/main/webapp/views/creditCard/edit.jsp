<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="creditCard/${rol}/edit.do" method="POST"
	modelAttribute="creditCard">
		<form:hidden path="id" />
		<form:hidden path="version" />

		<form:label path="holderName">
			<spring:message code="creditCard.holderName" />:
		</form:label>
		<form:input path="holderName" />
		<form:errors cssClass="error" path="holderName" />
		<br />

		<form:label path="brandName">
			<spring:message code="creditCard.brandName" />: </form:label>
		<form:select id="brandNames" path="brandName">
			<form:options path="brandName" items="${brandNames}" />
		</form:select>

		<form:errors cssClass="error" path="brandName" />
		<br /> <br />

		<form:label path="number">
			<spring:message code="creditCard.number" />:
		</form:label>
		<form:input path="number" />
		<form:errors cssClass="error" path="number" />
		<br />

		<form:label path="expirationMonth">
			<spring:message code="creditCard.expirationMonth" />:
		</form:label>
		<form:input path="expirationMonth" />
		<form:errors cssClass="error" path="expirationMonth" />
		<br />

		<form:label path="expirationYear">
			<spring:message code="creditCard.expirationYear" />:
		</form:label>
		<form:input path="expirationYear" />
		<form:errors cssClass="error" path="expirationYear" />
		<br />

		<form:label path="cvv">
			<spring:message code="creditCard.cvv" />:
		</form:label>
		<form:input path="cvv" />
		<form:errors cssClass="error" path="cvv" />
		<br />

		<input type="submit" name="save"
			value="<spring:message code="creditCard.save" />" />


		<jstl:if test="${rol == 'customer' }">
			<input type="button" name="back"
				value="<spring:message code="creditCard.back"/>"
				onclick="javascript:relativeRedir('application/customer/list.do');" />
		</jstl:if>
		<jstl:if test="${rol == 'sponsor' }">

			<!-- IMPORTANTE @ALEJANDRO_UGARTE COMPLETA LA REDIRECCION DEL CANCEL correspondiente al sponsorship -->

			<input type="button" name="back"
				value="<spring:message code="creditCard.back"/>"
				onclick="javascript:relativeRedir('sponsorship/sponsor/list.do');" />
		</jstl:if>
</form:form>

