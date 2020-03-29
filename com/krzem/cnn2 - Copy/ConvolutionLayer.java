package com.krzem.cnn2 - Copy;



import java.util.ArrayList;
import java.util.List;



public class ConvolutionLayer implements Layer{
	public String af;
	public int cn;
	public int kw;
	public int kh;
	private double[][][][] convL;
	private double[][][] lI;
	private double[][][] lO;
	private double[] bL;



	public ConvolutionLayer(int cn,int kw,int kh,String af){
		this.cn=cn;
		this.kw=kw;
		this.kh=kh;
		this.af=af;
	}



	@Override
	public int[] o_size(int[] ls){
		return new int[]{ls[0]-this.kw+1,ls[1]-this.kh+1,this.cn};
	}



	@Override
	public CNNVariable out(CNNVariable v){
		double[][][] in=v.get_3d();
		this.lI=in;
		int[] sz=this.o_size(new int[]{in[0][0].length,in[0].length,in.length});
		double[][][] o=new double[this.cn][sz[0]][sz[1]];
		for (int i=0;i<this.cn;i++){
			for (int j=0;j<in.length;j++){
				for (int k=0;k<sz[0];k++){
					for (int l=0;l<sz[1];l++){
						for (int m=0;m<this.kh;m++){
							for (int n=0;n<this.kw;n++){
								int x=l+n;
								int y=k+m;
								if (x<0||x>=in[0].length||y<0||y>=in[0][0].length){
									continue;
								}
								o[i][k][l]+=this.convL[i][j][m][n]*in[j][y][x];
							}
						}
						o[i][k][l]=Activation.apply(new double[]{o[i][k][l]+this.bL[i]},this.af)[0];
					}
				}
			}
		}
		this.lO=o;
		v.set(o);
		return v;
	}



	@Override
	public CNNVariable err(CNNVariable v,double lr){
		double[][][] e=v.get_3d();
		double[][][] o=new double[this.lI.length][this.lI[0].length][this.lI[0][0].length];
		double[][][] err=new double[this.lI.length][this.lI[0].length][this.lI[0][0].length];
		for (int i=0;i<this.lI.length;i++){
			for (int j=0;j<this.convL[0].length;j++){
				for (int k=0;k<this.lI[0].length-kh;k++){
					for (int l=0;l<this.lI[0][0].length-kw;l++){
						for (int m=0;m<kh;m++){
							for (int n=0;n<kw;n++){
								err[i][k+m][l+n]+=e[i][k][l]*this.convL[i][j][m][n];
							}
						}
					}
				}
				for (int k=0;k<this.lI[0].length;k++){
					for (int l=0;l<this.lI[0][0].length;l++){
						o[i][k][l]+=err[i][k][l]*Activation.applyD(new double[]{this.lI[j][k][l]},this.af)[0];
					}
				}
			}
			// this.bL[i]=this.lO[i][k][l];
		}
		v.set(o);
		return v;
	}



	@Override
	public String _name(){
		return "Convolution";
	}



	@Override
	public String _data(){
		return String.format("{convolutions = %d, kernel_size = (%d, %d), activation_function = %s}",this.cn,this.kw,this.kh,this.af);
	}



	@Override
	public String _output(int[] ls){
		return String.format("[%d %d %d]",ls[0]-this.kw+1,ls[1]-this.kh+1,this.cn);
	}



	@Override
	public String _params(int[] ls){
		return Integer.toString((ls[2]*this.kh*this.kw+1)*this.cn);
	}



	@Override
	public void _gen(int[] ls){
		this.convL=new double[this.cn][ls[2]][this.kh][this.kw];
		this.bL=new double[this.cn];
		for (int i=0;i<this.cn;i++){
			for (int j=0;j<ls[2];j++){
				for (int k=0;k<this.kh;k++){
					for (int l=0;l<this.kw;l++){
						this.convL[i][j][k][l]=Random.next();
					}
				}
			}
			this.bL[i]=0;//1;
		}
	}
}