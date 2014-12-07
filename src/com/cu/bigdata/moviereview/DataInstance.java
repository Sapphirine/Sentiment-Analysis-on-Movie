package com.cu.bigdata.moviereview;

import org.apache.mahout.math.Vector;

public class DataInstance {
	public Vector features;
	public Integer lable;
	public DataInstance(Vector features, Integer lable) {
		this.features = features;
		this.lable = lable;
	}
	public Vector getFeatures() {
		return features;
	}
	public void setFeatures(Vector features) {
		this.features = features;
	}
	public Integer getLable() {
		return lable;
	}
	public void setLable(Integer lable) {
		this.lable = lable;
	}
	
}
