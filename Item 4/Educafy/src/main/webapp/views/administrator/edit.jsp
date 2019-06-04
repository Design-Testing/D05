<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
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
			<p id="mensajeError"><spring:message code="error.message.error.div"/></p>
			<ul>
				<jstl:forEach items="${errors}" var="error">
					<jstl:if test="${error.field eq 'termsAndCondicions'}">
						<li><spring:message code="edit.${error.field}"/> - <jstl:out value="${error.defaultMessage}" /></li>
					</jstl:if>
				</jstl:forEach>
			</ul>
		</div>
	</jstl:if>
	<br>
<h2>
	<spring:message code="administrator.edit.msg" />
</h2>

<form:form modelAttribute="actorForm" action="administrator/edit.do"
	method="POST">
	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="administrator.edit.userAccountuser"
		path="userAccountuser" />
	<acme:password code="administrator.edit.userAccountpassword"
		path="userAccountpassword" />
	<acme:textbox code="administrator.edit.name" path="name" />
	<acme:textarea code="administrator.edit.surname" path="surname" />
	<acme:textbox code="administrator.edit.photo" path="photo" />

	<acme:textbox code="administrator.edit.email" path="email"
		placeholder="id@domain / alias id@domain / id@ / alias id@" size="45" />
	<div>
		<form:label path="phone">
			<spring:message code="administrator.edit.phone" />
		</form:label>
		<form:input path="phone" onblur="phoneFun()" />
		<form:errors path="phone" cssClass="error" />
	</div>

	<acme:textbox code="administrator.edit.address" path="address" />
	<br>

	<jstl:choose>
		<jstl:when test="${actorForm.termsAndCondicions == true}">
			<form:hidden path="termsAndCondicions" />
		</jstl:when>
		<jstl:otherwise>
			<form:checkbox path="termsAndCondicions" />
			<a href="profile/terms.do"><spring:message code="edit.accepted" />
				<spring:message code="edit.termsAndConditions" /></a>
			<br>
		</jstl:otherwise>
	</jstl:choose>

	<input type="submit" name="save"
		value="<spring:message code="administrator.edit.submit" />" />


</form:form>