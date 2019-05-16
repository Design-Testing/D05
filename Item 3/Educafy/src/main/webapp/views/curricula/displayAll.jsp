<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${not empty curricula}">
    <%-- Curricula --%>

   
	<display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curricula" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <!--<spring:message var="idf" code="record.curricula.id"/>
    <display:column property="id" title="${idf}"/>-->
    
    <display:column>
	<input type="button" name="delete"
                value="<spring:message code="record.delete.curricula" />"
                onclick="relativeRedir('curricula/delete.do?curriculaId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('curricula/display.do?curriculaId=${row.id}')" />
	</display:column>
        
	</display:table>
	
	<input type="button" name="create"
                value="<spring:message code="record.create" />"
                onclick="relativeRedir('curricula/create.do')" />

   
	
</jstl:if>