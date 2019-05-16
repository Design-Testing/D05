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
		requestURI="position${chooseList}.do" pagesize="5"
		class="displaytag">

	<display:column property="ticker" titleKey="position.ticker" />

	<display:column property="title" titleKey="position.title" />
	
	<display:column property="mode" titleKey="position.mode" />
		
	<acme:dataTableColumn property="deadline" code="position.deadline"/>
	
	<display:column>
	<jstl:choose>
	<jstl:when test="${rol eq 'company' }">
		<acme:button url="position/company/display.do?positionId=${row.id}" name="display" code="position.display"/>
	</jstl:when>
	<jstl:when test="${rol eq 'auditor' }">
		<acme:button url="audit/auditor/create.do?positionId=${row.id}" name="create" code="position.create.audit"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="position/display.do?positionId=${row.id}" name="display" code="position.display"/>
	</jstl:otherwise>
	</jstl:choose>
	</display:column>
	
	<security:authorize access="hasRole('ROOKY')">
	<jstl:if test="${rol eq 'rooky'}">
	<jstl:set var="ctrl" value="0"/>
	<jstl:forEach var="t" items="${rookyPositions}">
		<jstl:if test="${t eq row}">
			<jstl:set var="ctrl" value="1"/>
		</jstl:if>
	</jstl:forEach>
	
			<display:column>
				<jstl:choose>
					<jstl:when test="${ctrl == 0}">
						<acme:button url="application/rooky/create.do?positionId=${row.id}" name="apply" code="position.application"/>
					</jstl:when>
					<jstl:otherwise>
						<spring:message code="position.applied" />
					</jstl:otherwise>
				</jstl:choose>
			</display:column>
	</jstl:if>
	</security:authorize>
		
	<security:authorize access="hasRole('COMPANY')">
	<display:column>
		<jstl:if test="${row.mode eq 'DRAFT' and rol eq 'company'}">
		<acme:button url="position/company/edit.do?positionId=${row.id}" name="edit" code="position.edit"/>
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${row.mode eq 'DRAFT' and rol eq 'company'}">
		<acme:button url="position/company/delete.do?positionId=${row.id}" name="delete" code="position.delete"/>
		</jstl:if>
	</display:column>
	
	<display:column>
	<jstl:if test="${row.mode eq 'DRAFT' and rol eq 'company'}">
		<acme:button url="position/company/finalMode.do?positionId=${row.id}" name="edit" code="position.finalMode"/>
	</jstl:if>
	<jstl:if test="${row.mode eq 'FINAL' and rol eq 'company'}">
		<acme:button url="position/company/cancelledMode.do?positionId=${row.id}" name="edit" code="position.cancelledMode"/>
	</jstl:if>
	</display:column>
	
	</security:authorize>
	
	<display:column>
		<acme:button url="audit/auditor/listAll.do?positionId=${row.id}" name="display" code="audit.display"/>
	</display:column>

	<security:authorize access="hasRole('PROVIDER')">
			<jstl:if test="${rol eq 'provider'}">
				<jstl:set var="ctrl" value="0" />
				<jstl:forEach var="r" items="${providerpositions}">
					<jstl:if test="${r eq row}">
						<jstl:set var="ctrl" value="1" />
					</jstl:if>
				</jstl:forEach>
				<display:column>
					<jstl:choose>
						<jstl:when test="${ctrl == 0}">
							<acme:link url="sponsorship/provider/create.do?positionId=${row.id}"
								code="position.to.sponsors" />
						</jstl:when>
						<jstl:otherwise>
							<acme:link
								url="sponsorship/provider/display.do?positionId=${row.id}"
								code="position.sponsored" />
						</jstl:otherwise>
					</jstl:choose>
				</display:column>
			</jstl:if>
		</security:authorize>
</display:table>

<jstl:if test="${not empty msg}">
	<h3 style="color: red;"><spring:message code="${msg}"/></h3>
</jstl:if>

<jstl:if test="${empty positions}">
	<spring:message code="no.positions"/>
</jstl:if>
