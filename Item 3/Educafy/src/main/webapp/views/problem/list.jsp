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


<security:authorize access="hasRole('COMPANY')">
    
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="problems" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <display:column property="title" titleKey="problem.title"/>
    <display:column property="statement" titleKey="problem.statement"/>
    <display:column property="mode" titleKey="problem.mode"/>
    <display:column property="company" titleKey="problem.company"/>
    
	<display:column>
	<jstl:if test="${row.mode eq 'DRAFT'}">
            <input type="button" name="edit"
                value="<spring:message code="problem.edit" />"
                onclick="relativeRedir('problem/company/edit.do?problemId=${row.id}&positionId=${row.position.id}')" />
	</jstl:if>
	</display:column>
	<display:column>
		<acme:link url="position/company/display.do?positionId=${row.position.id}" code="problem.position"/>
	</display:column>
	
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="problem.display" />"
                onclick="relativeRedir('problem/company/display.do?problemId=${row.id}')" />
	</display:column>
        
	</display:table>
</security:authorize>