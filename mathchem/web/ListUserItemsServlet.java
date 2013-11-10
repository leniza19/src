package mathchem.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mathchem.data.User;
import mathchem.data.UserDAO;

public class ListUserItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/user/list-user-items.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		UserDAO userDAO = new UserDAO();
		List<User> userItems = userDAO.findAll();
		
		req.setAttribute("userItems", userItems);
		
		HttpSession session = req.getSession();		
		Long userPermission = (long) 0;
		
		if (session.getAttribute("userPermission") != null) {
			userPermission =  (Long) session.getAttribute("userPermission");
		}
				
		jsp = req.getRequestDispatcher("/WEB-INF/jsp/user/list-user-items.jsp");
		if (userPermission == 1)		
			jsp.forward(req, resp);
		else {
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/about.jsp");		
			jsp.forward(req, resp);
		}
						
	}
}