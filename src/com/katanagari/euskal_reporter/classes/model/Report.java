package com.katanagari.euskal_reporter.classes.model;

public class Report {
	private String category = "";
	private String description = "";
	private String photoPath = "";
	
	public String getCategory(){
		return this.category;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getPhotoPath(){
		return this.photoPath;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setPhotoPath(String photo_path){
		this.photoPath = photo_path;
	}
}
