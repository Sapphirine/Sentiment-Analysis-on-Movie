/** 
 * MyComparator.java 
 * Hao Tong
 * Email: tonghaozju@gmail.com
*/

package com.cu.bigdata.moviereview.BuildVector;

import java.util.Comparator;
import java.util.Map;

public class MyComparator implements Comparator<String>{
	Map<String, Double> map;
	public MyComparator(Map<String, Double> base) {
		this.map = base;
	}
	public int compare(String o1, String o2) {
		if (map.get(o1)>=map.get(o2)) {
			return -1;
		}
		return 1;
	}
}
