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


<display:table name="rookies" id="row"
	requestURI="rooky/list.do" pagesize="5"
	class="displaytag">


	<display:column property="name" titleKey="actor.name" />
		
	<display:column property="email" titleKey="actor.email" />

	
	<display:column>
		<a href="rooky/displayTabla.do?rookyId=${row.id}"> <spring:message
				code="rooky.display" />
		</a>
	</display:column>


</display:table>
