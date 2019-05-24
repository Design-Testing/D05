
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style type="text/css">
img.resize {
	max-width: 10%;
	max-height: 10%;
}
</style>

<acme:display code="lesson.ticker" value="${lesson.ticker}" />

<acme:display code="lesson.title" value="${lesson.title}" />

<acme:display code="lesson.description" value="${lesson.description}" />

<acme:display code="lesson.price" value="${lesson.price}" />

<spring:message code="lesson.isDraft"/>:
<jstl:choose>
	<jstl:when test="${lesson.isDraft eq true}">
		<spring:message code="lesson.isDraft.true" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="lesson.isDraft.false" />
	</jstl:otherwise>
</jstl:choose>
<br>
<acme:display code="lesson.teacher" value="${lesson.teacher.name}" />

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<acme:display code="lesson.subject" value="${lesson.subject.nameEn}" />
	</jstl:when>
	<jstl:otherwise>
		<acme:display code="lesson.subject" value="${lesson.subject.nameEs}" />
	</jstl:otherwise>
</jstl:choose>

<h3><spring:message code="lesson.assesments"/></h3>
<security:authorize access="hasRole('STUDENT')">
	<acme:button url="assesment/student/create.do?lessonId=${lesson.id}" name="create" code="lesson.assesment.create"/>
</security:authorize>

<jstl:choose>
	<jstl:when test="${not empty assesments}">
		<display:table name="assesments" id="row" requestURI="${requestURI}" pagesize="5"
		class="displaytag">
		
		<display:column property="lesson.title" titleKey="assesment.lesson" />
		
		<display:column property="score" titleKey="assesment.score" />
		
		<display:column>
			<acme:button url="assesment/display.do?assesmentId=${row.id}" name="display" code="assesment.display" />
		</display:column>
		
		</display:table>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="no.assesment"/>
	</jstl:otherwise>
</jstl:choose>


<acme:button url="subject/display.do?subjectId=${lesson.subject.id}" name="back" code="lesson.back" />

