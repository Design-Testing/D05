
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<script>
	function phoneFun() {
		var x = document.getElementById("phone");
		var telefono = x.value;
		var CCACPN = new RegExp("(^\\+([1-9]{1}[0-9]{0,2})){1}\\s*(\\([1-9]{1}[0-9]{0,2}\\)){1}\\s*(\\d{4,}$)"); /* +CC (AC) PN */
		var CCPN = new RegExp("(^\\+([1-9]{1}[0-9]{0,2})){1}\\s*(\\d{4,}$)"); /* +CC PN */
		var PN = new RegExp("(^\\d{4,}$)"); /* PN */
		if (('${phone}' != telefono) && !CCACPN.test(telefono) && !CCPN.test(telefono)) {
			if (PN.test(telefono)) {
				x.value = '${countryPhoneCode}' + " " + telefono;
			} else {
				var mensaje = confirm("<spring:message code="phone.error"/>");
				if (!mensaje) {
					x.value = '${phone}';
				}
			}
		}
	}
	</script>

<jstl:if test="${not empty alert}">
	<script>
	 $(document).ready(function() {
		 alert('<spring:message code="${alert}"/>');
	    });
		
	</script>
</jstl:if>

	<jstl:if test="${not empty errors}">
		<div class="errorDiv">
			<p id="mensajeError"><spring:message code="provider.error.message"/></p>
			<ul>
				<jstl:forEach items="${errors}" var="error">
					<jstl:if test="${error.field eq 'termsAndCondicions'}">
						<li><spring:message code="provider.edit.${error.field}"/> - <jstl:out value="${error.defaultMessage}" /></li>
					</jstl:if>
				</jstl:forEach>
			</ul>
		</div>
	</jstl:if>
	<br>

<form:form modelAttribute="providerForm" action="provider/edit.do" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<acme:textbox code="provider.edit.userAccountuser" path="userAccountuser" />
	<acme:password code="provider.edit.userAccountpassword" path="userAccountpassword" />

	<acme:textbox code="provider.edit.name" path="name" />
	<acme:textarea code="provider.edit.surname" path="surname" />
	<acme:textbox code="provider.edit.photo" path="photo" />
	<acme:textbox code="provider.edit.email" path="email"
		placeholder="id@domain / alias id@domain / id@ / alias id@" size="45" />
	<div>
		<form:label path="phone">
			<spring:message code="provider.edit.phone" />
		</form:label>
		<form:input path="phone" onblur="phoneFun()" />
		<form:errors path="phone" cssClass="error" />
	</div>
	
	<acme:textbox code="provider.edit.address" path="address" />
	<acme:numberbox code="provider.edit.vat" path="vat" min="0" max="1"/>
	<acme:textbox code="provider.edit.make" path="providerMake" />
	
	<h3><spring:message code="provider.edit.creditCard"/></h3>
	<acme:textbox code="provider.edit.holder" path="holderName" />
	<form:label path="make">
		<spring:message code="provider.edit.make" />
	</form:label>
	<form:select path="make">
		<form:options items="${cardmakes}"/>
	</form:select>
	<form:errors path="make" cssClass="error" />
	<acme:textbox code="provider.edit.number" path="number" />
	<acme:textbox code="provider.edit.expirationMonth" path="expirationMonth" />
	<acme:textbox code="provider.edit.expirationYear" path="expirationYear" />
	<acme:numberbox code="provider.edit.cvv" path="cvv" min="100" max="999"/>
	<br/>

	<jstl:choose>
	    <jstl:when test="${providerForm.termsAndCondicions == true}">
	        <form:hidden path="termsAndCondicions"/>
	    </jstl:when>    
	    <jstl:otherwise>
			<form:checkbox path="termsAndCondicions"/><a href="profile/terms.do"><spring:message code="edit.accepted"/> <spring:message code="edit.termsAndConditions"/></a>
			<br>
	    </jstl:otherwise>
	</jstl:choose>
	<br/>

	<input type="submit" name="save"
		value="<spring:message code="provider.edit.submit" />" />
	
</form:form>