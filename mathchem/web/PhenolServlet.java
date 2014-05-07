package mathchem.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

public class PhenolServlet extends HttpServlet
{
	private RequestDispatcher jsp;
	private String path="";

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		path=context.getRealPath("/");
		System.out.println(path);
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/phenol.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {			
		       
		jsp.forward(req, resp);
	}
}