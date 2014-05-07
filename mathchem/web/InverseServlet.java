package mathchem.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveDouble;

public class InverseServlet extends HttpServlet
{
	private RequestDispatcher jsp;
	
	private static String path="";
	
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/inverse.jsp");
		path=context.getRealPath("/");	
		System.out.println(path);
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {				
        
		String startGA = req.getParameter("startGA");
		System.out.println(startGA);
		req.setAttribute("startGA", startGA);
		if (startGA == null) {
			req.setAttribute("c1", 1.0);
			req.setAttribute("c2", 2.0);
			req.setAttribute("c3", 3.0);
			req.setAttribute("E1", 4.0);
			req.setAttribute("k1", 5.0);
			req.setAttribute("E2", 6.0);
			req.setAttribute("k2", 7.0);
			req.setAttribute("E3", 8.0);
			req.setAttribute("k3", 9.0);
			req.setAttribute("fval", 10.0);
			req.setAttribute("startGA", "no");
		} else	if (startGA.equals("yes")) {
			
			OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
			octave.eval("pkg load ga");
			OctaveDouble x = new OctaveDouble(new double[] { 3 }, 1, 1);
			octave.put("x", x);
						
			File f = new File(path+"\\octave\\obj_general.txt");
	        BufferedReader fin = new BufferedReader(new FileReader(f));
	        String line, obj_general;
	        obj_general = "";
	        while ((line = fin.readLine()) != null) {
	        	obj_general = obj_general + line;
	        }        	        		        
	        fin.close();
	        	        
	        f = new File(path+"\\octave\\arren.m");
	        fin = new BufferedReader(new FileReader(f));
	        String arren;
	        arren = "";
	        while ((line = fin.readLine()) != null) {
	        	arren = arren + line;
	        }        	        		        
	        fin.close();	        	        
			
			f = new File(path+"\\octave\\ode_perox.m");
	        fin = new BufferedReader(new FileReader(f));
	        String ode_perox;
	        ode_perox = "";
	        while ((line = fin.readLine()) != null) {
	        	ode_perox = ode_perox + line;
	        }        	        		        
	        fin.close();			
	        			
	        octave.eval("model = 5;");
	        octave.eval("catal = 213;");
	        
			octave.eval(obj_general);
			octave.eval(arren);
			octave.eval(ode_perox);
			
			octave.eval("options = gaoptimset('PopulationSize', 10,'Generations', 1);");			
			octave.eval("[b,fval]=ga(@obj_general,9,[],[],[],[],[50 50 50 0 0 0 0 0 0],[100 100 100 100 100 100 100 100 100],[],options);");			
			OctaveDouble c = octave.get(OctaveDouble.class, "b");
			OctaveDouble fval = octave.get(OctaveDouble.class, "fval");
			
			octave.close();
			//System.out.println("Java: b = " + b.get(1, 1)+" "+b.get(1, 2)+" "+b.get(1, 2));
			//System.out.println("Java: d = " + d.get(1, 1));
			
			
			req.setAttribute("c1", c.get(1,1));
			req.setAttribute("c2", c.get(1,2));
			req.setAttribute("c3", c.get(1,3));
			req.setAttribute("E1", c.get(1,4));
			req.setAttribute("k1", c.get(1,5));
			req.setAttribute("E2", c.get(1,6));
			req.setAttribute("k2", c.get(1,7));
			req.setAttribute("E3", c.get(1,8));
			req.setAttribute("k3", c.get(1,9));
			
			
			req.setAttribute("fval", fval.get(1,1));
		} 
			
		jsp.forward(req, resp);		     
	}
	
}