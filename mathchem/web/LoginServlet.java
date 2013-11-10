package mathchem.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mathchem.data.SecureDigester;
import mathchem.data.User;
import mathchem.data.UserDAO;

public class LoginServlet extends HttpServlet {
	private RequestDispatcher jsp;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		jsp.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		User user = new UserDAO().findByUsername(username);
		if (user == null) 
		{
			req.setAttribute("message", "Autentification failed");
			jsp.forward(req, resp);
			return;
		}

		String password = req.getParameter("password");

		if (password==null) {
			req.setAttribute("message", "Autentification failed: no password");
			jsp.forward(req, resp);
			return;			
		}
		String passwordDigest = SecureDigester.digest(password);
		if (!user.getPassword().equals(passwordDigest)) {
			req.setAttribute("message", "Autentification failed: bad password");
			jsp.forward(req, resp);
			return;	
		}

		HttpSession session = req.getSession();
		Long id = user.getId();
		session.setAttribute("id", id);
		String url = "list-article-items";
		resp.sendRedirect(url);
	}
}
