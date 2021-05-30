package com.controller;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.persistence.Temporal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.page.PageMessage;

import java.util.*;

@Controller
public class ServicerController extends BaseController {
	@Resource
	ServicerDAO servicerDAO;
	
	//后台客服列表
	@RequestMapping("/admin/servicerList")
	public String servicerList(HttpServletRequest request) {
		String key = request.getParameter("key");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		List<Servicer> list = servicerDAO.selectAll(map);
	   	PageMessage.getPageDetail(request);
	   	request.setAttribute("key", key);
		request.setAttribute("list", list);
		return "admin/servicerlist";
	}
	
	//添加客服
	@RequestMapping("/admin/servicerAdd")
	public String servicerAdd(Servicer servicer,HttpServletRequest request) {
		servicerDAO.add(servicer);
		return "redirect:servicerList.do";
	}
	
	//查找客服信息到修改页面
	@RequestMapping("/admin/showServicer")
	public String showServicer(int id,HttpServletRequest request) {
		Servicer servicer = servicerDAO.findById(id);
		request.setAttribute("servicer", servicer);
		return "admin/serviceredit";
	}
	
	//修改客服信息
	@RequestMapping("/admin/servicerEdit")
	public String showServicer(Servicer servicer,HttpServletRequest request) {
		servicerDAO.update(servicer);
		return "redirect:servicerList.do";
	}
	
	//删除客服信息
	@RequestMapping("/admin/servicerDelAll")
	public String servicerDelAll(int id,HttpServletRequest request) {
		servicerDAO.delete(id);
		return "redirect:servicerList.do";
	}
	
	
	
	
}
