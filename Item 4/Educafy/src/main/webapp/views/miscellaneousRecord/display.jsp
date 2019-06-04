
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	
<acme:display code="record.freeText" value="${miscellaneousRecord.freeText}" />
<spring:message code="record.attachments"/>
<jstl:forEach items="${miscellaneousRecord.attachments}" var="l">
	<ul>
		<li><jstl:out value="${l}"/></li>
	</ul>
</jstl:forEach>

<br>


<jstl:choose>
<jstl:when test="${buttons}">
	<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/display.do?curriculumId=${curriculumId}');"/>
</jstl:when>
<jstl:when test="${buttonsAnonymous}">
	<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/displayById.do?curriculumId=${curriculumId}');"/>
</jstl:when>
<jstl:when test="${buttonsCertifier}">
	<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/displayById.do?curriculumId=${curriculumId}');"/>
</jstl:when>

</jstl:choose>