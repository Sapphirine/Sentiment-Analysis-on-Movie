package com.cu.bigdata.moviereview;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.Vector;

import com.cu.bigdata.moviereview.BuildVector.PreProcessor;
import com.cu.bigdata.moviereview.data.ModelConfig;

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
	
	public static void main(String[] argv) throws IOException {
		
		String tp = "data/James+Berardinelli/subj.James+Berardinelli";
		String lp = "data/James+Berardinelli/label.4class.James+Berardinelli";
		String to = "data/train.csv";
		
		String tpt = "data/Dennis+Schwartz/subj.Dennis+Schwartz";
		String lpt = "data/Dennis+Schwartz/label.4class.Dennis+Schwartz";
		String tot = "data/test.csv";
		
		// preprecess train data
		PreProcessor processor = new PreProcessor();
		processor.toVector(tp, lp, to);
		
		// train
		LogisticRegressionModel model = new LogisticRegressionModel(4, ModelConfig.FeatureNumber, 0.1, 4);
		CSVHelper chCsvHelper = new CSVHelper();
		List<DataInstance> dInstance = chCsvHelper.ReadFromCSV(to);
		
		List<DataInstance> train = new ArrayList<DataInstance>();
		List<DataInstance> test = new ArrayList<DataInstance>();
		
		Random rand =  new Random();
		for (DataInstance dataInstance : dInstance) {
			int a = rand.nextInt(10);
			if (a<=7) {
				train.add(dataInstance);
			} else {
				test.add(dataInstance);
			}
		}
		
		model.LRTrainModel(train, "data/model/lr_model");
		
		// test
		model.LRTest(test);
	}
	
}
