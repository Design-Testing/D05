<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Only brotherhood can access to this view -->

<form:form action="problem/company/edit.do"
	modelAttribute="problem">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="mode" value="DRAFT"/>
	<form:hidden path="company"/>
	<form:hidden path="position"/>
	<input type="hidden" name="positionId" value="${positionId}"/>

	<acme:textbox code="problem.title" path="title" />
	<acme:textbox code="problem.statement" path="statement" />
	<acme:textbox code="problem.hint" path="hint" />
	<acme:textbox code="problem.attachments" path="attachments" />
	<h5 style="color: red;"><spring:message code="collection.attachments"/></h5>

	<input type="submit" name="save"
		value="<spring:message code="problem.save" />" />

	<acme:button url="position/company/display.do?positionId=${positionId}" name="cancel"
		code="problem.cancel" />

</form:form>