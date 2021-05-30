package com.entity;

import java.io.Serializable;
import java.util.*;

public class About implements Serializable{
	private int id;
	private String content;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "About [content=" + content + ", id=" + id + "]";
	}
	
	
	
	
}
