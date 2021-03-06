<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Zugriff nicht �ber Servlet --%>
<c:if test="${!valid_request}">
	<c:redirect url="/error.jsp"><c:param name="error" value="Zugriff verweigert" /></c:redirect>
</c:if>

<c:if test="${param.mode == 'new'}">
	<jsp:include page="../header.jsp"><jsp:param name="page_title" value="Aufgabe erstellen" /></jsp:include>
</c:if>
<c:if test="${param.mode == 'edit'}">
	<jsp:include page="../header.jsp"><jsp:param name="page_title" value="${task.name}" /></jsp:include>
</c:if>

<jsp:include page="../menu.jsp"><jsp:param name="menu" value="teams" /></jsp:include>
		
			<c:if test="${param.mode == 'new'}">
				<ol class="breadcrumb">
					<li><a href="${pageContext.request.contextPath}/">Start</a></li>
					<li><a href="${pageContext.request.contextPath}/team?mode=view&id=${team.id}">${team.name}</a></li>
					<li class="active"></li>
				</ol>
			</c:if>
			
			<c:if test="${param.mode == 'edit'}">
				<ol class="breadcrumb">
					<li><a href="${pageContext.request.contextPath}/">Start</a></li>
					<li><a href="${pageContext.request.contextPath}/team?mode=view&id=${task.taskGroup.team.id}">${task.taskGroup.team.name}</a></li>
					<li class="active"></li>
				</ol>
			</c:if>
					
			<c:if test="${param.mode == 'new'}"><h1>Aufgabe erstellen</h1></c:if>
			<c:if test="${param.mode == 'edit'}"><h1>Aufgabe bearbeiten</h1></c:if>
			
			<form class="form" action="${pageContext.request.contextPath}/task" method="post">
		  		<div class="form-group col-xs row">
		  			<div class="col-md-6">
		  				<label for="name"><span class="glyphicon glyphicon-time"></span> Name*</label>
						<input id="name" name="name" type="text" class="form-control input-lg" value='${task.name}' />
		  			</div>
		  			<div class="col-md-6">
		  				<label for="group"><span class="glyphicon glyphicon-tag"></span> Aufgabengruppe*</label>
						<select name="group" size="1" class="form-control input-lg">
							<c:forEach var="group" items="${taskGroups}">
								<c:if test="${group.id == task.taskGroup.id}">
									<option value="${group.id}" selected>${group.name}</option>
								</c:if>
								<c:if test="${group.id != task.taskGroup.id}">
									<option value="${group.id}">${group.name}</option>
								</c:if>
							</c:forEach>
						</select>
		  			</div>
				</div>
				<div class="form-group col-xs">
					<label for="description"><span class="glyphicon glyphicon-align-left"></span> Beschreibung</label>
					<textarea id="description" name="description" class="form-control" rows="5">${task.description}</textarea>
				</div>
				<div class="form-group col-xs row">
				    	<div class="col-xs-4">
				    		<label><span class="glyphicon glyphicon-calendar"></span> Erstellungsdatum</label>
				    		<c:if test="${param.mode == 'new'}">
				    			<p class="form-control-static"><fmt:formatDate pattern="dd.MM.yyyy" value="${today}" /></p>
				    		</c:if>
				    		<c:if test="${param.mode == 'edit'}">
				    			<p class="form-control-static"><fmt:formatDate pattern="dd.MM.yyyy" value="${task.dateObject}" /></p>
				    		</c:if>
				    	</div>
				    	<div class="col-xs-4">
				    		<label for="deadline" data-toggle="tooltip" data-placement="right" data-original-title="Falls die Datumsauswahl nicht angezeigt wird (Firefox und Internet Explorer): Geben Sie das Datum im Format jjjj-mm-tt an (z.B. 2015-04-21)."><span class="glyphicon glyphicon-bell"></span> Deadline</label>
							<input id="deadline" name="deadline" type="date" class="form-control" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${task.deadlineObject}" />" placeholder="jjjj-mm-tt" />
				    	</div>
				    	<div class="col-xs-4">
				    		<label for="status"><span class="glyphicon glyphicon-dashboard"></span> Status</label> <small>(%)</small>
							<input id="status" name="status" type="number" min="0" max="100" class="form-control" value="${task.status}" />								
				    	</div>
				</div>
				<div class="form-group col-xs">
					<label for="users"><span class="glyphicon glyphicon-user"></span> Mitglieder</label> <span class="badge" data-toggle="tooltip" data-placement="right" data-original-title="W&auml;hlen Sie hier die Mitglieder aus, welche die Aufgabe bearbeiten sollen.">?</span>
					<div class="checkbox">
						<c:forEach var="userSelected" items="${usersSelected}">							
							<label><input type="checkbox" name="users" value="${userSelected.id}" checked> ${userSelected.name} (${userSelected.firstName} ${userSelected.secondName}) </label><br />
						</c:forEach>
						<c:forEach var="user" items="${users}">
							<label><input type="checkbox" name="users" value="${user.id}"> ${user.name} (${user.firstName} ${user.secondName}) </label><br />
						</c:forEach>
					</div>
				</div>
				
				<table class="table table-hover col-xs">
		  			<thead>
		  			<tr>
				        <th><span class="glyphicon glyphicon-paperclip"></span> Verkn&uuml;pfte Dateien</th>
				        <th>Gr&ouml;&szlig;e</th>
				        <th>Verkn&uuml;pfung l&ouml;schen</th>
				    </tr>
				    </thead>
		  			<tbody>
		  				<c:if test="${fn:length(files) > 0}">
		  					<c:forEach var="file" items="${files}">
			  					<tr>
							        <td><span class="glyphicon glyphicon-file"></span> <a href="file?mode=view&id=${file.id}">${file.name}</a></td>
							        <td>${file.size} KB</td>
							        <td><input type="checkbox" name="deleteFiles" value="${file.id}"></td>
							    </tr>
			  				</c:forEach>
		  				</c:if>
		  				<c:if test="${fn:length(files) == 0}">
		  					<tr><td colspan="3">Sie haben noch keine Dateien zu dieser Aufgabe zugeordnet. Dies k&ouml;nnen Sie in den jeweiligen Dateieigenschaften tun.</td></tr>
		  				</c:if>
					</tbody>
				</table>
				
				<c:if test="${param.mode == 'new'}"><input type="hidden" name="mode" value="new" /></c:if>
				<c:if test="${param.mode == 'edit'}">
					<input type="hidden" name="mode" value="edit" />
					<input type="hidden" name="id" value="${task.id}" />
				</c:if>
				
				<div class="form-group col-xs">
					<c:if test="${param.mode == 'new'}">
						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Erstellen</button>
						<a href="${pageContext.request.contextPath}/team?mode=view&id=${team.id}" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span> Abbrechen</a>
					</c:if>
					<c:if test="${param.mode == 'edit'}">
						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Speichern</button>
						<a href="${pageContext.request.contextPath}/task?mode=view&id=${task.id}" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span> Abbrechen</a>
						<a href="${pageContext.request.contextPath}/task?mode=remove&id=${task.id}" class="btn btn-danger pull-right"><span class="glyphicon glyphicon-remove"></span> Aufgabe l&ouml;schen</a>
					</c:if>
				</div>
			</form>
			<jsp:include page="../sidebar.jsp" />

<jsp:include page="../footer.jsp" />
