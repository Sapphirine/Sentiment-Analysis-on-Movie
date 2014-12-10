/** 
 * MainController.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.Vector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cu.bigdata.moviereview.LogisticRegressionModel;
import com.cu.bigdata.moviereview.NavieBays;
import com.cu.bigdata.moviereview.BuildVector.VectorBuilderForWeb;
import com.cu.bigdata.moviereview.data.ModelConfig;
import com.cu.bigdata.moviereview.data.ModelData;
import com.cu.bigdata.moviereview.data.RatingAnalysisResponseData;

@Controller
@RequestMapping("/")
public class MainController {

	@RequestMapping(method = RequestMethod.GET)
    public String mainpage(){
        return "redirect:index.html";
    }
	
	@RequestMapping(value="/getRatingAnalysis/{text}", method = RequestMethod.GET)
    public @ResponseBody RatingAnalysisResponseData getRatingAnalysis(@PathVariable String text) throws IOException, URISyntaxException {
		RatingAnalysisResponseData responseData = new RatingAnalysisResponseData();
		System.out.println(text);
		// parse text and get vector
		VectorBuilderForWeb vectorBuilder = new VectorBuilderForWeb();
		Vector v1000 = vectorBuilder.buildVector(text, 1000);
		Vector v5000 = vectorBuilder.buildVector(text, 5000);
		Vector v10000 = vectorBuilder.buildVector(text, 10000);
		
		// navie bays predict
		NavieBays nbBays = new NavieBays();
		ModelConfig.FeatureNumber = 1000;
		Vector nbV1000 = nbBays.predict(v1000);
		ModelConfig.FeatureNumber = 5000;
		Vector nbV5000 = nbBays.predict(v5000);
		ModelConfig.FeatureNumber = 10000;
		Vector nbV10000 = nbBays.predict(v10000);
		
		// log regression predict
		LogisticRegressionModel lModel1000 = new LogisticRegressionModel(1000);
		Vector lrV1000 = lModel1000.LRClassifyInstance(v1000);
		LogisticRegressionModel lModel5000 = new LogisticRegressionModel(5000);
		Vector lrV5000 = lModel5000.LRClassifyInstance(v5000);;
		LogisticRegressionModel lModel10000 = new LogisticRegressionModel(10000);
		Vector lrV10000 = lModel10000.LRClassifyInstance(v10000);;
		
		// prepare response data
		List<ModelData> modelDatas1000 = new ArrayList<>();
		modelDatas1000.add(new ModelData("0", nbV1000.get(0), lrV1000.get(0)));
		modelDatas1000.add(new ModelData("1", nbV1000.get(1), lrV1000.get(1)));
		modelDatas1000.add(new ModelData("2", nbV1000.get(2), lrV1000.get(2)));
		modelDatas1000.add(new ModelData("3", nbV1000.get(3), lrV1000.get(3)));
		responseData.setF1000(modelDatas1000);
		
		List<ModelData> modelDatas5000 = new ArrayList<>();
		modelDatas5000.add(new ModelData("0", nbV5000.get(0), lrV5000.get(0)));
		modelDatas5000.add(new ModelData("1", nbV5000.get(1), lrV5000.get(1)));
		modelDatas5000.add(new ModelData("2", nbV5000.get(2), lrV5000.get(2)));
		modelDatas5000.add(new ModelData("3", nbV5000.get(3), lrV5000.get(3)));
		responseData.setF5000(modelDatas5000);
		
		List<ModelData> modelDatas10000 = new ArrayList<>();
		modelDatas10000.add(new ModelData("0", nbV10000.get(0), lrV10000.get(0)));
		modelDatas10000.add(new ModelData("1", nbV10000.get(1), lrV10000.get(1)));
		modelDatas10000.add(new ModelData("2", nbV10000.get(2), lrV10000.get(2)));
		modelDatas10000.add(new ModelData("3", nbV10000.get(3), lrV10000.get(3)));
		responseData.setF10000(modelDatas10000);
		
		return responseData;
    }
	
}
