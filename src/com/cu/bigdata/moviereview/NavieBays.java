/** 
 * NavieBays.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
 */

package com.cu.bigdata.moviereview;

import java.io.IOException;
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

import com.cu.bigdata.moviereview.BuildVector.PreProcessor;

public class NavieBays {

	private AbstractVectorClassifier classifier;
	
	public void toSequenceFile(List<DataInstance> vectors) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);

		Path seqFilePath = new Path("data/NavieBays/seq");
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

	public void train() throws Throwable {

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.getLocal(conf);

		TrainNaiveBayesJob trainNaiveBayes = new TrainNaiveBayesJob();
		trainNaiveBayes.setConf(conf);

		String sequenceFile = "data/NavieBays/seq";
		String outputDirectory = "data/NavieBays/output";
		String tempDirectory = "data/NavieBays/temp";

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
	
	public void test(List<DataInstance> vectors) {

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
			
			if (predictedLabel.equals(vector.lable)) {
				success++;
			}

			total++;
		}

		System.out.println("Total: " + total);
		System.out.println("Success: " + success + " : " + (total - success));
		System.out.println("Fail: " + (total - success));
		System.out.println("Precise: " + ((double) success / total));

		// StandardNaiveBayesClassifier classifier = new
		// StandardNaiveBayesClassifier();
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
			NavieBays model = new NavieBays();
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
			
			model.toSequenceFile(train);
			model.train();
			
			// preprecess train data
			processor.toVector(tpt, lpt, tot);
			
			// test
//			model.test(test);
			model.binaryTest(test);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
}
