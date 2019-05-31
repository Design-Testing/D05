<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
    uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${not empty curriculum}">
    <%-- Curriculum --%>

    <!-- Personal Record -->
    <div id="inceptionRecord">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="personalRecord"/></b>
        </ul>
    </div>
	<display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curriculum.personalRecord" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <display:column property="fullName" titleKey="record.fullname" sortable="true"/>
    <display:column property="statement" titleKey="record.statement" sortable="true"/>
    <jstl:choose>
    <jstl:when test="${buttons}">
    <security:authorize access="hasRole('TEACHER')">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('personalRecord/edit.do?personalRecordId=${row.id}&curriculumId=${curriculum.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('personalRecord/display.do?personalRecordId=${row.id}')" />
	</display:column>
	<display:column>
                <jstl:if test="${row.isDraft eq true}">
                	<input type="button" name="display"
                	value="<spring:message code="record.toFinal" />"
                	onclick="relativeRedir('personalRecord/toFinal.do?personalRecordId=${row.id}')" />
                </jstl:if>
                <jstl:if test="${row.isDraft eq false}">
                	<spring:message code="record.finalMode" />
                </jstl:if>
	</display:column>
	<display:column>
                <jstl:if test="${row.isCertified eq true}">
                	<spring:message code="record.certified" />
                </jstl:if>
                <jstl:if test="${row.isCertified eq false}">
                	<spring:message code="record.not.certified" />
                </jstl:if>
	</display:column>
	</security:authorize>
	</jstl:when>
   <jstl:when test="${buttonsAnonymous}">
			<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('personalRecord/display.do?personalRecordId=${row.id}')" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq false}">
				<display:column>
				<spring:message code="record.not.certified" />
				</display:column>
    		</jstl:if>
    </jstl:when>
     <jstl:when test="${buttonsCertifier}">
			<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq true)}">
				<display:column>
				<spring:message code="record.no.final" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq false)}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.certify" />"
                onclick="relativeRedir('personalRecord/certify.do?personalRecordId=${row.id}')" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('personalRecord/display.do?personalRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<spring:message code="record.certified" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('personalRecord/display.do?personalRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    </jstl:when>
    </jstl:choose>
	</display:table>

   <%-- Education records --%>
   <div id="educationRecord">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="educationRecords"/></b>
        </ul>
    </div>
    
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curriculum.educationRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="degree" code="record.degree"/>
    <display:column property="degree" title="${degree}" sortable="true"/>
    <spring:message var="institution" code="record.institution"/>
    <display:column property="institution" title="${institution}" sortable="true"/>
    <jstl:choose>
    <jstl:when test="${buttons}">
    <security:authorize access="hasRole('TEACHER')">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('educationRecord/edit.do?educationRecordId=${row.id}&curriculumId=${curriculum.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('educationRecord/delete.do?educationRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('educationRecord/display.do?educationRecordId=${row.id}')" />
	</display:column>
	<display:column>
                <jstl:if test="${row.isDraft eq true}">
                	<input type="button" name="display"
                	value="<spring:message code="record.toFinal" />"
                	onclick="relativeRedir('educationRecord/toFinal.do?educationRecordId=${row.id}')" />
                </jstl:if>
                <jstl:if test="${row.isDraft eq false}">
                	<spring:message code="record.finalMode" />
                </jstl:if>
	</display:column>
	<display:column>
                <jstl:if test="${row.isCertified eq true}">
                	<spring:message code="record.certified" />
                </jstl:if>
                <jstl:if test="${row.isCertified eq false}">
                	<spring:message code="record.not.certified" />
                </jstl:if>
	</display:column>
	</security:authorize>
	</jstl:when>
   <jstl:when test="${buttonsAnonymous}">
			<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('educationRecord/display.do?educationRecordId=${row.id}')" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq false}">
				<display:column>
				<spring:message code="record.not.certified" />
				</display:column>
    		</jstl:if>
    </jstl:when>
     <jstl:when test="${buttonsCertifier}">
			<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq true)}">
				<display:column>
				<spring:message code="record.no.final" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq false)}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.certify" />"
                onclick="relativeRedir('educationRecord/certify.do?educationRecordId=${row.id}')" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('educationRecord/display.do?educationRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<spring:message code="record.certified" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('educationRecord/display.do?educationRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    </jstl:when>
    </jstl:choose>
	</display:table>
	<br />
	<jstl:if test="${buttons eq true}">
	<input type="button" name="create"
    value="<spring:message code="record.create.educationRecord" />"
    onclick="relativeRedir('educationRecord/create.do?curriculumId=${curriculum.id}')" />
    </jstl:if>
    <br />
    
	<%-- Miscellaneous records --%>
	<div id="miscellaneousRecords">
        <ul style="list-style-type: disc">
            <li><b><spring:message code="miscellaneousRecords"/></b>
        </ul>
    </div>
    
    <display:table pagesize="10" class="displaytag" keepStatus="true"
               name="curriculum.miscellaneousRecords" requestURI="${requestURI}" id="row">
    <!-- Attributes -->
	
    <spring:message var="freeText" code="record.freeText"/>
    <display:column property="freeText" title="${freeText}" sortable="true"/>
    <spring:message var="attachments" code="record.attachments"/>
    <display:column property="attachments" title="${attachments}" sortable="true"/>
   <jstl:choose>
    <jstl:when test="${buttons}">
    <security:authorize access="hasRole('TEACHER')">
	<display:column>
            <input type="button" name="edit"
                value="<spring:message code="record.edit" />"
                onclick="relativeRedir('miscellaneousRecord/edit.do?miscellaneousRecordId=${row.id}&curriculumId=${curriculum.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="delete"
                value="<spring:message code="record.delete" />"
                onclick="relativeRedir('miscellaneousRecord/delete.do?miscellaneousRecordId=${row.id}')" />
	</display:column>
	<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('miscellaneousRecord/display.do?miscellaneousRecordId=${row.id}')" />
	</display:column>
	<display:column>
                <jstl:if test="${row.isDraft eq true}">
                	<input type="button" name="display"
                	value="<spring:message code="record.toFinal" />"
                	onclick="relativeRedir('miscellaneousRecord/toFinal.do?miscellaneousRecordId=${row.id}')" />
                </jstl:if>
                <jstl:if test="${row.isDraft eq false}">
                	<spring:message code="record.finalMode" />
                </jstl:if>
	</display:column>
	<display:column>
                <jstl:if test="${row.isCertified eq true}">
                	<spring:message code="record.certified" />
                </jstl:if>
                <jstl:if test="${row.isCertified eq false}">
                	<spring:message code="record.not.certified" />
                </jstl:if>
	</display:column>
	</security:authorize>
	</jstl:when>
   <jstl:when test="${buttonsAnonymous}">
			<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('miscellaneousRecord/display.do?miscellaneousRecordId=${row.id}')" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq false}">
				<display:column>
				<spring:message code="record.not.certified" />
				</display:column>
    		</jstl:if>
    </jstl:when>
     <jstl:when test="${buttonsCertifier}">
			<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq true)}">
				<display:column>
				<spring:message code="record.no.final" />
				</display:column>
    		</jstl:if>
    		<jstl:if test="${(row.isCertified eq false) and (row.isDraft eq false)}">
				<display:column>
				<input type="button" name="display"
                value="<spring:message code="record.certify" />"
                onclick="relativeRedir('miscellaneousRecord/certify.do?miscellaneousRecordId=${row.id}')" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('miscellaneousRecord/display.do?miscellaneousRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    		<jstl:if test="${row.isCertified eq true}">
				<display:column>
				<spring:message code="record.certified" />
				</display:column>
				<display:column>
			<input type="button" name="display"
                value="<spring:message code="record.display" />"
                onclick="relativeRedir('miscellaneousRecord/display.do?miscellaneousRecordId=${row.id}')" />
			</display:column>
    		</jstl:if>
    </jstl:when>
    </jstl:choose>
	</display:table>
	<br />
	<jstl:if test="${buttons}">
	<input type="button" name="create"
    value="<spring:message code="record.create.miscellaneousRecord" />"
    onclick="relativeRedir('miscellaneousRecord/create.do?curriculumId=${curriculum.id}')" />
    </jstl:if>
    <br />
	<br>
	<br>
	<jstl:if test="${buttons}">
	<input type="button" name="delete"
                value="<spring:message code="record.delete.curriculum" />"
                onclick="relativeRedir('curriculum/delete.do?curriculumId=${curriculum.id}')" />
	</jstl:if>
</jstl:if>
<jstl:if test="${empty curriculum}">
	<input type="button" name="create"
			value="<spring:message code="record.create.curriculum" />"
			onclick="relativeRedir('curriculum/create.do')" />
		<br>
</jstl:if>