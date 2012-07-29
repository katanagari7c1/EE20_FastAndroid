package com.katanagari.euskal_reporter.classes.model;

import java.io.Serializable;

public class Report implements Serializable{
	private static final long serialVersionUID = -6558446850738676489L;
	
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
