/** 
 * ModelData.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.data;

public class ModelData {

	private String Method;
	private Double NBScore;
	private Double LRScore;
	
	public ModelData() {
		super();
	}

	public ModelData(String method, Double nBScore, Double lRScore) {
		super();
		Method = method;
		NBScore = nBScore;
		LRScore = lRScore;
	}

	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	public Double getNBScore() {
		return NBScore;
	}
	public void setNBScore(Double nBScore) {
		NBScore = nBScore;
	}
	public Double getLRScore() {
		return LRScore;
	}
	public void setLRScore(Double lRScore) {
		LRScore = lRScore;
	}
	
	
	
}
