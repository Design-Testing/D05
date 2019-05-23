
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

<acme:display code="assesment.score" value="${assesment.score}"/>

<acme:display code="assesment.comment" value="${assesment.comment}"/>

<acme:display code="assesment.lesson" value="${assesment.lesson.title}"/>

<acme:display code="assesment.student" value="${assesment.student.name}"/>

<h3><spring:message code="assesment.comments"/></h3>
<security:authorize access="hasRole('TEACHER')">
	<acme:button url="comment/teacher/create.do?assesmentId=${assesment.id}" name="create" code="assesment.comment.create"/>
</security:authorize>

<jstl:choose>
	<jstl:when test="${not empty comments}">
		<display:table name="comments" id="row"
				requestURI="${requestURI}" pagesize="5"
				class="displaytag">
			
			<display:column property="text" titleKey="comment.text" />
			
			<jstl:choose>
			<jstl:when test="${rol eq 'teacher'}">
				<display:column>
				<acme:button url="comment/teacher/display.do?commentId=${row.id}" name="display" code="assesment.display"/>
				</display:column>
			</jstl:when>
			<jstl:otherwise>
				<display:column>
				<acme:button url="comment/display.do?commentId=${row.id}" name="display" code="assesment.display"/>
				</display:column>
			</jstl:otherwise>
			</jstl:choose>
		
		
		</display:table>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="no.comment"/>
	</jstl:otherwise>
</jstl:choose>

<br>

<jstl:choose>
	<jstl:when test="${rol eq 'teacher'}">
		<acme:button url="lesson/teacher/display.do?lessonId=${assesment.lesson.id}" name="back"
			code="lesson.back" />
	</jstl:when>
	<jstl:when test="${rol eq 'student'}">
		<acme:button url="lesson/student/display.do?lessonId=${assesment.lesson.id}" name="back"
			code="lesson.back" />
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="lesson/display.do?lessonId=${assesment.lesson.id}" name="back" code="lesson.back" />
	</jstl:otherwise>
</jstl:choose>

