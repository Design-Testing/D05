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



<display:table name="curriculas" id="row"
		requestURI="" pagesize="5"
		class="displaytag">

	<spring:message var="id" code="curricula.id"/>
    <display:column property="id" title="${id}" sortable="true"/>
    

	

	<display:column>
			<input type="button" name="display"
                value="<spring:message code="curricula.display" />"
                onclick="relativeRedir('curricula/display.do?curriculaId=${row.id}')" />
	</display:column>
	
	
	<display:column>
                <input type="button" name="apply"
                    value="<spring:message code="curricula.apply" />"
                    onclick="relativeRedir('application/rooky/apply.do?positionId=${positionId}&curriculaId=${row.id}')" />
	</display:column>

	
</display:table>