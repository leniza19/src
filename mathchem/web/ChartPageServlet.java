package mathchem.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mathchem.chart.IsotermTask;
import mathchem.chart.MoveTask;
import mathchem.chart.Task;
import mathchem.chart.ZernoTask;
import mathchem.chart.typePaint;

public class ChartPageServlet extends HttpServlet {

	private RequestDispatcher jsp;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/chart-page.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		jsp.forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String typeSloi = req.getParameter("typeSloi");
		if (typeSloi.equals("isotermSloi")) {
			double T_ = Double.parseDouble(req.getParameter("T_"));
			double C0_ = Double.parseDouble(req.getParameter("C0_"));
			double p0_ = Double.parseDouble(req.getParameter("p0_"));
			double epsilon_ = Double.parseDouble(req.getParameter("epsilon_"));
			String namePaint = req.getParameter("typePaint");
			typePaint myTypePaint = typePaint.none;
			Task myTask = new IsotermTask(T_, C0_, p0_, epsilon_);
			myTask.setChartName("Изотермический режим");		
			
			if (namePaint.equals("oxygen")) 
				myTypePaint = typePaint.oxygen;
			 else if (namePaint.equals("cocs")) 
				myTypePaint = typePaint.cocs;

			req.setAttribute("myTask", myTask);
			req.setAttribute("myTypePaint", myTypePaint);
			req.setAttribute("xAxis", "Относительная длина слоя");
			jsp.forward(req, resp);
		} 
		else if (typeSloi.equals("moveSloi")) {
			double Tv0 = Double.parseDouble(req.getParameter("Tv0"));
			System.out.println("Tv0=" + Tv0);
			double Gv0 = Double.parseDouble(req.getParameter("Gv0"));
			System.out.println("Gv0=" + Gv0);
			double Gv1 = Double.parseDouble(req.getParameter("Gv1"));
			System.out.println("Gv1=" + Gv1);
			double c00 = Double.parseDouble(req.getParameter("c00"));
			System.out.println("c00=" + c00);
			double Sp1 = Double.parseDouble(req.getParameter("Sp1"));
			String namePaint = req.getParameter("typePaint");
			typePaint myTypePaint = typePaint.none;
			if (namePaint.equals("temprature"))
				myTypePaint = typePaint.temprature;
			else if (namePaint.equals("cocs"))
				myTypePaint = typePaint.cocs; 
			Task myTask = new MoveTask(Tv0, Gv0, Gv1, c00, Sp1);
			myTask.setChartName("Движущийся слой");
			req.setAttribute("myTask", myTask);
			req.setAttribute("myTypePaint", myTypePaint);
			req.setAttribute("xAxis", "Длина регенератора, м");
			jsp.forward(req, resp);
		}
		else if (typeSloi.equals("zerno")) {
						
			double c00 = Double.parseDouble(req.getParameter("c00_"));
			System.out.println("c00=" + c00);
			double qc0 = Double.parseDouble(req.getParameter("qc0_"));
			System.out.println("qc0=" + qc0);
			double Rz = Double.parseDouble(req.getParameter("Rz_"));
			System.out.println("Rz=" + Rz);
			double T0 = Double.parseDouble(req.getParameter("T0_"));
			System.out.println("T0=" + T0);

		    
			String namePaint = req.getParameter("typePaint");
			typePaint myTypePaint = typePaint.none;
			if (namePaint.equals("temp"))
				myTypePaint = typePaint.temprature;
			else if (namePaint.equals("o2"))
				myTypePaint = typePaint.o2;
			else if (namePaint.equals("h2o"))
				myTypePaint = typePaint.h2o;
			else if (namePaint.equals("co2"))
				myTypePaint = typePaint.co2;
			else if (namePaint.equals("co"))
				myTypePaint = typePaint.co;
			
			
			Task myTask = new ZernoTask(c00, qc0, Rz, T0);
			myTask.setChartName("Зерно катализатора");
			req.setAttribute("myTask", myTask);
			req.setAttribute("myTypePaint", myTypePaint);
			req.setAttribute("xAxis", "Относительная радиус зерна");
			jsp.forward(req, resp);
		}

	}

}
