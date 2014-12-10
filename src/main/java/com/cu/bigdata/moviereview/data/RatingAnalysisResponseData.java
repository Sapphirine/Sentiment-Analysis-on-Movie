/** 
 * RatingAnalysisResponseData.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.data;

import java.util.List;

public class RatingAnalysisResponseData {

	List<ModelData> f1000;
	
	List<ModelData> f5000;
	
	List<ModelData> f10000;
	
	public RatingAnalysisResponseData() {
		super();
	}

	public RatingAnalysisResponseData(List<ModelData> f1000,
			List<ModelData> f5000, List<ModelData> f10000) {
		super();
		this.f1000 = f1000;
		this.f5000 = f5000;
		this.f10000 = f10000;
	}

	public List<ModelData> getF1000() {
		return f1000;
	}

	public void setF1000(List<ModelData> f1000) {
		this.f1000 = f1000;
	}

	public List<ModelData> getF5000() {
		return f5000;
	}

	public void setF5000(List<ModelData> f5000) {
		this.f5000 = f5000;
	}

	public List<ModelData> getF10000() {
		return f10000;
	}

	public void setF10000(List<ModelData> f10000) {
		this.f10000 = f10000;
	}
	
}
