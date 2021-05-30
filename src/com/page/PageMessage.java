package com.page;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

public class PageMessage {
	
	private static Page<Object> page = null;
	
    public static void getPage(HttpServletRequest request){
    	
     String index = request.getParameter("index");
   	 int pageindex = 1;
   	 if(index!=null){
   		 pageindex = Integer.parseInt(index);
   	 }
   	 page = PageHelper.startPage(pageindex,6);
    }
    
    public static void getPageDetail(HttpServletRequest request){
    	
   	 request.setAttribute("index", page.getPageNum());
   	 request.setAttribute("pages", page.getPages());
   	 request.setAttribute("total", page.getTotal());
    }
}
