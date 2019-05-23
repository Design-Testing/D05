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

<acme:button url="subject/create.do" name="create" code="subject.create"/>

<display:table name="subjects" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
	<jstl:choose>
		<jstl:when test="${lang eq 'en'}">
			<display:column property="nameEn" titleKey="subject.name" />
			
			<display:column property="descriptionEn" titleKey="subject.description" />
		</jstl:when>
		<jstl:otherwise>
			<display:column property="nameEs" titleKey="subject.name" />
			
			<display:column property="descriptionEs" titleKey="subject.description" />
		</jstl:otherwise>
	</jstl:choose>
	
	<display:column property="level" titleKey="subject.level" />
	
	<display:column>
		<acme:button url="subject/edit.do?subjectId=${row.id}" name="edit" code="subject.edit"/>
	</display:column>
	
	<display:column>
		<acme:button url="subject/display.do?subjectId=${row.id}" name="display" code="subject.display"/>
	</display:column>
	
	
</display:table>

