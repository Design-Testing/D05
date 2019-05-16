<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	
<acme:display code="item.name" value="${item.name}" />
<acme:display code="item.description" value="${item.description}" />
<acme:display code="item.links" value="${item.links}" />

<jstl:if test="${not empty item.photo}">
<spring:message code="item.photo"/>:<br>
<img src="${item.photo}" alt="<spring:message code="item.alt.image"/> ${item.photo}" width="20%" height="20%"/>
<br><br></jstl:if>

<br>

<security:authorize access="hasRole('PROVIDER')">
	
		<input type="button" class="btn btn-danger" name="back"
           value="<spring:message code="item.back" />"
           onclick="relativeRedir('item/provider/list.do');"/>
	
</security:authorize>


