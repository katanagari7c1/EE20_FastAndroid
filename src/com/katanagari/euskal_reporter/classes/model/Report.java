package com.katanagari.euskal_reporter.classes.model;

public class Report {
	private String category = "";
	private String description = "";
	
	public String getCategory(){
		return this.category;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
}
