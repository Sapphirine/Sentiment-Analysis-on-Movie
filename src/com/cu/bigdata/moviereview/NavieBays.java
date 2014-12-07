/** 
 * NavieBays.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
 */

package com.cu.bigdata.moviereview;

import java.io.IOException;
import java.util.List;

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

public class NavieBays {

	private AbstractVectorClassifier classifier;
	
	public static void toSequenceFile(List<DataInstance> vectors) throws IOException {
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

	public static void main(String[] argv) {
	}
}
