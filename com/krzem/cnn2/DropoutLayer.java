package com.krzem.cnn2;



public class DropoutLayer implements Layer{
	public double d;
	private boolean[] m_1d;
	private boolean[][][] m_3d;



	public DropoutLayer(double d){
		this.d=d;
	}



	@Override
	public int[] o_size(int[] ls){
		return ls;
	}



	@Override
	public CNNVariable out(CNNVariable v){
		if (v.type()==1){
			double[] in=v.get_1d();
			double[] o=new double[in.length];
			this.m_1d=new boolean[in.length];
			for (int i=0;i<in.length;i++){
				if (Random.next()<this.d){
					this.m_1d[i]=true;
				}
				else{
					o[i]=in[i]*(1/1-this.d);
				}
			}
			v.set(o);
			return v;
		}
		else{
			double[][][] in=v.get_3d();
			double[][][] o=new double[in.length][in[0].length][in[0][0].length];
			this.m_3d=new boolean[in.length][in[0].length][in[0][0].length];
			for (int i=0;i<in.length;i++){
				for (int j=0;j<in[0].length;j++){
					for (int k=0;k<in[0][0].length;k++){
						if (Random.next()<this.d){
							this.m_3d[i][j][k]=true;
						}
						else{
							o[i][j][k]=in[i][j][k]*(1/1-this.d);
						}
					}
				}
			}
			v.set(o);
			return v;
		}
	}



	@Override
	public CNNVariable err(CNNVariable v,double lr){
		if (v.type()==1){
			double[] in=v.get_1d();
			double[] o=new double[in.length];
			for (int i=0;i<in.length;i++){
				if (this.m_1d[i]==false){
					o[i]=in[i]*(1/1-this.d);
				}
			}
			v.set(o);
			return v;
		}
		else{
			double[][][] in=v.get_3d();
			double[][][] o=new double[in.length][in[0].length][in[0][0].length];
			for (int i=0;i<in.length;i++){
				for (int j=0;j<in[0].length;j++){
					for (int k=0;k<in[0][0].length;k++){
						if (this.m_3d[i][j][k]==false){
							o[i][j][k]=in[i][j][k]*(1/1-this.d);
						}
					}
				}
			}
			v.set(o);
			return v;
		}
	}



	@Override
	public String _name(){
		return "Dropout";
	}



	@Override
	public String _data(){
		return String.format("{propability = %.1f%%}",this.d*100);
	}



	@Override
	public String _output(int[] ls){
		if (ls.length==1){
			return String.format("[%d]",ls[0]);
		}
		return String.format("[%d %d %d]",ls[0],ls[1],ls[2]);
	}



	@Override
	public String _params(int[] ls){
		return "0";
	}



	@Override
	public void _gen(int[] ls){

	}
}