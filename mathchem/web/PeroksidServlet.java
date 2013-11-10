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

import mathchem.data.CatalystItem;
import mathchem.data.ConstItem;
import mathchem.data.ExpItem;
import mathchem.data.PeroksidDAO;

public class PeroksidServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/peroksid.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		List<ExpItem> zamerItems = new PeroksidDAO().getExpData();
		List<CatalystItem> catalystItems = new PeroksidDAO().getCatalysts();
		List<ConstItem> constItems = new PeroksidDAO().getConstants();
		
		req.setAttribute("zamerItems", zamerItems);
		req.setAttribute("catalystItems", catalystItems);
		req.setAttribute("constItems", constItems);
		jsp.forward(req, resp);
	}
}