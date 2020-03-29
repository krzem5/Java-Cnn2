package com.krzem.cnn2;



public class DenseLayer implements Layer{
	public int n;
	public String af;
	private double[][] wL;
	private double[] bL;
	private double[] lI;
	private double[] lO;



	public DenseLayer(int n,String af){
		this.n=n;
		this.af=af;
	}



	@Override
	public int[] o_size(int[] ls){
		return new int[]{this.n};
	}



	@Override
	public CNNVariable out(CNNVariable v){
		double[] in=v.get_1d();
		this.lI=in;
		double[] o=new double[this.n];
		for (int i=0;i<this.n;i++){
			for (int j=0;j<in.length;j++){
				o[i]+=in[j]*this.wL[i][j];
			}
			o[i]+=this.bL[i];
		}
		o=Activation.apply(o,this.af);
		this.lO=o;
		v.set(o);
		return v;
	}



	@Override
	public CNNVariable err(CNNVariable v,double lr){
		double[] e=v.get_1d();
		double[] o=new double[this.wL[0].length];
		double[] g=Activation.applyD(e,this.af);
		for (int i=0;i<this.n;i++){
			g[i]*=e[i]*lr;
			this.bL[i]+=g[i];
		}
		for (int i=0;i<this.n;i++){
			for (int j=0;j<o.length;j++){
				this.wL[i][j]+=g[i]*this.lI[j];
			}
		}
		for (int i=0;i<this.n;i++){
			for (int j=0;j<o.length;j++){
				o[j]+=e[i]*this.wL[i][j];
			}
		}
		v.set(o);
		return v;
	}



	@Override
	public String _name(){
		return "Dense";
	}



	@Override
	public String _data(){
		return String.format("{nodes = %d, activation_function = %s}",this.n,this.af);
	}



	@Override
	public String _output(int[] ls){
		return String.format("[%d]",this.n);
	}



	@Override
	public String _params(int[] ls){
		return Integer.toString(this.n*(ls[0]+1));
	}



	@Override
	public void _gen(int[] ls){
		this.wL=new double[this.n][ls[0]];
		this.bL=new double[this.n];
		for (int i=0;i<this.n;i++){
			for (int j=0;j<ls[0];j++){
				this.wL[i][j]=Random.next();
			}
			this.bL[i]=Random.next();
		}
	}
}