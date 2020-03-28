package com.krzem.cnn2 - Copy;



import java.util.ArrayList;
import java.util.List;



public class Main{
	public static void main(String[] args){
		new Main();
	}



	public Main(){
		List<Layer> ll=new ArrayList<Layer>();
		ll.add(new ConvolutionLayer(32,5,5,"relu"));
		ll.add(new ConvolutionLayer(32,5,5,"relu"));
		ll.add(new MaxPoolingLayer(2,2,1,1));
		ll.add(new DropoutLayer(0.25));
		ll.add(new ConvolutionLayer(64,3,3,"relu"));
		ll.add(new ConvolutionLayer(64,3,3,"relu"));
		ll.add(new MaxPoolingLayer(2,2,2,2));
		ll.add(new DropoutLayer(0.25));
		ll.add(new FlattenLayer());
		ll.add(new DenseLayer(256,"sigmoid"));
		ll.add(new DropoutLayer(0.5));
		ll.add(new DenseLayer(10,"softmax"));
		CNN cnn=new CNN(28,28,1,ll,new String[]{"0","1","2","3","4","5","6","7","8","9"},7.5e-3);
		cnn.print();
		Dataset tr=MNISTDatasetLoader.load("./mnist/train.csv");
		Dataset ts=MNISTDatasetLoader.load("./mnist/test.csv");
		System.out.println(Long.toString(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		cnn.load("./data/0.cnn-data");
		// System.out.printf("Train acc: %f\n",cnn.acc(tr));
		// System.out.printf("Test acc: %f\n",cnn.acc(ts));
		System.out.println(cnn.predict(tr.get("0").get(0)));
		System.out.println(cnn.predict(tr.get("7").get(0)));
		cnn.train(30,32,2,tr,ts,true);
		// System.out.printf("Train acc: %f\n",cnn.acc(tr));
		System.out.printf("Test acc: %f\n",cnn.acc(ts));
		System.out.println(cnn.predict(tr.get("0").get(0)));
		System.out.println(cnn.predict(tr.get("7").get(0)));
		// cnn.save("./data/0.cnn-data");
	}
}