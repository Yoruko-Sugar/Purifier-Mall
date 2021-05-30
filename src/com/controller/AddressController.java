package com.controller;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.persistence.Temporal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dao.*;
import com.entity.*;



import java.util.*;

@Controller
public class AddressController extends BaseController {
	@Resource
	AddressDAO addressDAO;
	@Resource
	CategoryDAO categoryDAO;
	@Resource
	CartDAO cartDAO;
	@Resource
	MemberDAO memberDAO;
	@Resource
	ProductDAO productDAO;
	
	
	
	
	
	//前台查询地址列表
	@RequestMapping("addressMsg")
	public String addressMsg(HttpServletRequest request) {
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		List<Address> list = addressDAO.selectAll(member.getId());
		request.setAttribute("list", list);
		return "addressmsg";
	}
	
	
	
	
	//添加地址 
	@RequestMapping("addressAdd")
	public String addressAdd(Address address,HttpServletRequest request){
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		address.setMemberid(member.getId());
		addressDAO.add(address);
		return "redirect:addressMsg.do";
	}
	
	
	
	//删除地址
	@RequestMapping("addressDel")
	public String addressDelAll(int id,HttpServletRequest request){
		addressDAO.delete(id);
		return "redirect:addressMsg.do";
	}
	

}
