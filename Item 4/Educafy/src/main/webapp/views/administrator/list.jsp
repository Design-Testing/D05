<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="actors" id="row" requestURI="actor/administrator/list.do" pagesize="5" class="displaytag">

	
	<display:column property="name" titleKey="actor.name"/>
	<display:column property="userAccount.username" titleKey="actor.userAccount"/>
	<jstl:set var="contains" value="false" />
	<jstl:forEach var="item" items="${row.userAccount.authorities}">
  		<jstl:if test="${item eq 'BANNED'}">
    		<jstl:set var="contains" value="true" />
  		</jstl:if>
	</jstl:forEach>
	<jstl:choose>
		<jstl:when test="${contains eq true}">	
		<display:column>
			<a href="actor/administrator/unban.do?actorId=${row.id}">
					<spring:message code="actor.unban"/>
				</a>
		</display:column>
		</jstl:when>
		<jstl:otherwise>
		<display:column>
			<a href="actor/administrator/ban.do?actorId=${row.id}">
					<spring:message code="actor.ban"/>
				</a>
		</display:column>
		</jstl:otherwise>
	</jstl:choose>
	
</display:table>
<br/>

<input type="button"
 	name="back"
 	value="<spring:message code="actor.back.welcome"/>"
	onclick="javascript:relativeRedir('/');" />