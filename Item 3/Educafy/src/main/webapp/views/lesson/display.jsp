
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

<acme:display code="lesson.ticker" value="${lesson.ticker}"/>

<acme:display code="lesson.title" value="${lesson.title}"/>

<acme:display code="lesson.description" value="${lesson.description}"/>

<acme:display code="lesson.price" value="${lesson.price}"/>

<jstl:choose>
	<jstl:when test="${lesson.isDraft eq true}">
		<spring:message code="lesson.isDraft"/>: <spring:message code="lesson.isDraft.true"/>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="lesson.isDraft"/>: <spring:message code="lesson.isDraft.false"/>
	</jstl:otherwise>
</jstl:choose>

<acme:display code="lesson.teacher" value="${lesson.teacher.name}"/>
<acme:button url="teacher/display.do?teacherId=${lesson.teacher.id}" name="displayTeacher" code="lesson.display"/>
<br><br>

<acme:display code="lesson.subject" value="${lesson.subject.name}"/>


<br><br>


<jstl:choose>
	<jstl:when test="${rol eq 'teacher' }">
		<acme:button url="lesson/teacher/myLessons.do" name="back" code="lesson.back"/>
	</jstl:when>
	<jstl:when test="${rol eq 'student'}">
		<acme:button url="lesson/student/myLessons.do" name="back" code="lesson.back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="lesson/list.do" name="back" code="lesson.back"/>
	</jstl:otherwise>
</jstl:choose>
