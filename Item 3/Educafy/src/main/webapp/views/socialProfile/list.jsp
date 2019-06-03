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

<h2><spring:message code="socialProfile.list.title"/></h2>
<display:table name="socialProfiles" id="row"
	requestURI="socialProfile/list.do" pagesize="5"
	class="displaytag">


	<display:column property="nick" titleKey="socialProfile.nick" />
	<display:column property="socialNetwork" titleKey="socialProfile.socialNetwork" />
	<display:column property="profileLink" titleKey="socialProfile.link"/>
	
	<display:column titleKey="socialProfile.edit">
		<a href="socialProfile/edit.do?id=${row.id}"> 
			<spring:message code="socialProfile.edit"/>
		</a>
	</display:column>
	
	<display:column titleKey="socialProfile.display">
		<a href="socialProfile/display.do?socialProfileId=${row.id}"> 
			<spring:message code="socialProfile.display"/>
		</a>
	</display:column>
	
	<display:column titleKey="socialProfile.delete">
		<a href="socialProfile/delete.do?id=${row.id}"> 
			<spring:message code="socialProfile.delete"/>
		</a>
	</display:column>


</display:table>

<input type="button" name="create"
				value="<spring:message code="socialProfile.create" />"
				onclick="javascript: relativeRedir('socialProfile/create.do');" />

