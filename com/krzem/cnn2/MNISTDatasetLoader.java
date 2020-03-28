package com.krzem.cnn2;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.Exception;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MNISTDatasetLoader{
	public static Dataset load(String fp){
		try{
			Map<String,List<double[][][]>> data=new HashMap<String,List<double[][][]>>();
			BufferedReader br=new BufferedReader(new FileReader(new File(fp)));
			String _l;
			for (int i=0;i<10;i++){
				data.put(Integer.toString(i),new ArrayList<double[][][]>());
			}
			while ((_l=br.readLine())!=null){
				String[] l=_l.split(",");
				double[][][] o=new double[1][28][28];
				for (int i=0;i<28;i++){
					for (int j=0;j<28;j++){
						o[0][j][i]=Integer.parseInt(l[i*28+j])/255d;
					}
				}
				data.get(l[0]).add(o);
			}
			br.close();
			return new Dataset(data);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}