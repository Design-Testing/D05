<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="folder/edit.do" modelAttribute="folderForm" method="POST">
	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox path="name" code="folder.name" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="general.save" />" />
</form:form>
<input type="button" class="btn btn-danger" name="cancel"
		value="<spring:message code="general.cancel" />"
		onclick="relativeRedir('folder/list.do');" />