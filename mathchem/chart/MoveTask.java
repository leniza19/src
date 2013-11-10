package mathchem.chart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MoveTask extends Task {
	private double TV0;
	private double Gv0;//
	private double Gv1;//
	/** начальная концентрация */
	private double c00;//
	private double Sp1;
	int n = 100, m = 200;
	double[] T = new double[3 * n + 1];
	double[] l = new double[3 * n + 1];
	double[] ro = new double[3 * n + 1];

	private double f_ro(double x, double k) {
		double f_ro = -k * x;
		return f_ro;
	}

	private double f_T(double x, double k1, double k2, double k3, double E) {
		double f_T = -k1 * x + k2 - k3 * Math.exp(-E / x);
		return f_T;
	}

	public MoveTask(double tv0, double gv0, double gv1, double c0_, double sp1) {
		setTv0(tv0);
		setGv0(gv0);
		setGv1(gv1);
		setC00(c0_);
		setSp1(sp1);
	}

	void diffurCalc() {
		double Tp = 473;
		double Tn = 300;
		double konst = 0.026;
		double Gk = 95.5;
		double r0 = 0.017;
		double ksi = 1.0;
		double bet = 10.0;
		double ck = 240.0;
		double q = -8300000.0;
		double cv = 250.0;
		double gamma_k = 0.7;
		double eps = 0.4;
		double E = 10.0;
		double T0 = TV0;
		int i, k;
		double f;
		double w;
		double k1, k2, k3, k4;
		double koef;
		double koef1, koef2, koef3;
		double[] L = new double[3];
		double[] C = new double[3 * n + 1];
		double[] c0 = new double[3];
		double[] k0 = new double[m + 1]; // t/(m2*ch)
		double[] h = new double[3];
		double[] Gv = new double[3];
		double[] Sp = new double[3]; // m2/m
		double[] Tv = new double[3];
		// ---------------------------------------

		L[0] = 5.4;
		L[1] = 9.0;
		L[2] = 5.6;// m
		double S = 6.7; // m2
		// double lam1=314.0,lam2=4.2; //kDg/(m2*ch*grad)
		double lam1 = 75.0, lam2 = 1.0;
		double Sn = 12.0; // m2/m
		// double ksi=3.42;
		// ksi=ksi*1000000;

		Gv[0] = Gv0;
		Gv[1] = Gv1;
		Gv[2] = Gv0;
		c0[0] = c00;// mass doli
		c0[1] = 0.23;// mass doli
		c0[2] = 0.23;// mass doli

		k0[0] = 0.001;
		l[0] = 0.0;
		for (i = 0; i <= 2; i++)
			h[i] = L[i] / n;

		for (k = 0; k <= 2; k++) {
			for (i = k * n + 1; i <= (k + 1) * n; ++i)
				l[i] = l[i - 1] + h[k];
		}

		Sp[0] = 0;
		Sp[1] = Sp1;
		Sp[2] = 8.0;

		Tv[0] = TV0; // K
		Tv[1] = 300; // K
		Tv[2] = 300; // K

		C[0] = c0[0];
		ro[0] = r0;
		T[0] = T0;

		f = Gv[0] / L[0];
		for (i = 0; i <= 3 * n; i++)
			C[i] = 0.0;

		C[0] = c0[0];
		ro[0] = r0;
		T[0] = T0;
		for (k = 0; k <= 2; k++) {
			f = Gv[k] / L[k];
			for (i = k * n + 1; i <= (k + 1) * n; ++i) {
				// raschet C
				w = konst * Math.exp(-E / T[i - 1]) * C[i - 1];
				C[i] = c0[k] - ksi * gamma_k * (1.0 - eps) * S * w / f;

				w = konst * Math.exp(-E / T[i - 1]) * C[i];

				koef = bet * ksi * gamma_k * (1.0 - eps) * S * w / (r0 * Gk);

				k1 = f_ro(ro[i - 1], koef);
				k2 = f_ro(ro[i - 1] + h[k] * k1 / 4.0, koef);
				k3 = f_ro(ro[i - 1] + h[k] * k2 / 2.0, koef);
				k4 = f_ro(ro[i - 1] + h[k] * k1 - 2.0 * h[k] * k2 + 2 * h[k]
						* k3, koef);
				ro[i] = ro[i - 1] + h[k] * (k1 + 4.0 * k3 + k4) / 6.0;

				koef1 = f * cv + lam1 * Sp[k] + lam2 * Sn;
				koef2 = f * cv * Tv[k] + lam1 * Sp[k] * Tp + lam2 * Sn * Tn;
				koef3 = bet * ksi * q * gamma_k * (1.0 - eps) * S * konst
						* C[i] * ro[i] / r0;
				k1 = (1.0 / (Gk * ck)) * f_T(T[i - 1], koef1, koef2, koef3, E);
				k2 = (1.0 / (Gk * ck))
						* f_T(T[i - 1] + h[k] * k1 / 4.0, koef1, koef2, koef3,
								E);
				k3 = (1.0 / (Gk * ck))
						* f_T(T[i - 1] + h[k] * k2 / 2.0, koef1, koef2, koef3,
								E);
				k4 = (1.0 / (Gk * ck))
						* f_T(T[i - 1] + h[k] * k1 - 2.0 * h[k] * k2 + 2 * h[k]
								* k3, koef1, koef2, koef3, E);
				T[i] = T[i - 1] + h[k] * (k1 + 4.0 * k3 + k4) / 6.0;
				System.out.println(T[i]);
				// printf("  T[%i]=%11.8f \n",i,T[i]);

			}
		}
	}

	public XYSeries calculate(typePaint myTypePaint) {
		diffurCalc();
		final XYSeries series1 = new XYSeries("");
		if (myTypePaint == typePaint.cocs) {
			series1.setKey("Изменение кокса");
			for (int i = 0; i <= 3 * n; i++)
				series1.add(l[i], ro[i]);
		} else if (myTypePaint == typePaint.temprature) {
			// series1.setKey("Изменение температуры");

			for (int i = 0; i <= 3 * n; i++)
				series1.add(l[i], T[i]);
		}
		return series1;
	}

	public XYDataset calculate1(typePaint type) {
		// XYDataset dataset = null;
		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 6.7;

		final XYSeries series1 = calculate(type);
		series1.setKey("Режим 1");
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		
//		TV0 = 420+273.15;
//		Gv0 = 3.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series2 = calculate(type);
//		series2.setKey("Режим 2");
//		dataset.addSeries(series2);
//
//		TV0 = 420+273.15;
//		Gv0 = 4.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series3 = calculate(type);
//		series3.setKey("Режим 3");
//		dataset.addSeries(series3);
//		
//		TV0 = 420+273.15;
//		Gv0 = 5.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series4 = calculate(type);
//		series4.setKey("Режим 4");
//		dataset.addSeries(series4);
//		
//		TV0 = 420+273.15;
//		Gv0 = 7.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series5 = calculate(type);
//		series5.setKey("Режим 5");
//		dataset.addSeries(series5);
//
//		TV0 = 420+273.15;
//		Gv0 = 8.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series6 = calculate(type);
//		series6.setKey("Режим 6");
//		dataset.addSeries(series6);

//		--------------------------------
		
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.15;
//		Sp1 = 6.7;
//
//		final XYSeries series2 = calculate(type);
//		series2.setKey("Режим 7");
//		dataset.addSeries(series2);
//
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.165;
//		Sp1 = 6.7;
//
//		final XYSeries series3 = calculate(type);
//		series3.setKey("Режим 8");
//		dataset.addSeries(series3);
//		
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.18;
//		Sp1 = 6.7;
//
//		final XYSeries series4 = calculate(type);
//		series4.setKey("Режим 9");
//		dataset.addSeries(series4);
//		
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.21;
//		Sp1 = 6.7;
//
//		final XYSeries series5 = calculate(type);
//		series5.setKey("Режим 10");
//		dataset.addSeries(series5);
//
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.225;
//		Sp1 = 6.7;
//
//		final XYSeries series6 = calculate(type);
//		series6.setKey("Режим 11");
//		dataset.addSeries(series6);
		
//		----------------------------------------
//		TV0 = 380+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series2 = calculate(type);
//		series2.setKey("Режим 12");
//		dataset.addSeries(series2);
//
//		TV0 = 400+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series3 = calculate(type);
//		series3.setKey("Режим 13");
//		dataset.addSeries(series3);
//		
//		TV0 = 440+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series4 = calculate(type);
//		series4.setKey("Режим 14");
//		dataset.addSeries(series4);
//		
//		TV0 = 460+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series5 = calculate(type);
//		series5.setKey("Режим 15");
//		dataset.addSeries(series5);
//
//		TV0 = 480+273.15;
//		Gv0 = 6.4;
//		Gv1 = 14.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series6 = calculate(type);
//		series6.setKey("Режим 16");
//		dataset.addSeries(series6);

//-----------------------------------------------------------------------
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 8.2;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series2 = calculate(type);
//		series2.setKey("Режим 17");
//		dataset.addSeries(series2);
//
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 9.8;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series3 = calculate(type);
//		series3.setKey("Режим 18");
//		dataset.addSeries(series3);
//		
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 11.4;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series4 = calculate(type);
//		series4.setKey("Режим 19");
//		dataset.addSeries(series4);
//		
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 13.0;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series5 = calculate(type);
//		series5.setKey("Режим 20");
//		dataset.addSeries(series5);
//
//		TV0 = 420+273.15;
//		Gv0 = 6.4;
//		Gv1 = 16.0;
//		c00 = 0.195;
//		Sp1 = 6.7;
//
//		final XYSeries series6 = calculate(type);
//		series6.setKey("Режим 21");
//		dataset.addSeries(series6);

//		-------------------------------------------------
		
		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 3.7;

		final XYSeries series2 = calculate(type);
		series2.setKey("Режим 22");
		dataset.addSeries(series2);

		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 4.7;

		final XYSeries series3 = calculate(type);
		series3.setKey("Режим 23");
		dataset.addSeries(series3);
		
		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 5.7;

		final XYSeries series4 = calculate(type);
		series4.setKey("Режим 24");
		dataset.addSeries(series4);
		
		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 7.7;

		final XYSeries series5 = calculate(type);
		series5.setKey("Режим 25");
		dataset.addSeries(series5);

		TV0 = 420+273.15;
		Gv0 = 6.4;
		Gv1 = 14.4;
		c00 = 0.195;
		Sp1 = 8.7;

		final XYSeries series6 = calculate(type);
		series6.setKey("Режим 26");
		dataset.addSeries(series6);

		return dataset;
	}

	public double getTV0() {
		return TV0;
	}

	public void setTv0(double tv0) {
		this.TV0 = tv0;
	}

	public double getGv0() {
		return Gv0;
	}

	public void setGv0(double gv0) {
		this.Gv0 = gv0;
	}

	public double getGv1() {
		return Gv1;
	}

	public void setGv1(double gv1) {
		this.Gv1 = gv1;
	}

	public double getC00() {
		return c00;
	}

	public void setC00(double c00) {
		this.c00 = c00;
	}

	public double getSp1() {
		return Sp1;
	}

	public void setSp1(double sp1) {
		Sp1 = sp1;
	}
}
