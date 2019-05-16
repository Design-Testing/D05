<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="educationData/edit.do?curriculaId=${curriculaId}" modelAttribute="educationData">

	<form:hidden path="id"/>
    <form:hidden path="version"/>
    
    
    <input type="hidden" name="curriculaId" value="${curriculaId}" />
    
     <jstl:if test="${alert}">
		<h5 style="color: red;"><spring:message code="alert.dates"/></h5>
	</jstl:if>

    <acme:textbox path="degree" code="record.degree"/>
    <acme:textbox path="institution" code="record.institution"/>
    <acme:textbox path="mark" code="record.mark"/>
    <acme:textbox path="startDate" code="record.startDate"/>
    <acme:textbox path="endDate" code="record.endDate"/>

    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curricula/display.do?curriculaId=${curriculaId}');"/>



</form:form>