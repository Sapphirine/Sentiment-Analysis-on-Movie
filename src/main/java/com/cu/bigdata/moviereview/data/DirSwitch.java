/** 
 * DirSwitch.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class DirSwitch {

	private static boolean LOCAL = false;
	
	public File FeatureDictBuilder_readDictWithMI(int numFeture) throws URISyntaxException {
		String path = "data/dict/dict_" + String.valueOf(numFeture) + ".ser";
		if (LOCAL) {
			return new File("src/main/resources/" + path);
		} else {
//			System.out.println(path);
			return new File(this.getClass().getResource("/" + path).toURI());
		}
	}
	
	public URI NavieBays_predict(int numFeature) throws URISyntaxException {
		String path = "/data/model/NavieBays_" + String.valueOf(numFeature) + "/";
//		if (LOCAL) {
//			return "src/main/resources" + path;
//		} else {
			return this.getClass().getResource(path).toURI();
//		}
	}
	
	public File LogisticRegressionModel_CONS(int numFeture) throws URISyntaxException {
		String path = "data/model/LogisticRegressionModels/f" + String.valueOf(numFeture);
		if (LOCAL) {
			return new File("src/main/resources/" + path);
		} else {
			return new File(this.getClass().getResource("/" + path).toURI());
		}
	}
}
