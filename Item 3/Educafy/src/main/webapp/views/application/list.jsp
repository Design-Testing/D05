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

<display:table name="applications" id="row"
		requestURI="application${chooseList}.do" pagesize="5"
		class="displaytag">

	<display:column property="position.title" titleKey="application.position"/>

	<display:column property="rooky" titleKey="application.rooky" />
		
	<display:column titleKey="application.status">
		<acme:statusChoose status="${row.status}"/>
	</display:column>
	
	<display:column property="problem.title" titleKey="application.problem" />
	
	<security:authorize access="hasRole('COMPANY')">
	<display:column>
				<jstl:if test="${row.status eq 'SUBMITTED'}">
					<acme:button url="application/company/accept.do?applicationId=${row.id}"
						name="accept" code="application.accept" />
				</jstl:if>
			</display:column>

			<display:column>
				<jstl:if test="${row.status eq 'SUBMITTED'}">
					<acme:button url="application/company/reject.do?applicationId=${row.id}"
						name="reject" code="application.reject" />
				</jstl:if>
			</display:column>
	</security:authorize>

	<security:authorize access="hasRole('ROOKY')">
	<display:column>
				<jstl:if test="${row.status eq 'PENDING'}">
					<acme:button url="application/rooky/edit.do?applicationId=${row.id}"
						name="submit" code="application.submit" />
				</jstl:if>
			</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('COMPANY')">	
	<display:column>
		<acme:button url="application/company/display.do?applicationId=${row.id}" name="display" code="application.display" />
	</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ROOKY')">	
	<display:column>
		<acme:button url="application/rooky/display.do?applicationId=${row.id}" name="display" code="application.display" />
	</display:column>
	</security:authorize>


</display:table>
