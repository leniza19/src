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


import mathchem.data.NewsItem;
import mathchem.data.NewsItemDAO;

public class ListNewsItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/news/list-news-items.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		String group = req.getParameter("group") ;
		
		if (group == null) {
			group = "1";
		}
			
		NewsItemDAO newsItemDAO = new NewsItemDAO();
		List<NewsItem> newsItems = newsItemDAO.findAll(group);
		Long pageNumber = newsItemDAO.getPageNumber();
		
		req.setAttribute("newsItems", newsItems);
		req.setAttribute("pageNumber", pageNumber);
		
		HttpSession session = req.getSession();		
		Long userPermission = (long) 0;
		
		if (session.getAttribute("userPermission") != null) {
			userPermission =  (Long) session.getAttribute("userPermission");
		}
				
		if ((userPermission == 1) || (userPermission==2))		
			req.setAttribute("userPermission", 1);
		else 		
			req.setAttribute("userPermission", 0);
		
		jsp.forward(req, resp);
	}
}