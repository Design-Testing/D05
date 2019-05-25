
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
.false {
	background-color: green;
}

.true {
	background-color: red;
}

</style>

<h3><spring:message code="schedule.monday"/></h3>
<display:table name="schedule" id="row"
		requestURI="${requestURI}"
		class="displaytag">

<jstl:forEach items="${schedule.monday}" var="m">
<jstl:set value="${m}" var="colorStyle" />
	<display:column titleKey="schedule.monday" class="${colorStyle}">
	<jstl:choose>
		<jstl:when test="${m}">
			<spring:message code="ocupado" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="libre" />
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
</jstl:forEach>

</display:table>


<h3><spring:message code="schedule.tuesday"/></h3>
<display:table name="schedule" id="row"
		requestURI="${requestURI}"
		class="displaytag">

<jstl:forEach items="${schedule.tuesday}" var="t">
	<jstl:set value="${t}" var="colorStyle" />
	<display:column titleKey="schedule.tuesday" class="${colorStyle}">
	<jstl:choose>
		<jstl:when test="${t}">
			<spring:message code="ocupado" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="libre" />
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
</jstl:forEach>

</display:table>

<h3><spring:message code="schedule.wednesday"/></h3>
<display:table name="schedule" id="row"
		requestURI="${requestURI}"
		class="displaytag">

<jstl:forEach items="${schedule.wednesday}" var="w">
	<jstl:set value="${w}" var="colorStyle" />
	<display:column titleKey="schedule.wednesday" class="${colorStyle}">
	<jstl:choose>
		<jstl:when test="${w}">
			<spring:message code="ocupado" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="libre" />
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
</jstl:forEach>

</display:table>

<h3><spring:message code="schedule.thursday"/></h3>
<display:table name="schedule" id="row"
		requestURI="${requestURI}"
		class="displaytag">

<jstl:forEach items="${schedule.thursday}" var="th">
	<jstl:set value="${th}" var="colorStyle" />
	<display:column titleKey="schedule.thursday" class="${colorStyle}">
	<jstl:choose>
		<jstl:when test="${th}">
			<spring:message code="ocupado" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="libre" />
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
</jstl:forEach>

</display:table>

<h3><spring:message code="schedule.friday"/></h3>
<display:table name="schedule" id="row"
		requestURI="${requestURI}"
		class="displaytag">

<jstl:forEach items="${schedule.friday}" var="f">
	<jstl:set value="${f}" var="colorStyle" />
	<display:column titleKey="schedule.friday" class="${colorStyle}">
	<jstl:choose>
		<jstl:when test="${f}">
			<spring:message code="ocupado" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="libre" />
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
</jstl:forEach>

</display:table>



