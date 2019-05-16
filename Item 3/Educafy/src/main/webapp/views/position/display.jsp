
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


<acme:display code="position.title" value="${position.title}"/>

<acme:display code="position.description" value="${position.description}"/>

<jstl:choose>
	<jstl:when test="${lang eq 'en' }">
		<spring:message code="position.deadline" />: <fmt:formatDate
			value="${position.deadline}" type="both" pattern="yyyy/MM/dd HH:mm" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="position.deadline" />: <fmt:formatDate
			value="${position.deadline}" type="both" pattern="dd/MM/yyyy HH:mm" />
	</jstl:otherwise>
</jstl:choose>
<br>

<acme:display code="position.ticker" value="${position.ticker}"/>

<acme:display code="position.profile" value="${position.profile}"/>

<acme:display code="position.skills" value="${position.skills}"/>

<acme:display code="position.technologies" value="${position.technologies}"/>

<acme:display code="position.salary" value="${position.salary}"/>

<acme:display code="position.mode" value="${position.mode}"/>

<acme:display code="position.company" value="${position.company.name}"/> 
<acme:button url="company/display.do?companyId=${position.company.id}" name="displayCompany" code="position.display"/>
<br><br>
<spring:message code="position.problem"/>

<security:authorize access="hasRole('COMPANY')">
<jstl:if test="${position.mode eq 'DRAFT' and (company.id eq position.company.id)}">
<acme:button url="problem/company/create.do?positionId=${position.id}" name="create" code="position.create" />
<br>
</jstl:if>
</security:authorize>

<display:table pagesize="5" class="displaytag" keepStatus="true"
               name="problems" requestURI="problem/company/list.do" id="row">
	
    <display:column property="title" titleKey="problem.title" sortable="true"/>

    <display:column property="statement" titleKey="problem.statement" sortable="true"/>

    <display:column property="hint" titleKey="problem.hint" sortable="true"/>


	<security:authorize access="hasRole('COMPANY')">	
	<display:column>
	<jstl:if test="${row.mode eq 'DRAFT' and (company.id eq position.company.id)}">
            <input type="button" name="edit"
                value="<spring:message code="problem.edit" />"
                onclick="relativeRedir('problem/company/edit.do?problemId=${row.id}&positionId=${row.position.id}')" />
	</jstl:if>
	</display:column>
	
	<display:column>
	<jstl:if test="${row.mode eq 'DRAFT' and (company.id eq position.company.id)}">
		<acme:button url="problem/company/finalMode.do?problemId=${row.id}" name="finalMode" code="problem.finalMode"/>
	</jstl:if>
	</display:column>
	
	<display:column>
	<jstl:if test="${company.id eq position.company.id}">
		<acme:button url="problem/company/display.do?problemId=${row.id}" name="display" code="problem.display"/>
	</jstl:if>
	</display:column>
	
	</security:authorize>
	
	<security:authorize access="hasRole('ROOKY')">
		<jstl:set var="hk" value="1"/>
	</security:authorize>
	<security:authorize access="hasRole('AUDITOR')">
		<jstl:set var="aud" value="1"/>
	</security:authorize>
        
</display:table>
<br><br>
<jstl:if test="${not empty imgbanner}">
	<a href="<jstl:out value="${targetpage}"/>">
		<img class="resize" src="${imgbanner}" alt="Banner"/>
	</a><br /><br />
</jstl:if>
<jstl:choose>
	<jstl:when test="${rol eq 'company' }">
		<acme:button url="position/company/myPositions.do" name="back" code="position.back"/>
	</jstl:when>
	<jstl:when test="${hk eq 1}">
		<acme:button url="position/rooky/list.do" name="back" code="position.back"/>
	</jstl:when>
	<jstl:when test="${aud eq 1}">
		<acme:button url="audit/auditor/listFreePositions.do" name="back" code="position.back"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:button url="position/list.do" name="back" code="position.back"/>
	</jstl:otherwise>
</jstl:choose>
<jstl:if test="${not empty errortrace}">
	<h3 style="color: red;"><spring:message code="${errortrace}"/></h3>
</jstl:if>
<jstl:if test="${not empty trace}">
	<h3 style="color: red;"><spring:message code="${trace}"/> <jstl:out value="${problemdeleted}"/></h3>
</jstl:if>