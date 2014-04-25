package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import administration.MitgliederTeams;
import administration.MitgliederVerwaltung;
import administration.TeamVerwaltung;
import entities.Mitglied;

@WebServlet("/user")
public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public User() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean login = false;
		if(request.getSession().getAttribute("login") != null){
			login = Boolean.parseBoolean(request.getSession().getAttribute("login").toString());
		}
		
		long currentUser = -1;
		if(request.getSession().getAttribute("currentUser") != null){
			try {
				currentUser = Long.parseLong(request.getSession().getAttribute("currentUser").toString());
			} catch (NullPointerException e){
				request.setAttribute("error", e);
			} 
		}
		
		long id = -1;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e){
			request.setAttribute("error", e);
		}
		
		long teamId = -1;
		try {
			id = Long.parseLong(request.getParameter("teamId"));
		} catch (NumberFormatException e){
			request.setAttribute("error", e);
		}
		
		String mode = request.getParameter("mode");
		if(request.getAttribute("mode") != null){
			mode = (String) request.getAttribute("mode");
		}
		
		RequestDispatcher view = request.getRequestDispatcher("error.jsp");

		
		// Fehler - kein Login
		if(!login){
			request.setAttribute("error", "Sie sind nicht eingeloggt!");
			view = request.getRequestDispatcher("error.jsp");
		}

		// Profil ansehen
		else if(mode.equals("view")){
			if(id != -1){
				Mitglied user = MitgliederVerwaltung.getMitgliedWithId(id); // TODO
				
				request.setAttribute("user", user);
				request.setAttribute("teams", TeamVerwaltung.getListeVonMitglied(id)); // TODO
				request.setAttribute("valid_request", true);
				view = request.getRequestDispatcher("jsp/user/userView.jsp");
			} else {
				request.setAttribute("error", "Ung&uuml;ltige Benutzer-ID!");
				view = request.getRequestDispatcher("error.jsp");
			}
		}
		
		// Profil bearbeiten (Formular)
		else if(mode.equals("edit")){
			Mitglied user = MitgliederVerwaltung.getMitgliedWithId(currentUser); // TODO
			
			request.setAttribute("user", user);
			request.setAttribute("valid_request", true);
			view = request.getRequestDispatcher("jsp/user/userEdit.jsp");
		}
		
		// Profil loeschen
		else if(mode.equals("remove")){
			Mitglied user = MitgliederVerwaltung.getMitgliedWithId(currentUser); // TODO
			
			request.setAttribute("user", user);			
			request.setAttribute("valid_request", true);
			view = request.getRequestDispatcher("jsp/user/userRemove.jsp");
		}
		
		// Team verlassen
		else if(mode.equals("leaveTeam")){
			if(MitgliederVerwaltung.istMitgliedInTeam(currentUser, teamId)){
				Mitglied user = MitgliederVerwaltung.getMitgliedWithId(currentUser); // TODO
				request.setAttribute("user", user);
				request.setAttribute("team", TeamVerwaltung.getTeamWithId(teamId));
				request.setAttribute("valid_request", true);
				view = request.getRequestDispatcher("jsp/user/userLeaveTeam.jsp");
			} else {
				if(TeamVerwaltung.getTeamWithId(teamId) == null){
					request.setAttribute("error", "Dieses Team existiert nicht!");
				} else {
					request.setAttribute("error", "Sie sind kein Mitglied des Teams " + TeamVerwaltung.getTeamWithId(teamId) + "!");
				}
				view = request.getRequestDispatcher("error.jsp");
			}
		}
		
		// Fehler - kein mode angegeben
		else {
			request.setAttribute("error", "Ung&uuml;ltiger Modus!");
			view = request.getRequestDispatcher("error.jsp");
		}
		
		view.forward(request, response);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean login = false;
		if(request.getSession().getAttribute("login") != null){
			login = Boolean.parseBoolean(request.getSession().getAttribute("login").toString());
		}
		
		long currentUser = -1;
		if(request.getSession().getAttribute("currentUser") != null){
			try {
				currentUser = Long.parseLong(request.getSession().getAttribute("currentUser").toString());
			} catch (NullPointerException e){
				request.setAttribute("error", e);
			} 
		}
		
		long id = -1;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e){
			request.setAttribute("error", e);
		}
		
		long teamId = -1;
		try {
			id = Long.parseLong(request.getParameter("teamId"));
		} catch (NumberFormatException e){
			request.setAttribute("error", e);
		}
		
		String mode = request.getParameter("mode");
		if(request.getAttribute("mode") != null){
			mode = (String) request.getAttribute("mode");
		}
		
		String sure = request.getParameter("sure");
		
		RequestDispatcher view = request.getRequestDispatcher("error.jsp");

		
		// Fehler - kein Login
		if(!login){
			request.setAttribute("error", "Sie sind nicht eingeloggt!");
			view = request.getRequestDispatcher("error.jsp");
		}

		// Profil bearbeiten (Aktion)
		if(mode.equals("edit")){
			String password = request.getParameter("password");
			String passwordRepeat = request.getParameter("passwordRepeat");
			
			
			Mitglied user = new Mitglied();
			// user.setId(id); // TODO noetig?
			user.setUsername(request.getParameter("username"));
			user.setVorname(request.getParameter("vorname"));
			user.setNachname(request.getParameter("nachname"));
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			// user.setRegdatum(new Date().getTime()); // TODO noetig?
			
			Mitglied userUpdated = MitgliederVerwaltung.bearbeiten(user);
			
			request.setAttribute("user", userUpdated);
			request.setAttribute("valid_request", true);
			view = request.getRequestDispatcher("jsp/user/userEdit.jsp");
		}
		
		// Profil loeschen (Aktion)
		else if(mode.equals("remove")){
			Mitglied user = MitgliederVerwaltung.getMitgliedWithId(currentUser); // TODO
			
			if(!sure.equals("true")){
				request.setAttribute("user", user);			
				request.setAttribute("valid_request", true);
				view = request.getRequestDispatcher("jsp/user/userRemove.jsp");
			} else if(sure.equals("true")){
				MitgliederVerwaltung.loeschen(currentUser);
				HttpSession session = request.getSession(true);
				session.removeAttribute("login");
				session.removeAttribute("currentUser");
				
				request.setAttribute("title", "Profil gel&ouml;scht");
				request.setAttribute("message", "Sie haben Ihr Profil endg&uuml;ltig gel&ouml;scht!<br />Auf Wiedersehen. :'(");
				request.setAttribute("valid_request", true);
				view = request.getRequestDispatcher("success.jsp");
			}
		}
		
		// Team verlassen (Aktion)
		else if(mode.equals("leaveTeam")){
			Mitglied user = MitgliederVerwaltung.getMitgliedWithId(currentUser); // TODO
			entities.Team team = TeamVerwaltung.getTeamWithId(teamId);
			
			if(MitgliederVerwaltung.istMitgliedInTeam(user.getId(), team.getId()) && sure.equals("true")){
				if(MitgliederTeams.austreten(user.getId(), team.getId())){
					request.setAttribute("user", user);
					request.setAttribute("team", TeamVerwaltung.getTeamWithId(teamId));
					
					request.setAttribute("title", "Team verlassen");
					request.setAttribute("message", "Sie haben das Team <b>" + team.getName() + "</b> verlassen.");
					request.setAttribute("valid_request", true);
					view = request.getRequestDispatcher("success.jsp");
				} else {
					request.setAttribute("error", "Sie konnten nicht aus dem Team entfernt werden! :(");
					view = request.getRequestDispatcher("error.jsp");
				}
			} else {
				if(TeamVerwaltung.getTeamWithId(id) != null){
					request.setAttribute("error", "Sie sind kein Mitglied des Teams " + TeamVerwaltung.getTeamWithId(teamId).getName() + "!");
				} else {
					request.setAttribute("error", "Dieses Team existiert nicht!");
				}
				view = request.getRequestDispatcher("error.jsp");
			}
		}
		
		// Fehler - kein mode angegeben
		else {
			request.setAttribute("error", "Ung&uuml;ltiger Modus!");
			view = request.getRequestDispatcher("error.jsp");
		}
		
		view.forward(request, response);
	}

}
