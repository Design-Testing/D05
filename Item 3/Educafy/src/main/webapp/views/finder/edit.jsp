
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="finder/student/edit.do" modelAttribute="finder" method="POST">
    <form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="lessons"/>
	<form:hidden path="creationDate"/>
	
	<acme:textbox code="finder.keyword" path="keyword"/><br>
	<form:label path="subjectLevel">
		<spring:message code="finder.subjectLevel" />: </form:label>
	<form:select path="subjectLevel">
		<form:options path="make" items="${subjectLevels}" />
	</form:select>
	<form:errors cssClass="error" path="subjectLevel" />
	<br><br>
	<form:label path="subjectName">
		<spring:message code="finder.subjectName" />: </form:label>
	<form:select path="subjectName">
		<form:options path="subjectName" items="${subjectNames}" />
	</form:select>
	<form:errors cssClass="error" path="subjectName" />
	<br><br>
	<acme:textbox code="finder.teacherName" path="teacherName"/><br>
	
	<input type="submit" name="save" value="<spring:message code="finder.search" />" />
	<input type="submit" name="clear" value="<spring:message code="finder.clear" />" />
	<br>
	<br>
	<spring:message code="finder.results" />
	<br>
	
	
<display:table name="${finder.lessons}" id="row" requestURI="/finder/student/edit.do" pagesize="15" class="displaytag">
	<display:column property="title" titleKey="lesson.title" />
	<display:column property="ticker" titleKey="lesson.ticker" />
	
	<display:column>
		<acme:link url="lesson${rolURL}/display.do?lessonId=${row.id}"
			code="lesson.display" />
	</display:column>
</display:table>
</form:form>