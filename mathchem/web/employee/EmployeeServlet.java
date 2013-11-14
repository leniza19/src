package mathchem.web.employee;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mathchem.data.ExpItem;
import mathchem.data.PeroksidDAO;

public class EmployeeServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		String EmpName = config.getInitParameter("employeeName");			
		
		//System.out.println(EmpName);
		
		if (EmpName.compareTo("irekmars") == 0) {
			jsp = context.getRequestDispatcher("/WEB-INF/jsp/employee/irekmars.jsp");
		} else if (EmpName.compareTo("spivak") == 0) {
			jsp = context.getRequestDispatcher("/WEB-INF/jsp/employee/irekmars.jsp");
		}
		
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		//List<ExpItem> zamerItems = new PeroksidDAO().getExpData();
		//req.setAttribute("zamerItems", zamerItems);
		jsp.forward(req, resp);
	}
}