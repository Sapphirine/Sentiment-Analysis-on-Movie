/** 
 * MainController.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.controller;

import org.apache.mahout.math.Vector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cu.bigdata.moviereview.NavieBays;
import com.cu.bigdata.moviereview.BuildVector.VectorBuilderForWeb;
import com.cu.bigdata.moviereview.data.ModelConfig;
import com.cu.bigdata.moviereview.data.RatingAnalysisResponseData;

@Controller
@RequestMapping("/")
public class MainController {

	@RequestMapping(method = RequestMethod.GET)
    public String mainpage(){
        return "redirect:index.html";
    }
	
	@RequestMapping(value="/getRatingAnalysis/{text}", method = RequestMethod.GET)
    public @ResponseBody RatingAnalysisResponseData getRatingAnalysis(@PathVariable String text) {
		RatingAnalysisResponseData responseData = new RatingAnalysisResponseData();
		
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
		Vector lrV1000;
		Vector lrV5000;
		Vector lrV10000;
		
		// prepare response data
//		responseData.setF1000(f1000);
		
		return responseData;
    }
	
}
