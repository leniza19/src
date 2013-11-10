package mathchem.chart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ZernoTask extends Task {
	int i,j,k;
	final int M=4,N=101;
	double ko[]={0,0.2423,0.8772,0.6165,5.2358}; // 1/c
	double E[]={0,99100.0,90800.0,52800.0,50400.0}; // J/mole
	
	double Dz=0.000005; // effective diffusion factor, m2/sec
	double t_op=793.0; // Basic temperature, Calvin
	double c_p=33.47;  // J/(mole*Cal)
	double tau_k=4.8; // contact time, sec
	double qc; // the initial and current content of coke
	// on the catalyst, g/g
	double gam0=600.0; //  (c_k/c_p)
	double tile;   // parameter Tile
	
	private double []t=new double[N+1];   // tenperature
	private double[] r = new double[N + 1]; // r[i] - points of an interval [0,1]
	double [][]y = new double[M+1][N+1];
	
	/////////////////////
	private double c00; //0.02
	private double qc0; //0.04
	private double Rz;// 0.005 // grain radius, m
	private double T0; //400.0
	////////////////////

	public ZernoTask(double c00_,double qc0_,double Rz_,double T0_) {
		setC00(c00_);
		setQc0(qc0_);
		setRz(Rz_);
		setT0(T0_);
	}
	public double getC00() {
		return c00;
	}
	public void setC00(double c00) {
		this.c00 = c00;
	}
	public double getQc0() {
		return qc0;
	}
	public void setQc0(double qc0) {
		this.qc0 = qc0;
	}
	public double getRz() {
		return Rz;
	}
	public void setRz(double rz) {
		Rz = rz;
	}
	public double getT0() {
		return T0;
	}
	public void setT0(double t0) {
		T0 = t0;
	}
	
	private double konst(double T,int ii)
	{
		double[] bb = new double[M + 1];	
		double sk;
		sk=Math.pow((qc/qc0),2.0/3.0);
		tile=Rz*Rz/(Dz*tau_k);

		for(int hh=1;hh<=M;hh++)
			bb[hh]=E[hh]/(8.314*t_op);
		double aa=ko[ii]*Math.exp(bb[ii]-bb[ii]*T);
		aa=aa*tau_k*sk*tile;
		return aa;
	}

	private double summa (double yy, double tt)
	{
		// heat effects of reactions
		double qp[]={0,83700.0,310300.0,67600.0,241800.0};
		for (int hh=1;hh<=M;hh++)
			qp[hh]=qp[hh]/(t_op*c_p);
		double ss1=0;
		ss1+=qp[1]*konst(tt,1)*yy;
		ss1+=qp[2]*konst(tt,2)*yy;
		ss1+=qp[3]*konst(tt,3);
		ss1+=qp[4]*konst(tt,4)*yy;
		return ss1;
	}
	private double sum_mu (double yy, double tt)
	{
		// last line of a stoichiometrical matrix
		double delt[]={0,-1.0,0,1.0,0};
		double ss2=0;
		ss2+=delt[1]*konst(tt,1)*yy;
		ss2+=delt[2]*konst(tt,2)*yy;
		ss2+=delt[3]*konst(tt,3);
		ss2+=delt[4]*konst(tt,4)*yy;
		return ss2;
	}

	private void zernoMake()
	{
		double tt0;   // reference temperature
		tt0=(T0+273.0)/t_op;
 
		qc=qc0;
		double gam;
		tile=Rz*Rz/(Dz*tau_k);

		gam=gam0*tile;
		double eps; eps=1.0/1000.0;   // accuracy
		//char str[50];
		
		double C;
		double[] h = new double[N + 1]; // h - slot pitch
		double[] dV = new double[N + 1];
		
		double[][] a_ = new double[M + 1][N+1];
		double[][] b_ = new double[M + 1][N+1];
		
		for(i=1;i<=N;i++)
			t[i]=tt0;
		double []tn=new double[N+1];
		double []te0=new double[N+1];

		double tau;  // time pitch
		double e,ek=0.3;
		e=tile*ek;
		tau=tau_k/10.0;

		//----- calculation h[i]
//		C=0.11/(1.0-Math.pow(0.89,(double)(N-2)));
		Double.parseDouble(Integer.toString(i)); 
		C=0.11/(1.0-Math.pow(0.89,Double.parseDouble(Integer.toString(N-2))));
		
		h[1]=0;
		h[2]=0.5*C;
		for(i=3;i<=N-1;i++)
			h[i]=0.5*C*1.89*Math.pow(0.89,i-3.0);
		h[N]=0.5*C*Math.pow(0.89,N-3.0);

		for(i=2;i<=N;i++)
			h[i]=1.0/ Double.parseDouble(Integer.toString(N-1));
		//----- calculation r[i]
		r[0]=0.0;    //
		r[1]=0.5*h[2];

		for(i=2;i<=N-1;i++)
		{
			r[i]=r[i-1]+h[2];
		}
		r[N]=1;
		//----- calculation deltaV[i]
		for(i=2;i<=N-1;i++)
		{
			dV[i]=(Math.pow(r[i],3.0)-Math.pow(r[i-1],3.0))/3.0;
		}
		dV[1]=Math.pow(r[1],3.0)/3.0;
		dV[N]=(1.0-Math.pow(r[N-1],3.0))/3.0;
		//------------additional factors---------------------
		double []uu = new double [N];
		double []ss = new double[N];
    	for(i=2;i<=N-1;i++)
		{
			uu[i]=dV[i]/(r[i]*r[i]);
			ss[i]=Math.pow(r[i-1]/r[i+1],2.0);
		}
		//------------------------------------------------------------
		//------------------------------------------------------------
		double x[]={0,c00,0,0,0};
		double [][]y0 = new double [M+1][N+1];
		//------------------------------------------------------------
		//At the initial moment of time all concentration are equal to zero
		for(i=0;i<=M;i++)
			for(j=0;j<=N;j++)
				y0[i][j]=0;
		//------------------------------------------------------------
		double []ym = new double [N+1]; // stefan stream
		double []a = new double [N+1];
		double []b = new double[N+1];
		double []c = new double[N+1];
		double []f = new double [N+1];

		double bet,bet0;
		bet=0.0115;
		bet0=bet*Rz/Dz;

		double []p = new double[N];
		double []q = new double[N];
		double []raz = new double[N+1];
		int fg,kol;
		double max,max1;
		fg=0;kol=0;

		//-----------------------------------------
		do
		{
			kol++;
			
			for(i=1;i<=N;i++)
				te0[i]=t[i];

			do
			{
				for(i=1;i<=N;i++)
					tn[i]=t[i];

				fg++;
				for(i=1;i<=N;i++)
				{
					a_[0][i]=0; b_[0][i]=0;
					a_[1][i]=konst(tn[i],1)+konst(tn[i],2)+konst(tn[i],4);
					a_[2][i]=0; a_[3][i]=0; a_[4][i]=0;
					b_[1][i]=0; b_[4][i]=konst(tn[i],3);
				}
				//-------------------------------------------------
				
				ym[1]=dV[1]*sum_mu(y0[1][1],tn[1])/(r[1]*r[1]);
				for(i=2;i<=N-1;i++)
					ym[i]=ss[i]*ym[i-1]+uu[i]*sum_mu(y0[1][i],tn[i]);
				
				ym[N]=ss[N-1]*ym[N-1]+uu[N-1]*sum_mu(y0[1][N],tn[N]);
				
				//-------------------------------------------------
				for(k=1;k<=M;k++)
				{
					b[1]=r[1]*r[1]*(1/h[2]+0.5*(Math.abs(ym[1])-ym[1]));
					c[1]=r[1]*r[1]*(1/h[2]+0.5*(Math.abs(ym[1])+ym[1]));
					c[1]+=dV[1]*(a_[k][1]+e/tau);
					f[1]=dV[1]*(e*y0[k][1]/tau+b_[k][1]);

					a[N]=r[N-1]*r[N-1]*(1/h[N]+0.5*(Math.abs(ym[N-1])+ym[N-1]));
					c[N]=r[N-1]*r[N-1]*(1/h[N]+0.5*(Math.abs(ym[N-1])-ym[N-1]));
					c[N]+=bet0+dV[N]*(a_[k][N]+e/tau);
					f[N]=dV[N]*(e*y0[k][N]/tau+b_[k][N])+bet0*x[k];

					for(i=2;i<=N-1;i++)
					{
						a[i]=r[i-1]*r[i-1]*(1/h[i]+0.5*(Math.abs(ym[i-1])-ym[i-1]));
						b[i]=r[i]*r[i]*(1/h[i+1]+0.5*(Math.abs(ym[i])-ym[i]));
						c[i]=r[i]*r[i]*(1/h[i+1]+0.5*(Math.abs(ym[i])+ym[i]));
						c[i]+=r[i-1]*r[i-1]*(1/h[i]+0.5*(Math.abs(ym[i-1])+ym[i-1]));
						c[i]+=dV[i]*(a_[k][N]+e/tau);
						f[i]=dV[i]*(e*y0[k][i]/tau+b_[k][i]);
					}

					p[1]=b[1]/c[1]; q[1]=f[1]/c[1];
					for(i=2;i<=N-1;i++)
					{
						p[i]=b[i]/(c[i]-a[i]*p[i-1]);
						q[i]=(f[i]+a[i]*q[i-1])/(c[i]-a[i]*p[i-1]);
					}

					y[k][N]=(f[N]+a[N]*q[N-1])/(c[N]-a[N]*p[N-1]);
					for(i=N-1;i>=1;i--)
						y[k][i]=p[i]*y[k][i+1]+q[i];
					if(k==1)
						for(i=1;i<=N;i++)
						{
							b_[2][i]=konst(tn[i],4)*y[1][i];
							b_[3][i]=konst(tn[i],2)*y[1][i];
						}
				}
				//--------------------------------------------------------------------
				b[1]=r[1]*r[1]/h[2];
				c[1]=gam*dV[1]/tau+b[1];
				f[1]=dV[1]*(gam*te0[1]/tau+summa(y[1][1],tn[1]));

				a[N]=Math.pow(r[N-1],3.0)/h[N];
				f[N]=dV[N]*(gam*te0[N]/tau+summa(y[1][N],tn[N]))+bet0*tt0;
				c[N]=gam*dV[N]/tau+a[N]+bet0;

				for(i=2;i<=N-1;i++)
				{
					a[i]=r[i-1]*r[i-1]/h[i];
					b[i]=r[i+1]*r[i+1]/h[i+1];
					c[i]=a[i]+b[i]+dV[i]*gam/tau;
					f[i]=dV[i]*(gam*te0[i]/tau+summa(y[1][i],tn[i]));
				}

				p[1]=b[1]/c[1]; q[1]=f[1]/c[1];
				for(i=2;i<=N-1;i++)
				{
					p[i]=b[i]/(c[i]-a[i]*p[i-1]);
					q[i]=(f[i]+a[i]*q[i-1])/(c[i]-a[i]*p[i-1]);
				}

				t[N]=(f[N]+a[N]*q[N-1])/(c[N]-a[N]*p[N-1]);
				for(i=N-1;i>=1;i--)
					t[i]=p[i]*t[i+1]+q[i];

				max=0;
				for(j=1;j<=N;j++)
				{
					raz[j]=Math.abs(tn[j]-t[j]);
					if (raz[j]>max) max=raz[j];
				}
			}
			while(max>=0.00001);

			for(i=1;i<=M;i++)
				for(j=1;j<=N;j++)
					y0[i][j]=y[i][j];
			max1=0;
			for(j=1;j<=N;j++)
			{
				raz[j]=Math.abs(te0[j]-t[j]);
				if (raz[j]>max1) max1=raz[j];
			}
		}
		while(max1>=eps);
	}
	public XYSeries calculate(typePaint myTypePaint) {
		zernoMake();
		final XYSeries series1 = new XYSeries("");
	if (myTypePaint == typePaint.temprature) {
			// series1.setKey("Изменение температуры");
			
			for (int i = 1; i <= N; i++)
			{
				  t[i]=t[i]*t_op-273.0;
				series1.add(r[i], t[i]);
			}
		}
	else if (myTypePaint == typePaint.o2) {
		series1.setKey("Концентрация O2");
		for (int i = 1; i <= N; i++)
			series1.add(r[i], y[1][i]);
	}
	else if (myTypePaint == typePaint.h2o) {
		series1.setKey("Концентрация H2O");
		for (int i = 1; i <= N; i++)
			series1.add(r[i], y[2][i]);
	}
	else if (myTypePaint == typePaint.co2) {
		series1.setKey("Концентрация CO2");
		for (int i = 1; i <= N; i++)
			series1.add(r[i], y[3][i]);
	}
	else if (myTypePaint == typePaint.co) {
		series1.setKey("Концентрация CO");
		for (int i = 1; i <= N; i++)
			series1.add(r[i], y[4][i]);
	}
		return series1;
	}
	
	public XYDataset calculate1(typePaint type) {
		// XYDataset dataset = null;

		T0 = 400;

		final XYSeries series1 = calculate(type);
		series1.setKey("Т = 400 C");
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		T0 = 450;

		final XYSeries series2 = calculate(type);
		series2.setKey("Т = 450 C");
		dataset.addSeries(series2);
		
		T0 = 500;

		final XYSeries series3 = calculate(type);
		series3.setKey("Т = 500 C");
		dataset.addSeries(series3);
		
//		T0 = 460;
//
//		final XYSeries series4 = calculate(type);
//		series4.setKey("Т = 460 C");
//		dataset.addSeries(series4);
		
//		T0 = 480;
//
//		final XYSeries series5 = calculate(type);
//		series5.setKey("Т = 480 C");
//		dataset.addSeries(series5);
		
//		T0 = 500;
//
//		final XYSeries series6 = calculate(type);
//		series6.setKey("Т = 500 C");
//		dataset.addSeries(series6);

		return dataset;
	}
}
