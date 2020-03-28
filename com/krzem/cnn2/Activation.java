package com.krzem.cnn2;



import java.lang.Math;



public class Activation{
	public static double[] apply(double[] i,String f){
		if (f.equals("sigmoid")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=1/(1+Math.exp(-i[j]));
			}
			return o;
		}
		else if (f.equals("relu")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=Math.max(i[j],0);
			}
			return o;
		}
		else if (f.equals("tanh")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=Math.tanh(i[j]);
			}
			return o;
		}
		else if (f.equals("softmax")){
			double t=0;
			for (int j=0;j<i.length;j++){
				System.out.printf("[%d] %f => %f\n",j,i[j],Math.exp(i[j]));
				t+=Math.exp(i[j]);
			}
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=Math.exp(i[j])/t;
			}
			return o;
		}
		return i;
	}



	public static double[] applyD(double[] i,String f){
		if (f.equals("sigmoid")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=i[j]*(1-i[j]);
			}
			return o;
		}
		else if (f.equals("relu")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=(i[j]>0?1:0);
			}
			return o;
		}
		else if (f.equals("tanh")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=1-i[j]*i[j];
			}
			return o;
		}
		else if (f.equals("softmax")){
			double[] o=new double[i.length];
			for (int j=0;j<i.length;j++){
				o[j]=i[j]*i[j];
			}
			return o;
		}
		return i;
	}
}