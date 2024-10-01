package com.compulynx.compas.models.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineChart {
	public static final List<String> YEAR =Arrays.asList("Jan", "Feb", "Mat", "Apr", "May", "Jun", "Jul", "Aug","Sep","Oct","Nov","Dec");
	private List<String> labels ;
	
	ArrayList<ArrayList<Integer>> series;

	public LineChart(List<String> labels, ArrayList<ArrayList<Integer>>series) {
		super();
		this.labels = labels;
		this.series = series;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public ArrayList<ArrayList<Integer>> getSeries() {
		return series;
	}

	public void setSeries(ArrayList<ArrayList<Integer>> series) {
		this.series = series;
	}
	
	
}
