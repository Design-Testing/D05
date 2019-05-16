<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="positionData/edit.do?curriculaId=${curriculaId}" modelAttribute="positionData">

	<form:hidden path="id"/>
    <form:hidden path="version"/>
    
   <jstl:if test="${alert}">
		<h5 style="color: red;"><spring:message code="alert.dates"/></h5>
	</jstl:if>

    <acme:textbox path="title" code="record.title"/>
    <acme:textbox path="description" code="record.description"/>
    <acme:textbox path="startDate" code="record.startDate"/>
    <acme:textbox path="endDate" code="record.endDate"/>

    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curricula/display.do?curriculaId=${curriculaId}');"/>



</form:form>