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


<display:table name="assesments" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
	<display:column property="lesson.title" titleKey="assesment.lesson" />
	
	<display:column property="score" titleKey="assesment.score" />
	
	<display:column>
		<acme:button url="assesment/display.do?assesmentId=${row.id}" name="display" code="assesment.display"/>
	</display:column>

	<security:authorize access="hasRole('TEACHER')">
		<display:column>
			<acme:button url="comment/teacher/create.do?assesmentId=${row.id}" name="create" code="assesment.comment.create"/>
		</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${not empty msg}">
	<h3 style="color: red;"><spring:message code="${msg}"/></h3>
</jstl:if>

