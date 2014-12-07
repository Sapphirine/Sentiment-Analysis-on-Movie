package com.cu.bigdata.moviereview;

import com.google.common.collect.Lists;

import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogisticRegressionModel {
	private OnlineLogisticRegression learningAlgo;
	
	public LogisticRegressionModel(int NumCate,int NumFeature,double lambda,int learningRate){
		learningAlgo = new OnlineLogisticRegression(NumCate, NumFeature, new L1());
        learningAlgo.lambda(lambda);
        learningAlgo.learningRate(learningRate);
	}
	
	public LogisticRegressionModel(String model_path) throws IOException{
        InputStream in = new FileInputStream(model_path);
        learningAlgo = ModelSerializer.readBinary(in, OnlineLogisticRegression.class);
        in.close();
	}
	
	public boolean LRTrainModel(List<DataInstance> trainset,String model_path) throws IOException{
		for(DataInstance instance:trainset){
			learningAlgo.train(instance.lable, instance.features);
		}
		ModelSerializer.writeBinary(model_path, learningAlgo);
        learningAlgo.close();
		return true;
	}
	
	public int LRClassify(Vector feature){
		Vector r = learningAlgo.classifyFull(feature);
		return r.maxValueIndex();
	}
	
	public void LRTest(List<DataInstance> testset){
		int total = 0;
		int success = 0;

		for (DataInstance vector : testset) {
			Vector prediction = learningAlgo.classifyFull(vector.features);
			
			if (prediction.maxValueIndex()==vector.lable) {
				success++;
			}
			total++;
		}

		System.out.println("Total: " + total);
		System.out.println("Success: " + success + " : " + (total - success));
		System.out.println("Fail: " + (total - success));
		System.out.println("Precise: " + ((double) success / total));
	}
	
}
