package mathchem.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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
	private String path="";

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		path=context.getRealPath("/");
		System.out.println(path);
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
		
		PrintWriter out = new PrintWriter(path+"\\json\\exp.json");
		String jsonText = "";
		
		Iterator it=zamerItems.iterator();
			
		jsonText = jsonText + "[";
				
        while(it.hasNext())
        {
        	ExpItem expItem=(ExpItem)it.next();
        	jsonText = jsonText + "{\"idexp\": " + expItem.getIdExp() + ", \n" + 
        	"\"time\": " + expItem.getTime() + ",\n " + 
        	"\"conc\": " + expItem.getConcentration() + "},\n";        	        	
        }
		
        jsonText = jsonText.substring(0, jsonText.length() - 2);
        jsonText = jsonText + "]";
        
        out.println(jsonText);
		out.close();
		
		jsp.forward(req, resp);
	}
}