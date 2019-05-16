
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="position/company/edit.do" modelAttribute="positionForm" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:textbox code="position.title" path="title" />
	<acme:textbox code="position.description" path="description" />
	<acme:textbox code="position.deadline" path="deadline" placeholder="yyyy-MM-dd HH:mm"/>
	<spring:message code="position.data.pattern"/>
	<acme:textbox code="position.profile" path="profile" />
	<acme:textarea code="position.skills" path="skills" />
	<acme:textarea code="position.technologies" path="technologies" />
	<acme:numberbox code="position.salary" path="salary" min="0" />
<br>
	<acme:button url="position/company/myPositions.do" name="save" code="position.submit"/>
	<acme:button url="position/company/myPositions.do" name="back" code="position.back"/>

</form:form>