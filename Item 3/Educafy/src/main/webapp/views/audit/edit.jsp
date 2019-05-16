
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<form:form action="application/rooky/edit.do" modelAttribute="auditForm" method="POST">
	<form:hidden path="id" />
	<form:hidden path="version" />

	<div>
		<form:label path="text">
			<spring:message code="audit.text" />
		</form:label>
		<form:textarea path="text" />
		<form:errors path="text" cssClass="error" />
	</div>
	<br>
	<div>
		<form:label path="score">
			<spring:message code="audit.score" />
		</form:label>
		<form:input path="score" />
		<form:errors path="score" cssClass="error" />
	</div>
	<br><br>
	<input type="submit" name="save"
		value="<spring:message code="audit.submit" />" />

	<acme:button url="application/rooky/listPending.do" name="back"
		code="application.back" />
</form:form>
