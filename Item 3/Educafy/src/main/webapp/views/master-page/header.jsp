<%--
 * header.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${bannerURL}" alt="<jstl:out value="${sysName}"/> Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  ADMINNISTRATOR  ================================================ -->
		<!-- ========================================================================================================= -->
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.configurationParameters" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="configurationParameters/administrator/edit.do"><spring:message code="master.page.configurationParameters.edit" /></a></li>
					<li><a href="dashboard/administrator/dataBreach.do"><spring:message	code="master.page.administrator.data.breach" /></a></li>
					<li><a href="configurationParameters/administrator/rebrand.do"><spring:message code="master.page.configurationParameters.rebrand" /></a></li>
					<li><a href="company/computeScores.do"><spring:message code="master.page.compute.scores" /></a></li>
				</ul>
			</li>
			
			<li><a href="finder/searching.do"><spring:message code="master.page.finder.rooky.edit" /></a></li>
			<li><a class="fNiv" href="company/list.do"><spring:message code="master.page.company.list" /></a></li>
			<li><a class="fNiv" href="provider/list.do"><spring:message code="master.page.provider.list" /></a></li>
			<li><a href="position/list.do"><spring:message code="master.page.position.list" /></a></li>
			<li><a href="subject/list.do"><spring:message code="master.page.subject.list" /></a></li>
			<li><a href="dashboard/administrator/statistics.do"><spring:message	code="master.page.dashboard" /></a></li>
			<li><a href="administrator/create.do"><spring:message	code="master.page.create.administrator" /></a></li>
			<li><a href="auditor/create.do"><spring:message	code="master.page.create.auditor" /></a></li>
				
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  TEACHER  ====================================================== -->
		<!-- ========================================================================================================= -->
		
		<security:authorize access="hasRole('TEACHER')">
			<!-- LESSONS -->
			<li><a href="lesson/teacher/myLessons.do"><spring:message code="master.page.lesson.myLessons" /></a></li>
			
			<li><a href="assesment/teacher/myAssesments.do"><spring:message code="master.page.assesment.myAssesments" /></a></li>
			
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  STUDENT  ====================================================== -->
		<!-- ========================================================================================================= -->
		
		<security:authorize access="hasRole('STUDENT')">
					
			<li><a href="lesson/student/myLessons.do"><spring:message code="master.page.lesson.myLessons" /></a></li>
			
			<li><a href="assesment/student/myAssesments.do"><spring:message code="master.page.assesment.myAssesments" /></a></li>
			
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  STUDENT  ================================================ -->
		<!-- ========================================================================================================= -->
		
		<security:authorize access="hasRole('STUDENT')">
			<li><a href="creditCard/student/list.do"><spring:message
								code="master.page.student.creditCard.list" /></a></li>
		</security:authorize>
		
		<!-- ========================================================================================================= -->
		<!-- ========================================  ANONYMOUS  ================================================ -->
		<!-- ========================================================================================================= -->
				
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="lesson/list.do"><spring:message code="master.page.lesson.list" /></a></li>
			<li><a class="fNiv" href="teacher/create.do"><spring:message code="master.page.teacher.register" /></a></li>
			<li><a class="fNiv" href="student/create.do"><spring:message code="master.page.student.register" /></a></li>
			<li><a class="fNiv" href="certifier/create.do"><spring:message code="master.page.certifier.register" /></a></li>			
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a href="subject/list.do"><spring:message code="master.page.subject.list" /></a></li>
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
						<security:authorize access="hasRole('TEACHER')">
							<li><a href="teacher/edit.do"><spring:message code="master.page.teacher.edit" /></a></li>
							<li><a href="teacher/display2.do"><spring:message code="master.page.teacher.display" /></a></li>
						</security:authorize>
						<security:authorize access="hasRole('ADMIN')">
							<li><a href="administrator/edit.do"><spring:message code="master.page.administrator.edit" /></a></li>
							<li><a href="administrator/display.do"><spring:message code="master.page.administrator.display" /></a></li>
						</security:authorize>
						<security:authorize access="hasRole('STUDENT')">
							<li><a href="student/edit.do"><spring:message code="master.page.student.edit" /></a></li>
							<li><a href="student/display.do"><spring:message code="master.page.student.display" /></a></li>
						</security:authorize>
						<security:authorize access="hasRole('CERTIFIER')">
							<li><a href="certifier/edit.do"><spring:message code="master.page.certifier.edit" /></a></li>
							<li><a href="certifier/display2.do"><spring:message code="master.page.certifier.display" /></a></li>
						</security:authorize>
					<li><a href="folder/list.do"><spring:message code="master.page.folder.list" /></a></li>
					<li><a href="socialProfile/list.do"><spring:message code="master.page.socialProfile" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

