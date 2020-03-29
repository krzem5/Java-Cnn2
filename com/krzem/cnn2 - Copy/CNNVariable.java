package com.krzem.cnn2 - Copy;






public class CNNVariable{
	private double[] _1d;
	private double[][][] _3d;



	public CNNVariable(double[] i){
		this.set(i);
	}
	public CNNVariable(double[][][] i){
		this.set(i);
	}



	public void set(double[] i){
		this._3d=null;
		this._1d=i;
	}
	public void set(double[][][] i){
		this._1d=null;
		this._3d=i;
	}
	public void set(CNNVariable v){
		this._3d=v._3d;
		this._1d=v._1d;
	}



	public double[] get_1d(){
		if (this._1d==null){
			System.exit(1);
		}
		return this._1d;
	}



	public double[][][] get_3d(){
		if (this._3d==null){
			System.exit(1);
		}
		return this._3d;
	}



	public int type(){
		return (this._1d==null?3:1);
	}



	public boolean _NaN(){
		if (this._1d!=null){
			for (int i=0;i<this._1d.length;i++){
				if (Double.isNaN(this._1d[i])){
					return true;
				}
			}
			return false;
		}
		else{
			for (int i=0;i<this._3d.length;i++){
				for (int j=0;j<this._3d[0].length;j++){
					for (int k=0;k<this._3d[0][0].length;k++){
						if (Double.isNaN(this._3d[i][j][k])){
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}