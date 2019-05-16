<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${not empty rol}">
	<jstl:set var="rolURL" value="/${rol}" />
</jstl:if>


<jstl:set var="chooseList" value="/list" />
	<jstl:if test="${not empty listPositions}">
		<jstl:set var="chooseList" value="${rolURL}/${listPositions}" />
	</jstl:if>

<display:table name="items" id="row"
		requestURI="${requestURI}" pagesize="5"
		class="displaytag">

	<display:column property="name" titleKey="item.name" />

	<display:column property="description" titleKey="item.description" />
			
	<jstl:if test="${buttons eq true }">
		<display:column>
			<acme:button url="item/provider/delete.do?itemId=${row.id}" name="display" code="item.delete"/>
			<acme:button url="item/provider/edit.do?itemId=${row.id}" name="display" code="item.edit"/>
		</display:column>
	</jstl:if>
	
	<display:column>
			<acme:button url="item/provider/display.do?itemId=${row.id}" name="display" code="item.display"/>
	</display:column>
	
	<jstl:if test="${buttons eq false }">
		<display:column>
			<acme:button url="provider/display.do?providerId=${row.provider.id}" name="display" code="item.display.provider"/>
		</display:column>
	</jstl:if>
	
</display:table>


<jstl:if test="${buttons eq true }">
			<input type="button" name="create"
                value="<spring:message code="item.create" />"
                onclick="relativeRedir('item/provider/create.do')" />
</jstl:if>


