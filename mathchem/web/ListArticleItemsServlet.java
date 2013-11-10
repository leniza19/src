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

import mathchem.data.ArticleItem;
import mathchem.data.ArticleItemDAO;


public class ListArticleItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/list-article-items.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		List<ArticleItem> articleItems = new ArticleItemDAO().findAll();
		req.setAttribute("articleItems", articleItems);
		jsp.forward(req, resp);
	}
}