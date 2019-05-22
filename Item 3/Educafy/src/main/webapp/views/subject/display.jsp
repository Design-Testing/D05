
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
	<jstl:when test="${lang eq 'EN'}">
		<acme:display code="subject.nameEn" value="${subject.nameEn}"/>
		
		<acme:display code="subject.descriptionEn" value="${subject.descriptionEn}"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:display code="subject.nameEs" value="${subject.nameEs}"/>
		
		<acme:display code="subject.descriptionEs" value="${subject.descriptionEs}"/>
	</jstl:otherwise>
</jstl:choose>

<acme:display code="subject.level" value="${lesson.level}"/>

<acme:button url="subject/list.do" name="back" code="subject.back"/>
