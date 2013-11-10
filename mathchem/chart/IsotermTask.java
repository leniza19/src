package mathchem.chart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
public class IsotermTask extends Task {

	int N=100001;
	static double E,h;
	int t=1;
	double N2=0.0122;

	private double T;
	private double C0;//начальное содержание кислорода
	private double p0;//начальное содержание кокса
	private double epsilon;//пористость



	public IsotermTask (double T_,double C0_,double p0_,double epsilon_)
	{
		setT(T_);
		setP0(p0_);
		setC0(C0_);
		setEpsilon(epsilon_);
	}

	private double p(double l,double tau,double p0, double k, double N1, double N2, double k1, double C0)
	{
		return p0/(1-Math.exp(-(k*l)/N2)+Math.exp(-((k1*C0)/p0)*((N1*l)/N2-tau)-(k*l/N2)));
	}
	private double C(double l,double tau,double p0, double k, double N1, double N2, double k1, double C0)
	{
		return C0/(Math.exp((k*l)/N2-1)*Math.exp((k1*C0)*((N1/N2)*l-tau)/p0)+1);
	}
	private double Simmons(double a,double b,double tau,double p0, double k, double N1, double N2, double k1, double C0)
	{
		double integral=0.0,h;
//		h=(b-a)/50.0;
		h=(b-a)/10000.0;
		int i=1;
		for(double x=a+h;x<b;x+=h)
		{
			if(i%2!=0) integral=integral+4*p(x,tau,p0,k,N1,N2,k1,C0);
			else integral=integral+2*p(x,tau,p0,k,N1,N2,k1,C0);
			++i;
		}
		integral=integral+p(a,tau,p0,k,N1,N2,k1,C0)+p(b,tau,p0,k,N1,N2,k1,C0);
		return integral*(b-a)/(3*10000.0);
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}

	public double getC0() {
		return C0;
	}

	public void setC0(double c0) {
		C0 = c0;
	}

	public double getP0() {
		return p0;
	}

	public void setP0(double p0) {
		this.p0 = p0;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public XYSeries calculate(typePaint myTypePaint) {
		double T_in=T-273;
		double k1=0.0046;
		double gamma=0.0012;//плотность газа г/см^3
		double N2=0.0122, N1=gamma*epsilon;
		double bet=0.0115;//порозность слоя
		double k=(k1*gamma)/bet;
		double S=4;
		double l=50;
		double qc0=gamma*S*Simmons(0,l,0,p0,k,N1,N2,k1,C0);

//		for (double tau = 0; tau < 200; tau+=0.5)
//		{
//			series1.add(tau,(gamma*S*Simmons(0,l,tau,p0,k,N1,N2,k1,C0))/qc0);
//			//series1.add(tau,(gamma*S*Simmons(0,l,tau,p0,(0.0046*gamma)/bet,N1,N2,0.0046,C0))/qc0);
//		}
		final XYSeries series1=new XYSeries("");
		if(myTypePaint==typePaint.cocs)
		{
			series1.setKey("Изменение кокса");
			for(l=0;l<=50;l+=1)
				series1.add(l/50,p(l,T,p0,k,N1,N2,k1,C0)/p0);
		}
		else if(myTypePaint==typePaint.oxygen)
		{
			series1.setKey("Изменение кислорода");
			for(l=0;l<=40;l+=1)
				series1.add(l/40,C(l,T,p0,k,N1,N2,k1,C0)/C0);
		}
		return series1;
	}

	public XYDataset calculate1(typePaint type) {
		// XYDataset dataset = null;
		T=20;
		C0=0.23;//начальное содержание кислорода
		p0=0.05314;//начальное содержание кокса
		epsilon=0.42;//пористость

		final XYSeries series1 = calculate(type);
		series1.setKey("t = 20 мин");
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		T=75;
		C0=0.23;//начальное содержание кислорода
		p0=0.05314;//начальное содержание кокса
		epsilon=0.42;//пористость

		final XYSeries series2 = calculate(type);
		series2.setKey("t = 75 мин");

		dataset.addSeries(series2);
		
		T=200;
		C0=0.23;//начальное содержание кислорода
		p0=0.05314;//начальное содержание кокса
		epsilon=0.42;//пористость

		final XYSeries series3 = calculate(type);
		series3.setKey("t = 200 мин");

		dataset.addSeries(series3);

//		T=75;
//		C0=0.23;//начальное содержание кислорода
//		p0=0.05314;//начальное содержание кокса
//		epsilon=0.42;//пористость
//		final XYSeries series4 = calculate(type);
//		series4.setKey("t = 75 мин");
//		dataset.addSeries(series4);
//		
//		T=100;
//		C0=0.23;//начальное содержание кислорода
//		p0=0.05314;//начальное содержание кокса
//		epsilon=0.42;//пористость
//		final XYSeries series5 = calculate(type);
//		series5.setKey("t = 100 мин");
//		dataset.addSeries(series5);
//		
//		T=200;
//		C0=0.23;//начальное содержание кислорода
//		p0=0.05314;//начальное содержание кокса
//		epsilon=0.42;//пористость
//		final XYSeries series6 = calculate(type);
//		series6.setKey("t = 200 мин");
//		dataset.addSeries(series6);
		
		return dataset;
	}
}
