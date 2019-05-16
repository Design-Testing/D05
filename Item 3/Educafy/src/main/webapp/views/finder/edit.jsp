
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="finder/rooky/edit.do" modelAttribute="finder" method="POST">

    
    <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="positions"/>
	<form:hidden path="creationDate"/>
	
	<acme:textbox code="finder.keyword" path="keyword"/>
	<acme:numberbox code="finder.min.salary" path="minSalary" min="0"/>
	<acme:numberbox code="finder.max.salary" path="maxSalary" min="0"/>
	<acme:textbox code="finder.minDate" path="minDeadline" placeholder="yyyy-MM-dd HH:mm"/>
	<acme:textbox code="finder.maxDate" path="maxDeadline" placeholder="yyyy-MM-dd HH:mm"/>
	<br>
	<jstl:if test="${salnul}">
		<spring:message code="finder.n.salary" />
	</jstl:if>
	<jstl:if test="${deadnul}">
		<spring:message code="finder.n.deadline" />
	</jstl:if>
	<br><br>
	<input type="submit" name="save" value="<spring:message code="finder.search" />" />
	<input type="submit" name="clear" value="<spring:message code="finder.clear" />" />
	<br>
	<br>
	<spring:message code="finder.results" />
	<br>
	
	
<display:table name="${finder.positions}" id="row" requestURI="/finder/rooky/edit.do" pagesize="15" class="displaytag">
	<display:column property="title" titleKey="position.title" />
	
	<display:column property="ticker" titleKey="position.ticker" />

	<acme:dataTableColumn code="position.deadline" property="deadline" />
	
	<display:column titleKey="position.company">
		<jstl:out value="${row.company.commercialName}" />
		
	</display:column>
	
	<display:column>
		<acme:link url="position${rolURL}/display.do?positionId=${row.id}"
			code="position.display" />
	</display:column>
	
	
</display:table>
		
</form:form>