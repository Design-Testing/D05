
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="timePeriod/edit.do" modelAttribute="timePeriod" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textarea code="timePeriod.startHour" path="startHour"/>
	<acme:textarea code="timePeriod.endHour" path="endHour"/>
	<acme:textarea code="timePeriod.dayNumber" path="dayNumber"/>
<br>

	<input type="submit" name="save"
		value="<spring:message code="timePeriod.submit" />" />
	
	<acme:button url="URL del display de reservation" name="back" code="timePeriod.back"/>

</form:form>