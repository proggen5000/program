<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Zugriff nicht �ber Servlet --%>
<c:if test="${!valid_request}">
	<c:redirect url="error.jsp"><c:param name="error" value="Zugriff verweigert"></c:param></c:redirect>
</c:if>
	
<jsp:include page="../header.jsp"><jsp:param name="page_title" value="${username}" /></jsp:include>
<jsp:include page="../menu.jsp" />

			<h1>Profil l&ouml;schen</h1>
			<p>Sind Sie sicher, dass Sie Ihr Tasks 5000-Profil endg&uuml;ltig l&ouml;schen m&ouml;chten? Es kann danach nicht wiederhergestellt werden, zudem werden gewisse mit Ihnen verkn&uuml;pfte Elemente gel&ouml;scht:</p>
			<ul>
				<li>alle Teams, in denen Sie Teammanager sind</li>
				<li>alle Verkn&uuml;pfungen zu Ihren Aufgaben und Dateien</li>
			</ul>
			<form action="/user" method="post">
				<input type="hidden" name="id" value="${currentUser}">
				<input type="hidden" name="mode" value="remove">
				<input type="hidden" name="sure" value="true">
				<button type="submit" class="btn btn-danger"><span class="glyphicon glyphicon-ok"></span> Ja, mein Profil f&uuml;r immer l&ouml;schen</button>
				<a class="btn btn-default" href="user?mode=edit"><span class="glyphicon glyphicon-remove"></span> Nein, abbrechen</a>
			</form>
			
			<jsp:include page="../sidebar.jsp" />
<jsp:include page="../footer.jsp" />