
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
img.resize {
  max-width:10%;
  max-height:10%;
}
</style>

<jstl:choose>
	<jstl:when test="${lang eq 'en'}">
		<acme:display code="subject.name" value="${subject.nameEn}"/>
		
		<acme:display code="subject.description" value="${subject.descriptionEn}"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:display code="subject.name" value="${subject.nameEs}"/>
		
		<acme:display code="subject.description" value="${subject.descriptionEs}"/>
	</jstl:otherwise>
</jstl:choose>

<acme:display code="subject.level" value="${subject.level}"/>

<h3><spring:message code="subject.lessons"/></h3>
<security:authorize access="hasRole('TEACHER')">
	<acme:button url="lesson/teacher/create.do?subjectId=${subject.id}" name="create" code="subject.lesson.create"/>
</security:authorize>

	<jstl:choose>
	<jstl:when test="${not empty lessons}">
		<display:table name="lessons" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">

	<display:column property="title" titleKey="lesson.title" />
	
	<display:column property="teacher.name" titleKey="lesson.teacher" />

		<display:column>
			<acme:button url="lesson/display.do?lessonId=${row.id}" name="display" code="lesson.display" />
		</display:column>

	<security:authorize access="hasRole('STUDENT')">
	<display:column>
		<acme:button url="reservation/student/create.do?lessonId=${row.id}" name="create" code="reservation.create"/>
	</display:column>
	</security:authorize>
	
</display:table>
		
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="no.lessons"/>
	</jstl:otherwise>
</jstl:choose>
	

<br><br>

<jstl:if test="${empty lessons}">
		<acme:button url="subject/delete.do?subjectId=${subject.id}" name="back" code="subject.delete"/>
</jstl:if>



<acme:button url="subject/list.do" name="back" code="subject.listing"/>
