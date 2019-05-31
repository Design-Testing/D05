<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<form:form action="personalRecord/edit.do" modelAttribute="personalRecord">

	<form:hidden path="id"/>
    <form:hidden path="version"/>
    <form:hidden path="isDraft"/>
    <form:hidden path="isCertified"/>


    <acme:textbox path="statement" code="record.statement"/>
    <acme:textbox path="github" code="record.github"/>
    <acme:textbox path="photo" code="record.photo"/>
   <acme:textbox path="linkedin" code="record.linkedin"/>

    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/display.do?curriculumId=${curriculumId}');"/>



</form:form>