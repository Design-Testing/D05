<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="miscellaneousRecord/edit.do?curriculumId=${curriculumId}" modelAttribute="miscellaneousRecord">

	<form:hidden path="id"/>
    <form:hidden path="version"/>
    <form:hidden path="isDraft"/>
    <form:hidden path="isCertified"/>

    <acme:textbox path="freeText" code="record.freeText"/>
    <acme:textarea path="attachments" code="record.attachments"/>


    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/display.do?curriculumId=${curriculumId}');"/>



</form:form>