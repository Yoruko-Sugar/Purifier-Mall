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
import com.util.Info;

import java.util.*;

@Controller
public class InventoryController extends BaseController {
	@Resource
	CategoryDAO categoryDAO;
	@Resource
	InventoryDAO inventoryDAO;
	@Resource
	ProductDAO productDAO;
	
	@RequestMapping("/admin/inventoryList")
	public String inventoryList(HttpServletRequest request) {
		HashMap mmm = new HashMap();
		List<Category> ctlist = categoryDAO.selectAll(mmm);
		request.setAttribute("ctlist", ctlist);
		
		String index = request.getParameter("index");
		String key = request.getParameter("key");
		String key1 = request.getParameter("key1");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
   	    HashMap map = new HashMap();
   	    map.put("key", key);
   	    map.put("key1", key1);
   	    List<Product> list = productDAO.selectAll(map);
		for(int i=0;i<list.size();i++){
			Category category = categoryDAO.findById(Integer.parseInt(list.get(i).getCategoryid()));
			list.get(i).setCategory(category);
			int totalnum = Info.getInventory(list.get(i).getId());
			Inventory inventory = new Inventory();
			inventory.setNum(totalnum);
			list.get(i).setInventory(inventory);
		}
		request.setAttribute("list", list);
		request.setAttribute("key", key);
		request.setAttribute("key1", key1);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/inventorylist";
	}
	
	@RequestMapping("/admin/searchInventory")
	public String searchInventory(HttpServletRequest request) {
		String index = request.getParameter("index");
		String key = request.getParameter("key");
		String key1 = request.getParameter("key1");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
   	    List<Product> list = productDAO.search(key, key1);
		for(int i=0;i<list.size();i++){
			Category category = categoryDAO.findById(Integer.parseInt(list.get(i).getCategoryid()));
			list.get(i).setCategory(category);
			int totalnum = Info.getInventory(list.get(i).getId());
			Inventory inventory = new Inventory();
			inventory.setNum(totalnum);
			list.get(i).setInventory(inventory);
		}
		request.setAttribute("list", list);
		request.setAttribute("key", key);
		request.setAttribute("key1", key1);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/inventorysearchlist";
	}
	
	@RequestMapping("/admin/inventoryAdd")
	public String inventoryAdd(Inventory inventory, HttpServletRequest request) {
		inventoryDAO.add(inventory);
		return "redirect:inventoryList.do";
	}
	
	

}
