<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>


<jstl:set var="chooseList" value="/list" />
	<jstl:if test="${not empty listPositions}">
		<jstl:set var="chooseList" value="${rolURL}/${listPositions}" />
	</jstl:if>

<display:table name="positions" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">

	<display:column property="ticker" titleKey="position.ticker" />

	<display:column property="title" titleKey="position.title" />
	
	<display:column property="mode" titleKey="position.mode" />
		
	<acme:dataTableColumn property="deadline" code="position.deadline"/>
	
	<display:column>
	
		<acme:button url="audit/auditor/create.do?positionId=${row.id}" name="display" code="position.audit"/>
	
	</display:column>
	
	<display:column>
	
		<acme:button url="position/display.do?positionId=${row.id}" name="display" code="position.display"/>
	
	</display:column>
	
	

	
</display:table>

<jstl:if test="${not empty msg}">
	<h3 style="color: red;"><spring:message code="${msg}"/></h3>
</jstl:if>

<jstl:if test="${empty positions}">
	<spring:message code="no.positions"/>
</jstl:if>
