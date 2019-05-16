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

    <!-- Personal data -->
    <div id="personalData">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="personalData"/></b>
        </ul>
    </div>
	<display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curricula.personalRecord" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="fullName" code="record.fullName"/>
    <display:column property="fullName" title="${fullName}" sortable="true"/>
    <spring:message var="statement" code="record.statement"/>
    <display:column property="statement" title="${statement}" sortable="true"/>
    <spring:message var="github" code="record.github"/>
    <display:column property="github" title="${github}" sortable="true"/>
    <spring:message var="phone" code="record.phone"/>
    <display:column property="phone" title="${phone}" sortable="true"/>
    <spring:message var="linkedin" code="record.linkedin"/>
    <display:column property="linkedin" title="${linkedin}" sortable="true"/>
   	<jstl:if test="${buttons}">
	<display:column>
                <input type="button" name="edit"
                    value="<spring:message code="record.edit" />"
                    onclick="relativeRedir('personalData/edit.do?personalDataId=${row.id}')" />
	</display:column>
	</jstl:if>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('personalData/display.do?personalDataId=${row.id}')" />
	</display:column>
        
	</display:table>

   <%-- Education datas --%>
   <div id="educationData">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="educationDatas"/></b>
        </ul>
    </div>
    
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curricula.educations" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="degree" code="record.degree"/>
    <display:column property="degree" title="${degree}" sortable="true"/>
    <spring:message var="institution" code="record.institution"/>
    <display:column property="institution" title="${institution}" sortable="true"/>
    <spring:message var="mark" code="record.mark"/>
    <display:column property="mark" title="${mark}" sortable="true"/>
    <spring:message var="startDate" code="record.startDate"/>
    <display:column property="startDate" title="${startDate}" sortable="true"/>
    <spring:message var="endDate" code="record.endDate"/>
    <display:column property="endDate" title="${endDate}" sortable="true"/>
    <jstl:if test="${buttons}">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('educationData/edit.do?educationDataId=${row.id}&curriculaId=${curricula.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('educationData/delete.do?educationDataId=${row.id}')" />
	</display:column>
	</jstl:if>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('educationData/display.do?educationDataId=${row.id}')" />
	</display:column>
        
	</display:table>
	<br />
	<jstl:if test="${buttons}">
	<input type="button" name="create"
    value="<spring:message code="record.create.educationData" />"
    onclick="relativeRedir('educationData/create.do?curriculaId=${curricula.id}')" />
    </jstl:if>
    <br />
    
	<%-- Position datas --%>
	<div id="positionData">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="positionDatas"/></b>
        </ul>
    </div>
   
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curricula.positions" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="title" code="record.title"/>
    <display:column property="title" title="${title}" sortable="true"/>
    <spring:message var="description" code="record.description"/>
    <display:column property="description" title="${description}" sortable="true"/>
    <spring:message var="startDate" code="record.startDate"/>
    <display:column property="startDate" title="${startDate}" sortable="true"/>
    <spring:message var="endDate" code="record.endDate"/>
    <display:column property="endDate" title="${endDate}" sortable="true"/>
   	<jstl:if test="${buttons}">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('positionData/edit.do?positionDataId=${row.id}&curriculaId=${curricula.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('positionData/delete.do?positionDataId=${row.id}')" />
	</display:column>
	</jstl:if>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('positionData/display.do?positionDataId=${row.id}')" />
	</display:column>
    
        
	</display:table>
	<br />
	<jstl:if test="${buttons}">
	 <input type="button" name="create"
    value="<spring:message code="record.create.positionData" />"
    onclick="relativeRedir('positionData/create.do?curriculaId=${curricula.id}')" />
    </jstl:if>
    <br />
    
	<%-- Miscellaneous datas --%>
	<div id="miscellaneousData">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="miscellaneousDatas"/></b>
        </ul>
    </div>
    
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curricula.miscellaneous" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="freeText" code="record.freeText"/>
    <display:column property="freeText" title="${freeText}" sortable="true"/>
    <spring:message var="attachments" code="record.attachments"/>
    <display:column property="attachments" title="${attachments}" sortable="attachments"/>
	
	<jstl:if test="${buttons}">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('miscellaneousData/edit.do?miscellaneousDataId=${row.id}&curriculaId=${curricula.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('miscellaneousData/delete.do?miscellaneousDataId=${row.id}')" />
	</display:column>
	</jstl:if>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('miscellaneousData/display.do?miscellaneousDataId=${row.id}')" />
	</display:column>
        
	</display:table>
	<br />
	<jstl:if test="${buttons}">
	<input type="button" name="create"
    value="<spring:message code="record.create.miscellaneousData" />"
    onclick="relativeRedir('miscellaneousData/create.do?curriculaId=${curricula.id}')" />
    </jstl:if>
    <br />
	
	
	<br>
	<br>
	<jstl:if test="${buttons}">
	<input type="button" name="delete"
                value="<spring:message code="record.delete.curricula" />"
                onclick="relativeRedir('curricula/delete.do?curriculaId=${curricula.id}')" />
	</jstl:if>
</jstl:if>