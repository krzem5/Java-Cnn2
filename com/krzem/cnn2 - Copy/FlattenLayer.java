package com.krzem.cnn2 - Copy;






public class FlattenLayer implements Layer{
	private int[] lS;



	public FlattenLayer(){

	}



	@Override
	public int[] o_size(int[] ls){
		return new int[]{ls[0]*ls[1]*ls[2]};
	}



	@Override
	public CNNVariable out(CNNVariable v){
		double[][][] in=v.get_3d();
		double[] o=new double[in.length*in[0].length*in[0][0].length];
		this.lS=new int[]{in.length,in[0].length,in[0][0].length};
		int l=0;
		for (int i=0;i<in.length;i++){
			for (int j=0;j<in[0].length;j++){
				for (int k=0;k<in[0][0].length;k++){
					o[l]=in[i][j][k];
					l++;
				}
			}
		}
		v.set(o);
		return v;
	}



	@Override
	public CNNVariable err(CNNVariable v,double lr){
		double[] in=v.get_1d();
		double[][][] o=new double[this.lS[0]][this.lS[1]][this.lS[2]];
		int l=0;
		for (int i=0;i<this.lS[0];i++){
			for (int j=0;j<this.lS[1];j++){
				for (int k=0;k<this.lS[2];k++){
					o[i][j][k]=in[l];
					l++;
				}
			}
		}
		v.set(o);
		return v;
	}



	@Override
	public String _name(){
		return "Flatten";
	}



	@Override
	public String _data(){
		return "{}";
	}



	@Override
	public String _output(int[] ls){
		return String.format("[%d]",ls[0]*ls[1]*ls[2]);
	}



	@Override
	public String _params(int[] ls){
		return "0";
	}



	@Override
	public void _gen(int[] ls){

	}
}