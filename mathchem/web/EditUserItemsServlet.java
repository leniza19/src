package mathchem.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import mathchem.data.User;
import mathchem.data.UserDAO;

public class EditUserItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/news/edit-user-item.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		String id = req.getParameter("id") ;
		UserDAO userDAO = new UserDAO();
		if (id != null) {			
			User userItem = userDAO.findById(id);		
			req.setAttribute("newsItem", userItem);
			req.setAttribute("isNew", false);
		} else {
			User userItem = new User();
			Long id1 = userDAO.getNewId();
			userItem.setId(id1);
			userItem.setUsername("");
			userItem.setPermission((long) 3);
			userItem.setPassword("");
			
			req.setAttribute("newsItem", userItem);
			req.setAttribute("isNew", true);
		}
		
		HttpSession session = req.getSession();	
		Long userPermission = (long) 0;

		if (session.getAttribute("userPermission") != null) {
			userPermission =  (Long) session.getAttribute("userPermission");
		}

		jsp = req.getRequestDispatcher("/WEB-INF/jsp/user/edit-user-item.jsp");
		if (userPermission == 1)		
			jsp.forward(req, resp);
		else {
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/about.jsp");		
			jsp.forward(req, resp);
		}
			
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			req.setCharacterEncoding("UTF-8");
			
			// Check if cancel button was pressed.
		    String cancelButton = req.getParameter("cancel-button");
		    if (cancelButton != null)
		    {
		    	resp.sendRedirect("list-user-items");
		        return;
		    }
		      
			String username = req.getParameter("username");
			Long permission = Long.parseLong(req.getParameter("permission"));
			String password = req.getParameter("password");
			Long id = Long.parseLong(req.getParameter("id"));
			Boolean isNew = Boolean.parseBoolean(req.getParameter("isNew"));
			
			User userItem = new User();
			userItem.setUsername(username);
			userItem.setPermission(permission);
			userItem.setPassword(password);
			userItem.setId(id);
			
//			if (!isNew) {
//				new UserDAO().update(newsItem);
//			}
//			else {
//				new NewsItemDAO().insert(newsItem);
//			}
					
			
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/user/save-user-item.jsp");
			jsp.forward(req, resp);
		
	}

}