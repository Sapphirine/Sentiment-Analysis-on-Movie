/** 
 * NavieBays.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
 */

package com.cu.bigdata.moviereview;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.classifier.AbstractVectorClassifier;
import org.apache.mahout.classifier.naivebayes.ComplementaryNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import com.cu.bigdata.moviereview.data.DirSwitch;
import com.cu.bigdata.moviereview.data.ModelConfig;

public class NavieBays {

	private AbstractVectorClassifier classifier;
	
	public void toSequenceFile(List<DataInstance> vectors) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);

		Path seqFilePath = new Path("src/main/resources/data/NavieBays/seq");
		fs.delete(seqFilePath, false);

		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf,
				seqFilePath, Text.class, VectorWritable.class);

		try {
			// initial the labels
			for (DataInstance vector : vectors) {
				VectorWritable vectorWritable = new VectorWritable();
				vectorWritable.set(vector.features);
				writer.append(new Text("/" + vector.lable+ "/"), vectorWritable);
			}
		} finally {
			writer.close();
		}
	}
	
	public Vector predict(Vector vector) throws URISyntaxException {
		Vector prediction = null;
		try {
			Configuration conf = new Configuration();
//			String outputDirectory = "src/main/resources/data/model/NavieBays_" + ModelConfig.FeatureNumber + "/";
			NaiveBayesModel naiveBayesModel = NaiveBayesModel.materialize(new Path(
					new DirSwitch().NavieBays_predict(ModelConfig.FeatureNumber)), conf);
			this.classifier = new ComplementaryNaiveBayesClassifier(naiveBayesModel);
			prediction = classifier.classifyFull(vector);
			
			double sum = 0;
			for (int i = 0; i < 4; i++) {
				sum += prediction.get(i);
			}
			prediction.set(0, prediction.get(0)/sum);
			prediction.set(1, prediction.get(1)/sum);
			prediction.set(2, prediction.get(2)/sum);
			prediction.set(3, prediction.get(3)/sum);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prediction;
	}

	public void train() throws Throwable {

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);

		TrainNaiveBayesJob trainNaiveBayes = new TrainNaiveBayesJob();
		trainNaiveBayes.setConf(conf);

		String sequenceFile = "src/main/resources/data/NavieBays/seq";
//		String outputDirectory = "data/NavieBays/output";
		String outputDirectory = "src/main/resources/data/model/NavieBays_" + ModelConfig.FeatureNumber + "/";
		String tempDirectory = "src/main/resources/data/NavieBays/temp";

		fs.delete(new Path(outputDirectory), true);
		fs.delete(new Path(tempDirectory), true);

		trainNaiveBayes.run(new String[] { "--input", sequenceFile, "--output",
				outputDirectory, "-el", "--overwrite", "--tempDir",
				tempDirectory });

		// Train the classifier
		NaiveBayesModel naiveBayesModel = NaiveBayesModel.materialize(new Path(
				outputDirectory), conf);

		System.out.println("features: " + naiveBayesModel.numFeatures());
		System.out.println("labels: " + naiveBayesModel.numLabels());

		this.classifier = new ComplementaryNaiveBayesClassifier(naiveBayesModel);
	}
	
	public double test(List<DataInstance> vectors) {

		int total = 0;
		int success = 0;

		for (DataInstance vector : vectors) {
			Vector prediction = classifier.classifyFull(vector.features);

			if (prediction.maxValueIndex()==vector.lable) {
				success++;
			}

			total++;
		}

		System.out.println("Total: " + total);
		System.out.println("Success: " + success);
		System.out.println("Fail: " + (total - success));
		System.out.println("Precise: " + ((double) success / total));

		// StandardNaiveBayesClassifier classifier = new
		// StandardNaiveBayesClassifier();
		return ((double) success / total);
	}

	public void binaryTest(List<DataInstance> vectors) {

		int total = 0;
		int success = 0;

		for (DataInstance vector : vectors) {
			Vector prediction = classifier.classifyFull(vector.features);

			int highestScore = 0;
			Integer predictedLabel = 0;
			for (int i = 0; i < 4; i++) {
				if (prediction.get(i) > highestScore) {
					predictedLabel = i;
				}
			}
			predictedLabel = predictedLabel <= 1? 0 : 1;
			vector.lable = vector.lable <=1 ? 0 : 1;
			if (predictedLabel.equals(vector.lable)) {
				success++;
			}

			total++;
		}

		System.out.println("Total: " + total);
		System.out.println("Success: " + success);
		System.out.println("Fail: " + (total - success));
		System.out.println("Precise: " + ((double) success / total));
	}
	
	public static void main(String[] argv) {
		
		try {
			ModelConfig.FeatureNumber = 5000;
			// James Dennis+Schwartz
			String to = "src/main/resources/data/train_log_10000_James.csv";
			
			// preprecess train data
//			PreProcessor processor = new PreProcessor();
//			processor.toVector(tp, lp, to);
			
			// train
			NavieBays model = new NavieBays();
			CSVHelper chCsvHelper = new CSVHelper();
			List<DataInstance> dInstance = chCsvHelper.ReadFromCSV(to);
			
			Random rand =  new Random();
			
			//10 fold cross validation
			List<List<DataInstance>> datafolds = new ArrayList<List<DataInstance>>();
			
			for(int i = 0;i<10;i++){
				datafolds.add(new ArrayList<DataInstance>());
			}
			
			for(DataInstance instance:dInstance){
				int a = rand.nextInt(10);
				datafolds.get(a).add(instance);
			}
			
			double totalaccuricy = 0;
			for(int i = 0;i<10;i++){
				// train
				System.out.println("[LRModel]Fold: "+i+" training...");
								
				List<DataInstance> trainset = new ArrayList<DataInstance>();
				List<DataInstance> testset = new ArrayList<DataInstance>();
				testset.addAll(datafolds.get(i));
				for(int j = 0;j<10;j++){
					if(j==i) continue;
					trainset.addAll(datafolds.get(j));
				}
				model.toSequenceFile(trainset);
				model.train();

				// test
				System.out.println("[LRModel]Fold: "+i+" Testing...");
				totalaccuricy += model.test(testset);
				System.out.println("[LRModel]Fold: "+i+" Complete!");
			}
			
			System.out.println("[LRModel] Average accuricy:"+totalaccuricy/10);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
}
