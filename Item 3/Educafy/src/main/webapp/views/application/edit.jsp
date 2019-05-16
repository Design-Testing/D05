
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="application/rooky/edit.do" modelAttribute="applicationForm" method="POST">
	<form:hidden path="id" />
	<form:hidden path="version" />

	<div>
		<form:label path="explanation">
			<spring:message code="application.explanation" />
		</form:label>
		<form:textarea path="explanation" />
		<form:errors path="explanation" cssClass="error" />
	</div>
	<br>
	<div>
		<form:label path="link">
			<spring:message code="application.link" />
		</form:label>
		<form:input path="link" />
		<form:errors path="link" cssClass="error" />
	</div>
	<br><br>
	<input type="submit" name="save"
		value="<spring:message code="application.submit" />" />

	<acme:button url="application/rooky/listPending.do" name="back"
		code="application.back" />
</form:form>