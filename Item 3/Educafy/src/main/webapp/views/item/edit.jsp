<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="item/provider/edit.do" modelAttribute="itemForm" method="POST">

	<form:hidden path="id"/>
    <form:hidden path="version"/>


    <acme:textbox path="name" code="item.name"/>
    <acme:textbox path="description" code="item.description"/>
    <acme:textbox path="photo" code="item.photo"/>
    <acme:textbox path="links" code="item.links"/>

    <br/>

    <!---------------------------- BOTONES -------------------------->

 	<acme:submit name="save" code="general.save"/>
 	
    <input type="button" class="btn btn-danger" name="back"
           value="<spring:message code="item.back" />"
           onclick="relativeRedir('item/provider/list.do');"/>



</form:form>