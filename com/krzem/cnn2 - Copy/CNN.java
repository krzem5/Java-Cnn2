package com.krzem.cnn2 - Copy;



import java.io.File;
import java.lang.Math;
import java.util.List;



public class CNN{
	public int inW;
	public int inH;
	public int inD;
	public List<Layer> layers;
	public String[] cL;
	public double lr;



	public CNN(int inW,int inH,int inD,List<Layer> layers,String[] cL,double lr){
		this.inW=inW;
		this.inH=inH;
		this.inD=inD;
		this.layers=layers;
		this.cL=cL;
		this.lr=lr;
		this._gen();
	}



	public void load(String s){
		if (new File(s).exists()&&new File(s).isFile()){
			System.out.println("Load!");
		}
	}



	public void save(String s){
		System.out.println("Save!");
	}



	public void print(){
		int nsw=11;
		int dsw=Integer.toString(this.inW).length()-Integer.toString(this.inH).length()-Integer.toString(this.inD).length()+31;
		int osw=Integer.toString(this.inW).length()-Integer.toString(this.inH).length()-Integer.toString(this.inD).length()+4;
		int psw=9;
		int[] li=new int[]{this.inW,this.inH,this.inD};
		for (Layer l:this.layers){
			nsw=Math.max(nsw,l._name().length());
			dsw=Math.max(dsw,l._data().length());
			osw=Math.max(osw,l._output(li).length());
			psw=Math.max(psw,l._params(li).length());
			li=l.o_size(li);
		}
		String o=String.format("Layer Name %s â”‚ Layer Data %s â”‚ Size %s â”‚ Param # %s\n%sâ”¼%sâ”¼%sâ”¼%s\nInput%s â”‚ {width = %d, height = %d, depth = %d}%s â”‚ [%d %d %d]%s â”‚ 0%s\n",this._pad(nsw-11," "),this._pad(dsw-11," "),this._pad(osw-5," "),this._pad(psw-9," "),this._pad(nsw+1,"â”€"),this._pad(dsw+2,"â”€"),this._pad(osw+2,"â”€"),this._pad(psw+2,"â”€"),this._pad(nsw-5," "),this.inW,this.inH,this.inD,this._pad(dsw-Integer.toString(this.inW).length()-Integer.toString(this.inH).length()-Integer.toString(this.inD).length()-31," "),this.inW,this.inH,this.inD,this._pad(osw-Integer.toString(this.inW).length()-Integer.toString(this.inH).length()-Integer.toString(this.inD).length()-4," "),this._pad(psw-1," "));
		li=new int[]{this.inW,this.inH,this.inD};
		for (Layer l:this.layers){
			String ns=l._name();
			String ds=l._data();
			String os=l._output(li);
			String ps=l._params(li);
			o+=String.format("%s%s â”‚ %s%s â”‚ %s%s â”‚ %s%s\n",ns,this._pad(nsw-ns.length()," "),ds,this._pad(dsw-ds.length()," "),os,this._pad(osw-os.length()," "),ps,this._pad(psw-ps.length()," "));
			li=l.o_size(li);
		}
		o="\n"+o+String.format("Output%s â”‚ {size = %d}%s â”‚ [%d]%s â”‚ 0%s\n",this._pad(nsw-6," "),li[0],this._pad(dsw-Integer.toString(li[0]).length()-9," "),li[0],this._pad(osw-Integer.toString(li[0]).length()-2," "),this._pad(psw-1," "));
		System.out.println(o);
	}



	public String predict(double[][][] i){
		double[] o=this._predict(i);
		double max=-1;
		int bidx=-1;
		for (int j=0;j<o.length;j++){
			if (o[j]>max){
				max=o[j];
				bidx=j+0;
			}
		}
		return this.cL[bidx];
	}



