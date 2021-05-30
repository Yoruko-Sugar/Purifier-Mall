package com.entity;

import java.io.Serializable;
import java.util.*;

public class Picture implements Serializable{
	private int id;
	private String filename;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	@Override
	public String toString() {
		return "Picture [filename=" + filename + ", id=" + id + "]";
	}
	
	
	
	
}
