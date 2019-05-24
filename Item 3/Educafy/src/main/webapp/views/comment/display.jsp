
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

<acme:display code="comment.text" value="${comment.text}"/>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="comment.moment" />: <fmt:formatDate
			value="${comment.moment}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="comment.moment" />: <fmt:formatDate
			value="${comment.moment}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>

<acme:display code="comment.assesment" value="${comment.assesment.lesson.title}"/>

<br>

<acme:button url="assesment/display.do?assesmentId=${comment.assesment.id}" name="back" code="lesson.back" />