	public void train(int maxE,int bs,int tbs,Dataset dt,Dataset test,boolean log){
		if (log==true){
			System.out.printf("Start accuracy: %f%%\n",this._test_batch_acc(test,tbs));
		}
		for (int e=0;e<maxE;e++){
			this._train(dt,bs,log);
			if (log==true){
				double ba=this._test_batch_acc(test,tbs);
				int w=17+String.format("%d%d%f",e+1,maxE,ba).length();
				String b="";
				for (int i=0;i<w;i++){
					b+="=";
				}
				System.out.printf("%s\nEpoch %d/%d complete (test_batch_acc=%f)\n%s\n",b,e+1,maxE,ba,b);
			}
		}
	}



	public double acc(Dataset dt){
		int acc=0;
		int p=-1;
		long ost=-1;
		long st=-1;
		for (int i=0;i<dt.size();i++){
			if (p<(int)((double)i/dt.size()*100)){
				p=(int)((double)i/dt.size()*100);
				double sd=(double)(System.nanoTime()-ost);
				double d=(double)(System.nanoTime()-st);
				if (ost==-1||st==-1){
					ost=System.nanoTime();
					sd=0;
					d=0;
				}
				System.out.printf("%d%% complete... (t=%fs, dt=%fs)\n",p,sd*1e-9,d*1e-9);
				st=System.nanoTime();
			}
			double[] o=this._predict(dt.getI(i));
			double m=-Double.MAX_VALUE;
			int mi=-1;
			for (int j=0;j<o.length;j++){
				if (o[j]>m){
					m=o[j];
					mi=j+0;
				}
			}
			if (this.cL[mi].equals(dt.getL(i))){
				acc++;
			}
		}
		return (double)(acc)/dt.size()*100;
	}



	private double _test_batch_acc(Dataset dt,int bs){
		int acc=0;
		for (String l:dt.labels()){
			List<double[][][]> tl=dt.get(l);
			for (int i=0;i<bs;i++){
				int k=(int)Math.min(Math.floor(Math.abs(Random.next())*tl.size()),tl.size()-1);
				double[] o=this._predict(tl.get(k));
				double m=-Double.MAX_VALUE;
				int mi=-1;
				for (int j=0;j<o.length;j++){
					if (o[j]>m){
						m=o[j];
						mi=j+0;
					}
				}
				if (this.cL[mi].equals(l)){
					acc++;
				}
			}
		}
		return (double)(acc)/(bs*dt.labels().size())*100;
	}



	private void _gen(){
		int[] li=new int[]{this.inW,this.inH,this.inD};
		for (Layer l:this.layers){
			l._gen(li);
			li=l.o_size(li);
		}
	}



	private double[] _predict(double[][][] i){
		CNNVariable v=new CNNVariable(i);
		for (Layer l:this.layers){
			// System.out.println(l._name());
			v.set(l.out(v));
			if (v._NaN()){
				System.exit(1);
			}
		}
		return v.get_1d();
	}



	private void _train(Dataset dt,int bs,boolean log){
		int l=-1;
		int k=0;
		int[] il=dt.get_r_batch_idx(bs);
		for (int i:il){
			if (log==true&&Math.floor((double)(k)/bs*100)>l){
				l=(int)Math.floor((double)(k)/bs*100);
				System.out.printf("%d%% complete...\n",l);
			}
			double[] o=this._predict(dt.getI(i));
			double[] to=new double[this.cL.length];
			for (int j=0;j<this.cL.length;j++){
				if (this.cL[j].equals(dt.getL(i))){
					to[j]=1d;
					break;
				}
			}
			double[] fce=new double[o.length];
			for (int j=0;j<o.length;j++){
				fce[j]=(o[j]-to[j]);
			}
			CNNVariable v=new CNNVariable(fce);
			for (int j=this.layers.size()-1;j>=0;j--){
				v.set(this.layers.get(j).err(v,this.lr));
			}
			k++;
		}
	}



	private String _pad(int l,String c){
		String o="";
		for (int i=0;i<l;i++){
			o+=c;
		}
		return o;
	}
}