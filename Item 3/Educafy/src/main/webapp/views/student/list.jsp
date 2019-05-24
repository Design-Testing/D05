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

<security:authorize access="hasRole('ADMIN')">
	
	<acme:button url="teacher/computeScores.do" name="display" code="teacher.compute.scores"/>
</security:authorize>

<display:table name="companies" id="row"
	requestURI="teacher/list.do" pagesize="5"
	class="displaytag">


	<display:column property="name" titleKey="actor.name" />
		
	<display:column property="email" titleKey="actor.email" />
	
	<jstl:choose>
	<jstl:when test="${not empty row.score}">
		<display:column property="score" titleKey="teacher.score" />
	</jstl:when>
	<jstl:otherwise>
		<display:column titleKey="teacher.score">
			<jstl:out value="-"></jstl:out>
		</display:column>
	</jstl:otherwise>
	</jstl:choose>

	
	<display:column>
	<acme:button url="teacher/display.do?teacherId=${row.id}" name="display" code="teacher.display"/>
	</display:column>
	
	<security:authorize access="hasRole('ADMIN')">
	
		<display:column>
	<acme:button url="teacher/computeScore.do?teacherId=${row.id}" name="display" code="teacher.compute.score"/>
	</display:column>
	
	</security:authorize>


</display:table>
