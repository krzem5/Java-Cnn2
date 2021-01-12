package com.krzem.cnn2;



import java.util.ArrayList;
import java.util.List;



public class Main{
	public static void main(String[] args){
		new Main();
	}



	public Main(){
		List<Layer> ll=new ArrayList<Layer>();
		ll.add(new ConvolutionLayer(32,5,5,"sigmoid"));
		ll.add(new ConvolutionLayer(32,5,5,"sigmoid"));
		ll.add(new MaxPoolingLayer(2,2,1,1));
		ll.add(new DropoutLayer(0.25));
		ll.add(new ConvolutionLayer(64,3,3,"sigmoid"));
		ll.add(new ConvolutionLayer(64,3,3,"sigmoid"));
		ll.add(new MaxPoolingLayer(2,2,2,2));
		ll.add(new DropoutLayer(0.25));
		ll.add(new FlattenLayer());
		ll.add(new DenseLayer(256,"sigmoid"));
		ll.add(new DropoutLayer(0.5));
		ll.add(new DenseLayer(3,"sigmoid"));
		CNN cnn=new CNN(32,32,1,ll,new String[]{"empty","fish","bottle"},7.5e-3);
		cnn.print();
		Dataset tr=DatasetLoader.load("D:\\K\\Project\\project2\\DATA\\final\\train\\");
		Dataset ts=DatasetLoader.load("D:\\K\\Project\\project2\\DATA\\final\\test\\");
		System.out.println(Long.toString(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		cnn.load("./data/0.cnn-data");
		System.out.println(cnn.predict(tr.get("empty").get(0)));
		System.out.println(cnn.predict(tr.get("bottle").get(0)));
		cnn.train(2,64,1,tr,ts,true);
		System.out.println(cnn.predict(tr.get("empty").get(0)));
		System.out.println(cnn.predict(tr.get("bottle").get(0)));
		System.out.printf("Test acc: %f\n",cnn.acc(ts));
		// cnn.save("./data/0.cnn-data");
	}
}