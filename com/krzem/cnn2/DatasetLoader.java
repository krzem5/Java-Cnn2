package com.krzem.cnn2;



import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;



public class DatasetLoader{
	public static Dataset load(String fp){
		try{
			Map<String,List<double[][][]>> data=new HashMap<String,List<double[][][]>>();
			for (String lb:new String[]{"empty","bottle","fish"}){
				data.put(lb,new ArrayList<double[][][]>());
				for (File f:new File(fp+lb+"\\").listFiles()){
					BufferedImage i=ImageIO.read(f);
					double[][][] dt=new double[1][32][32];
					for (int j=0;j<32;j++){
						for (int k=0;k<32;k++){
							dt[0][j][k]=(i.getRGB(j,k)&0xff)/255d;
						}
					}
					data.get(lb).add(dt);
				}
			}
			return new Dataset(data);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}