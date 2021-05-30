package com.entity;

import java.io.Serializable;
import java.util.*;

public class Product implements Serializable{
	private int id;
	private String productname;
	private String filename;
	private double price;
	private double discount;
	private String categoryid;
	private String content;
	private String delstatus;
	private int looknum;
	private Category category;
	private Inventory inventory;
	private int totalnum;
	private double likescore;
	private int vote;
	private String isxs;
	private double plusprice;
	private String issj;
	private String jdcontent;
	private String isys;
	
	public String getIsys() {
		return isys;
	}
	public void setIsys(String isys) {
		this.isys = isys;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDelstatus() {
		return delstatus;
	}
	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	
	public double getLikescore() {
		return likescore;
	}
	public void setLikescore(double likescore) {
		this.likescore = likescore;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	
	
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	
	
	public int getLooknum() {
		return looknum;
	}
	public void setLooknum(int looknum) {
		this.looknum = looknum;
	}
	
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getIsxs() {
		return isxs;
	}
	public void setIsxs(String isxs) {
		this.isxs = isxs;
	}
	
	public double getPlusprice() {
		return plusprice;
	}
	public void setPlusprice(double plusprice) {
		this.plusprice = plusprice;
	}
	
	
	public String getIssj() {
		return issj;
	}
	public void setIssj(String issj) {
		this.issj = issj;
	}
	public String getJdcontent() {
		return jdcontent;
	}
	public void setJdcontent(String jdcontent) {
		this.jdcontent = jdcontent;
	}
	@Override
	public String toString() {
		return "Product [category=" + category + ", categoryid=" + categoryid
				+ ", content=" + content + ", delstatus=" + delstatus
				+ ", filename=" + filename + ", id=" + id + ", inventory="
				+ inventory + ", price=" + price + ", productname="
				+ productname + ", totalnum=" + totalnum + "]";
	}
	
	
	
	
	
}
