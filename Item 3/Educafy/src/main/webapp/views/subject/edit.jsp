
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="subject/edit.do" modelAttribute="subject" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="subject.nameEn" path="nameEn"/>
	<acme:textbox code="subject.nameEs" path="nameEs"/>
	<acme:textbox code="subject.descriptionEn" path="descriptionEn"/>
	<acme:textbox code="subject.descriptionEs" path="descriptionEs"/>
	<acme:textbox code="subject.level" path="level"/>
<br>

	<input type="submit" name="save"
		value="<spring:message code="subject.save" />" />
	
	<acme:button url="subject/list.do" name="back" code="subject.back"/>

</form:form>