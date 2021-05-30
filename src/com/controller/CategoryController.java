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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.page.PageMessage;

import java.util.*;

@Controller
public class CategoryController extends BaseController {
	@Resource
	CategoryDAO categoryDAO;
	
	@RequestMapping("/admin/categoryList")
	public String categoryList(HttpServletRequest request) {
		String key = request.getParameter("key");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		List<Category> list = categoryDAO.selectAll(map);
	   	PageMessage.getPageDetail(request);
	   	request.setAttribute("key", key);
		request.setAttribute("list", list);
		return "admin/categorylist";
	}
	
	
	
	@RequestMapping("/admin/categoryAdd")
	public String dingdanAdd(Category ct,HttpServletRequest request){
		categoryDAO.add(ct);
		return "redirect:categoryList.do";
	}
	
	@RequestMapping("/admin/showCategory")
	public String showCategory(int id,HttpServletRequest request){
		Category category =  categoryDAO.findById(id);
		request.setAttribute("category", category);
		return "admin/categoryedit";
	}
	
	@RequestMapping("/admin/categoryEdit")
	public String categoryEdit(Category category,HttpServletRequest request){
		categoryDAO.update(category);
		request.setAttribute("category", category);
		return "redirect:categoryList.do";
	}
	
	@RequestMapping("admin/categoryDelAll")
	public String categoryDelAll(int id, HttpServletRequest request,HttpServletResponse response){
		categoryDAO.delete(id);
		return "redirect:categoryList.do";
	}
	
	
	

}
