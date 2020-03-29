package com.krzem.cnn2 - Copy;



import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Dataset{
	private List<double[][][]> data;
	private List<String> ll;
	private Map<String,List<double[][][]>> m_data;



	public Dataset(Map<String,List<double[][][]>> m_data){
		this.m_data=m_data;
		this._load();
	}



	public int size(){
		return this.data.size();
	}



	public List<String> labels(){
		List<String> o=new ArrayList<String>();
		for (String k:this.m_data.keySet()){
			o.add(k);
		}
		return o;
	}



	public double[][][] getI(int i){
		return this.data.get(i);
	}



	public String getL(int i){
		return this.ll.get(i);
	}



	public List<double[][][]> get(String l){
		return this.m_data.get(l);
	}



	public int[] get_r_batch_idx(int bs){
		int[] o=new int[bs];
		Arrays.fill(o,-1);
		for (int i=0;i<bs;i++){
			while (true){
				boolean n=true;
				int j=(int)Math.abs(Math.floor(Math.min(Math.abs(Random.next()),1)*this.data.size())-1);
				for (int k=0;k<i;k++){
					if (o[k]==j){
						n=false;
						break;
					}
				}
				if (n==true){
					o[i]=j;
					break;
				}
			}
		}
		return o;
	}



	private void _load(){
		this.ll=new ArrayList<String>();
		this.data=new ArrayList<double[][][]>();
		for (String l:this.m_data.keySet()){
			for (double[][][] k:this.m_data.get(l)){
				this.ll.add(l);
				this.data.add(k);
			}
		}
	}
}