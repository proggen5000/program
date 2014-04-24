package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import administration.MitgliederVerwaltung;
import entities.Mitglied;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean login = false;
		if(request.getSession().getAttribute("login") != null){
			login = (boolean) request.getSession().getAttribute("login");
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String mode = request.getParameter("mode");
		if(request.getAttribute("mode") != null){
			mode = (String) request.getAttribute("mode");
		}
		
		RequestDispatcher view = request.getRequestDispatcher("error.jsp");
		
		
		// Login
		if(mode.equals("login")){
			if(!login){
				if(MitgliederVerwaltung.pruefeLogin(username, password)){
					Mitglied user =  MitgliederVerwaltung.getAnhandUsernameDummy(username);
					
					HttpSession session = request.getSession(true);
					session.setAttribute("login", true);
					session.setAttribute("currentUser", user.getId());
					
					request.setAttribute("user", user);
					request.setAttribute("valid_request", true);
					view = request.getRequestDispatcher("jsp/login/login.jsp");
				} else {
					request.setAttribute("error", "Benutzername und Password stimmen nicht &uuml;berein!");
					view = request.getRequestDispatcher("error.jsp");
				}
			} else {
				request.setAttribute("error", "Sie sind bereits eingeloggt!");
				view = request.getRequestDispatcher("error.jsp");
			}
		// Logout
		} else if(mode.equals("logout")){
			if(login){
				HttpSession session = request.getSession(true);
				session.removeAttribute("login");
				session.removeAttribute("currentUser");
				
				request.setAttribute("valid_request", true);
				view = request.getRequestDispatcher("jsp/login/logout.jsp");
			} else {
				request.setAttribute("error", "Sie sind bereits ausgeloggt!");
				view = request.getRequestDispatcher("error.jsp");
			}
		}
		
		// Fehler - kein mode angegeben
		else {
			request.setAttribute("error", "Kein g&uuml;ltiger Modus!");
			view = request.getRequestDispatcher("error.jsp");
		}
		
		view.forward(request, response);
	}

}