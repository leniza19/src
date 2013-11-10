package mathchem.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.lang.Object;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.*;

public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Task myTask;
	private typePaint myTypePaint;
	private String xAxis;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException {
			
        HttpSession s = request.getSession(false);
       
		myTask=(Task) s.getAttribute("myTask");
		myTypePaint=(typePaint) s.getAttribute("myTypePaint");
		xAxis = (String) s.getAttribute("xAxis");
		
		System.out.println("-----------   "+xAxis);
        response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();
		
		JFreeChart chart = getChart(myTask.getChartName());
		int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
	}

	private XYDataset createDataset() {

		//AnalyticTask myTask = new AnalyticTask(T_, C0_, p0_, epsilon_);
		final XYSeries series1 = myTask.calculate(myTypePaint);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		// dataset.addSeries(series2);
		// dataset.addSeries(series3);
		return dataset;

	}

	public JFreeChart getChart(String chartName) {
		
		final XYDataset dataset = createDataset();
		
        String yAxisLabel = new String();
        switch (myTypePaint) {
		case oxygen:
			yAxisLabel = "Коцентрация O2";
			break;
		case cocs:
			yAxisLabel = "Коцентрация кокса";
			break;
		case temprature:
			yAxisLabel = "Температура, С";
			break;
		case o2:
			yAxisLabel = "Концентрация O2";
			break;
		case h2o:
			yAxisLabel = "Концентрация H2O";
			break;
		case co:
			yAxisLabel = "Концентрация CO";
			break;
		case co2:
			yAxisLabel = "Концентрация CO2";
			break;
		default:
			break;
		}
        
//		final XYDataset dataset = myTask.calculate1(myTypePaint);
		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
				chartName, // chart title
				xAxis, // x axis label
				yAxisLabel, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);
				
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		
		plot.setBackgroundPaint(new Color(238, 203, 173)); // коралловый
//		plot.setBackgroundPaint(new Color(211, 211, 211)); // серый
		
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesStroke(
	            0, new BasicStroke(2.0f,
	            		BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[] {0.5f, 0f}, 0)
	        );
		
		renderer.setSeriesShapesVisible(1, false);
		renderer.setSeriesStroke(
	            1, new BasicStroke(1.0f,
	            		BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[] {2.0f, 5.0f}, 0)
	        );
		renderer.setSeriesShapesVisible(2, false);
		renderer.setSeriesStroke(
	            2, new BasicStroke(2.0f,
	            		BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[] {5.0f, 45.0f}, 0)
	        );
		renderer.setSeriesPaint(0, new Color(0, 0, 0));
		renderer.setSeriesPaint(1, new Color(0, 0, 0));
		renderer.setSeriesPaint(2, new Color(0, 0, 0));
		
//		renderer.setSeriesShapesVisible(0, false);
//		renderer.setSeriesShapesVisible(1, false);
//		renderer.setSeriesShapesVisible(2, false);

		plot.setRenderer(renderer);
		
		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//		rangeAxis.setRange(450, 700);
//		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		
		return chart;
		
	}
}