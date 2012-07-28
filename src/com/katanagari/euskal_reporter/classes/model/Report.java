package com.katanagari.euskal_reporter.classes.model;

public class Report {
	private String description = "";
	
	public Report(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
}
