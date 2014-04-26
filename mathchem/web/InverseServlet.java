package mathchem.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		if (startGA == null) {
			req.setAttribute("b1", 0.9);
			req.setAttribute("b2", 0.9);
			req.setAttribute("b3", 0.9);
			req.setAttribute("d", 0.9);
		} else	if (startGA.equals("yes")) {
			
			OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
			octave.eval("pkg load ga");
			OctaveDouble x = new OctaveDouble(new double[] { 3 }, 1, 1);
			octave.put("x", x);
			String func = "" //
			+ "function res = rastrigin(x,a=5)" //
			+ " s = 0;" +
			"for i =1:length (x) " +
			"s += x(i)^2+ a*(1-cos(2*pi*x(i)));" +
			"endfor; " +
			"res=s;" //
			+ "endfunction" //
			+"";
			
			
			octave.eval(func);
			octave.eval("[b,fval]=ga(@rastrigin,3);");
			OctaveDouble b = octave.get(OctaveDouble.class, "b");
			OctaveDouble d = octave.get(OctaveDouble.class, "fval");
			
			octave.close();
			System.out.println("Java: b = " + b.get(1, 1)+" "+b.get(1, 2)+" "+b.get(1, 2));
			System.out.println("Java: d = " + d.get(1, 1));
			
			
			req.setAttribute("b1", b.get(1,1));
			req.setAttribute("b2", b.get(1,2));
			req.setAttribute("b3", b.get(1,3));
			
			req.setAttribute("d", d.get(1,1));
		} 
			
		jsp.forward(req, resp);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        File f = new File(path+"\\octave\\prim.txt");
        BufferedReader fin = new BufferedReader(new FileReader(f));
        String name;
        String line;
        //System.out.println("Print File "+f.getName()+"? y/n");
        //name = br.readLine();
        //if(name.equals("y"))
        while ((line = fin.readLine()) != null) System.out.println(line);
	}

	 
	
	
}