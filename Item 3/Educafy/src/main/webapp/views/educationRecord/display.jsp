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

	
<acme:display code="record.degree" value="${educationRecord.degree}" />
<acme:display code="record.institution" value="${educationRecord.institution}" />
<acme:display code="record.mark" value="${educationRecord.mark}" />
<acme:display code="record.startDate" value="${educationRecord.startDate}" />
<acme:display code="record.endDate" value="${educationRecord.endDate}" />
<acme:display code="record.attachment" value="${educationRecord.attachment}" />

<br>

<input type="button" class="btn btn-danger" name="cancel"
           value="<spring:message code="general.cancel" />"
           onclick="relativeRedir('curriculum/display.do?curriculumId=${curriculumId}');"/>