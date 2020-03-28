package com.krzem.cnn2;



import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



public class MaxPoolingLayer implements Layer{
	public int w;
	public int h;
	public int sw;
	public int sh;
	private boolean[][][] lM;



	public MaxPoolingLayer(int w,int h,int sw,int sh){
		this.w=w;
		this.h=h;
		this.sw=sw;
		this.sh=sh;
	}



	@Override
	public int[] o_size(int[] ls){
		return new int[]{(ls[0]-this.w)/this.sw+1,(ls[1]-this.h)/this.sh+1,ls[2]};
	}



	@Override
	public CNNVariable out(CNNVariable v){
		double[][][] in=v.get_3d();
		if (this.lM==null){
			this.lM=new boolean[in.length][in[0].length][in[0][0].length];
		}
		int[] sz=this.o_size(new int[]{in[0][0].length,in[0].length,in.length});
		double[][][] o=new double[in.length][sz[1]][sz[0]];
		for (int i=0;i<in.length;i++){
			for (int j=0;j<sz[1];j+=this.sh){
				for (int k=0;k<sz[0];k+=this.sw){
					double mv=-Double.MAX_VALUE;
					int ml=-1;
					int mm=-1;
					for (int l=Math.min(j*this.h,in[i].length-1);l<Math.min(Math.min(j*this.h,in[i].length-1)+this.h,in[i].length);l++){
						for (int m=Math.min(k*this.w,in[i][0].length-1);m<Math.min(Math.min(k*this.w,in[i][0].length-1)+this.w,in[i][0].length);m++){
							double s=in[i][l][m];
							if (s>mv){
								mv=s;
								ml=l;
								mm=m;
							}
						}
					}
					this.lM[i][ml][mm]=true;
					o[i][j][k]=mv;
				}
			}
		}
		v.set(o);
		return v;
	}



	@Override
	public CNNVariable err(CNNVariable v,double lr){
		double[][][] g=v.get_3d();
		double[][][] o=new double[g.length][this.lM[0].length][this.lM[0][0].length];
		for (int i=0;i<g.length;i++){
			for (int j=0;j<this.lM[0].length;j++){
				for (int k=0;k<this.lM[0][0].length;k++){
					if (this.lM[i][j][k]==true){
						o[i][j][k]=g[i][j/this.h][k/this.w];
					}
				}
			}
		}
		v.set(o);
		return v;
	}



	@Override
	public String _name(){
		return "MaxPooling";
	}



	@Override
	public String _data(){
		return String.format("{pool_size = (%d, %d), stride = (%d, %d)}",this.w,this.h,this.sw,this.sh);
	}



	@Override
	public String _output(int[] ls){
		return String.format("[%d %d %d]",ls[0]/this.sw-(ls[0]%this.sw>0?0:1),ls[1]/this.sh-(ls[1]%this.sh>0?0:1),ls[2]);
	}



	@Override
	public String _params(int[] ls){
		return "0";
	}



	@Override
	public void _gen(int[] ls){

	}
}