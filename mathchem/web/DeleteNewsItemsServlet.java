package mathchem.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import mathchem.chart.IsotermTask;
import mathchem.chart.MoveTask;
import mathchem.chart.Task;
import mathchem.chart.ZernoTask;
import mathchem.chart.typePaint;
import mathchem.data.NewsItem;
import mathchem.data.NewsItemDAO;

public class DeleteNewsItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/news/delete-news-item.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		String id = req.getParameter("id") ;
		NewsItemDAO newsItemDAO = new NewsItemDAO();
		if (id != null) {			
			NewsItem newsItem = newsItemDAO.find(id);		
			req.setAttribute("newsItem", newsItem);
		} else {
			NewsItem newsItem = new NewsItem();
			Long id1 = newsItemDAO.getNewId();
			newsItem.setId(id1);
			newsItem.setTitle("");
			newsItem.setContent("");
			Calendar currentDate = Calendar.getInstance(); //Get the current date
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
			String dateNow = formatter.format(currentDate.getTime());
			newsItem.setNewsDate(dateNow);
			req.setAttribute("newsItem", newsItem);
		}
		
		HttpSession session = req.getSession();	
		Long userPermission = (long) 0;

		if (session.getAttribute("userPermission") != null) {
			userPermission =  (Long) session.getAttribute("userPermission");
		}

		jsp = req.getRequestDispatcher("/WEB-INF/jsp/news/delete-news-item.jsp");
		if (userPermission == 1 || userPermission==2)		
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
		    	resp.sendRedirect("list-news-items?group=1");
		        return;
		    }
		      
			Long id = Long.parseLong(req.getParameter("id"));
			
			new NewsItemDAO().delete(id);			
			
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/news/save-news-item.jsp");
			jsp.forward(req, resp);
		
	}
}