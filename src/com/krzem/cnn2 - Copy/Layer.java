package com.krzem.cnn2 - Copy;






public interface Layer{
	public int[] o_size(int[] ls);



	public CNNVariable out(CNNVariable v);



	public CNNVariable err(CNNVariable v,double lr);



	public String _name();



	public String _data();



	public String _output(int[] ls);



	public String _params(int[] ls);



	public void _gen(int[] ls);
}