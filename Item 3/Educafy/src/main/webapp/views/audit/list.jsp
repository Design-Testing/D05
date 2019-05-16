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
	<jstl:if test="${not empty listApplications}">
		<jstl:set var="chooseList" value="${rolURL}/${listApplications}" />
	</jstl:if>

<display:table name="audits" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">

	<display:column property="position.title" titleKey="audit.position"/>

	<display:column property="auditor" titleKey="audit.auditor" />
		
	<display:column property="text" titleKey="audit.text" />
	
	<display:column property="score" titleKey="audit.score" />
	
	
	<security:authorize access="hasRole('AUDITOR')">
	
		<jstl:if test="${row.isDraft eq true}">
				<display:column>
					<acme:button url="audit/auditor/edit.do?auditId=${row.id}&positionId=${row.position.id}"
						name="submit" code="audit.edit" />
				</display:column>
				<display:column>
					<acme:button url="audit/auditor/delete.do?auditId=${row.id}"
						name="submit" code="audit.delete" />
				</display:column>
				<display:column>
					<acme:button url="audit/auditor/toFinal.do?auditId=${row.id}"
						name="submit" code="audit.final" />
				</display:column>
		</jstl:if>
	
	</security:authorize>
	
		
	<display:column>
		<acme:button url="audit/auditor/display.do?auditId=${row.id}" name="display" code="audit.display" />
	</display:column>
	
	
</display:table>
