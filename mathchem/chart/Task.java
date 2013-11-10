package mathchem.chart;

import java.math.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

public class Task {

	private String chartName;
	private String xAxis;
	private String yAxis;

	public XYSeries calculate(typePaint type) {
		XYSeries series1 = new XYSeries("simple");
		return series1;
	}

	public XYDataset calculate1(typePaint type) {
		XYDataset dataset = null;
		return dataset;
	}
	
	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

}
