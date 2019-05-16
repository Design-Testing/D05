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

<acme:display code="problem.company" value="${problem.company}" />
<acme:display code="problem.title" value="${problem.title}" />
<acme:display code="problem.statement" value="${problem.statement}" />
<acme:display code="problem.hint" value="${problem.hint}" />
<spring:message code="problem.mode"/>:
 <acme:modeChoose mode="${problem.mode}"/>
<br>
<spring:message code="problem.attachments"/>: 
<jstl:forEach items="${problem.attachments}" var="l">
	<ul>
		<li><jstl:out value="${l}"/></li>
	</ul>
</jstl:forEach>
<br>
<jstl:if test="${empty applications}">
	<acme:button url="problem/company/delete.do?problemId=${problem.id}&positionId=${position.id}" name="delete" code="problem.delete"/>	
</jstl:if>

<acme:button url="position/company/display.do?positionId=${problem.position.id}" name="back"
		code="problem.position.associated" />
		
<jstl:if test="${not empty errortrace}">
	<h3 style="color: red;"><spring:message code="${errortrace}"/></h3>
</jstl:if>

<br />
