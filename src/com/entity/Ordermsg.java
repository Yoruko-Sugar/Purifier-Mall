package com.entity;

import java.io.Serializable;
import java.util.*;

public class Ordermsg implements Serializable{
	private int id;
	private String orderno;
	private int memberid;
	private String total;
	private String fkstatus;
	private String fhstatus;
	private String shstatus;
	private String addrid;
	private String savetime;
	private Member member;
	private Address address;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public int getMemberid() {
		return memberid;
	}
	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getFkstatus() {
		return fkstatus;
	}
	public void setFkstatus(String fkstatus) {
		this.fkstatus = fkstatus;
	}
	public String getFhstatus() {
		return fhstatus;
	}
	public void setFhstatus(String fhstatus) {
		this.fhstatus = fhstatus;
	}
	public String getShstatus() {
		return shstatus;
	}
	public void setShstatus(String shstatus) {
		this.shstatus = shstatus;
	}
	public String getAddrid() {
		return addrid;
	}
	public void setAddrid(String addrid) {
		this.addrid = addrid;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	

}
