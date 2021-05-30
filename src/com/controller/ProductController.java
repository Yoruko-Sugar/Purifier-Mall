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
import com.util.CommDAO;

import java.util.*;

@Controller
public class ProductController extends BaseController {
	@Resource
	ProductDAO productDAO;
	@Resource
	CategoryDAO categoryDAO;
	@Resource
	CartDAO cartDAO;
	@Resource
	MemberDAO memberDAO;
	@Resource
	CommentDAO commentDAO;
	@Resource
	OrdermsgdetailsDAO ordermsgdetailsDAO;
	@Resource
	VoteDAO voteDAO;
	@Resource
	ServicerDAO servicerDAO;
	
	
	
	//后台查询商品列表
	@RequestMapping("/admin/productList")
	public String productList(HttpServletRequest request) {
		HashMap mmm = new HashMap();
		List<Category> ctlist = categoryDAO.selectAll(mmm);
		request.setAttribute("ctlist", ctlist);
		
		String key = request.getParameter("key");
		String key1 = request.getParameter("key1");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		map.put("key1", key1);
		List<Product> list = productDAO.selectAll(map);
	   	PageMessage.getPageDetail(request);
		for(int i=0;i<list.size();i++){
			Category category = categoryDAO.findById(Integer.parseInt(list.get(i).getCategoryid()));
			list.get(i).setCategory(category);
		}
		request.setAttribute("key", key);
		request.setAttribute("key1", key1);
		request.setAttribute("list", list);
		return "admin/productlist";
	}
	
	
	//查询商品类别
	@RequestMapping("/admin/categorySelect")
	public String categorySelect(HttpServletRequest request){
		HashMap map = new HashMap();
		List<Category> list = categoryDAO.selectAll(map);
		request.setAttribute("list", list);
		return "admin/productadd";
	}
	
	//商品添加
	@RequestMapping("/admin/productAdd")
	public String productAdd(Product product,HttpServletRequest request){
		product.setIssj("yes");
		product.setIsys("no");
		productDAO.add(product);
		return "redirect:productList.do";
	}
	
	
	
	//后台商品查询
	@RequestMapping("/admin/showProduct")
	public String showproduct(int id,HttpServletRequest request){
		Product product =  productDAO.findById(id);
		HashMap map = new HashMap();
		List<Category> list = categoryDAO.selectAll(map);
		request.setAttribute("list", list);
		request.setAttribute("product", product);
		return "admin/productedit";
	}
	
	//前台商品查询
	@RequestMapping("productDetails")
	public String productDetails(int id,HttpServletRequest request){
		String msg = request.getParameter("msg");
		String message = request.getParameter("message");
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				
				//存入会员价
				double plusprice = product.getPrice()*product.getDiscount();
				BigDecimal doubleplusprice = new BigDecimal(plusprice);
				plusprice = doubleplusprice.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				product.setPlusprice(plusprice);
				
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				//是否为会员算会员价
				if(m.getLev().equals("普通用户")){
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
				}else{
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPlusprice();	
				}
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		
		Product product =  productDAO.findById(id);
		double plusprice = product.getPrice()*product.getDiscount();
		//保留一位小数
		BigDecimal doubleplusprice = new BigDecimal(plusprice);
		plusprice = doubleplusprice.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		
		product.setPlusprice(plusprice);
		product.setLooknum(product.getLooknum()+1);
		productDAO.updateLooknum(product);
		List<Product> list = productDAO.selectCorrelation(id,product.getCategoryid());
		Category category = categoryDAO.findById(Integer.parseInt(product.getCategoryid()));
		product.setCategory(category);
		
		if(msg!=null){
			request.setAttribute("msg", msg);
		}
		
		//评论
		List<Comment> commentlist = commentDAO.selectProduct(id);
		Double score = 0.0;
		for(int i=0;i<commentlist.size();i++){
			Member m = memberDAO.findById(commentlist.get(i).getMemberid());
			commentlist.get(i).setMember(m);
			score+= Double.parseDouble(String.valueOf(commentlist.get(i).getQuality()));
		}
		//好感度
		double averageStr=0.0;
		if(commentlist.size()!=0){
		double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
		averageStr = Double.valueOf(String.format("%.1f", average));
		}
		
		//商品类别
		HashMap map = new HashMap();
		List<Category> ctlist = categoryDAO.selectAll(map);
		request.setAttribute("ctlist", ctlist);
		
		//客服
		List<Servicer> svlist = servicerDAO.selectAll(map);
		request.setAttribute("svlist", svlist);
		
		
		request.setAttribute("product", product);
		request.setAttribute("averageStr", averageStr);
		request.setAttribute("commentlist", commentlist);
		request.setAttribute("list", list);
		return "productdetails";
	}
	
	//商品编辑
	@RequestMapping("/admin/productEdit")
	public String productEdit(Product product,HttpServletRequest request){
		productDAO.update(product);
		request.setAttribute("product", product);
		return "redirect:productList.do";
	}
	//商品删除
	@RequestMapping("admin/productDelAll")
	public String productDelAll(int id,HttpServletRequest request,HttpServletResponse response){
			productDAO.delete(id);
		return "redirect:productList.do";
	}
	
	
	//添加评论
	@RequestMapping("commentAdd")
	public String commentAdd(Comment comment, HttpServletRequest request){
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
		List<Ordermsgdetails> list = ordermsgdetailsDAO.selectOne(comment.getProductid(),member.getId());
		if(list.size()!=0){
		comment.setMemberid(member.getId());
		commentDAO.add(comment);
		return "redirect:productDetails.do?id="+comment.getProductid()+"&msg=suc";
		}else{
			return "redirect:productDetails.do?id="+comment.getProductid()+"&msg=msg";
		}	
		}else{
			return "login";
		}
	}
	
	
	//显示商品列表页
	@RequestMapping("product_List")
	public String listProduct(int categoryid, HttpServletRequest request){
		//商品销量排行
		//CommDAO dao  = new CommDAO();
		//List<HashMap> phlist = dao.select("select sum(o.num) as num,o.productid  from ordermsgdetails o,ordermsg g where g.shstatus='已收货' and g.orderno=o.orderno GROUP BY productid order by num desc");
		
		
		
		//显示商品列表
		String index = request.getParameter("index");
		
		String key = request.getParameter("key");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		List<Product> list = productDAO.selectCategory(categoryid);
	   	PageMessage.getPageDetail(request);
		
		
		for(int i=0;i<list.size();i++){
			List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
			Double score = 0.0;
			for(int a=0;a<commentlist.size();a++){
				score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
			}
			double averageStr=0.0;
			if(commentlist.size()!=0){
			double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
			averageStr = Double.valueOf(String.format("%.1f", average));
			}
			list.get(i).setLikescore(averageStr);
		}
		request.setAttribute("list", list);
		request.setAttribute("categoryid", categoryid);
		return "productlist";
	}
	
	//显示商品列表页
	@RequestMapping("product_List_Desc")
	public String listProductDesc(int categoryid, HttpServletRequest request){
		//商品销量排行
		//CommDAO dao  = new CommDAO();
		//List<HashMap> phlist = dao.select("select sum(o.num) as num,o.productid  from ordermsgdetails o,ordermsg g where g.shstatus='已收货' and g.orderno=o.orderno GROUP BY productid order by num desc");
		
		
		
		//显示商品列表
		String index = request.getParameter("index");
		
		String key = request.getParameter("key");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		List<Product> list = productDAO.selectProductDesc(categoryid);
	   	PageMessage.getPageDetail(request);
		
		
		for(int i=0;i<list.size();i++){
			List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
			Double score = 0.0;
			for(int a=0;a<commentlist.size();a++){
				score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
			}
			double averageStr=0.0;
			if(commentlist.size()!=0){
			double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
			averageStr = Double.valueOf(String.format("%.1f", average));
			}
			list.get(i).setLikescore(averageStr);
		}
		request.setAttribute("list", list);
		request.setAttribute("categoryid", categoryid);
		return "productlist";
	}
	
	//显示商品列表页
	@RequestMapping("product_List_Asc")
	public String listProductAsc(int categoryid, HttpServletRequest request){
		//商品销量排行
		//CommDAO dao  = new CommDAO();
		//List<HashMap> phlist = dao.select("select sum(o.num) as num,o.productid  from ordermsgdetails o,ordermsg g where g.shstatus='已收货' and g.orderno=o.orderno GROUP BY productid order by num desc");
		
		
		
		//显示商品列表
		String index = request.getParameter("index");
		
		String key = request.getParameter("key");
		PageMessage.getPage(request);
		HashMap map = new HashMap();
		map.put("key", key);
		List<Product> list = productDAO.selectProductASC(categoryid);
	   	PageMessage.getPageDetail(request);
		
		
		for(int i=0;i<list.size();i++){
			List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
			Double score = 0.0;
			for(int a=0;a<commentlist.size();a++){
				score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
			}
			double averageStr=0.0;
			if(commentlist.size()!=0){
			double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
			averageStr = Double.valueOf(String.format("%.1f", average));
			}
			list.get(i).setLikescore(averageStr);
		}
		request.setAttribute("list", list);
		request.setAttribute("categoryid", categoryid);
		return "productlist";
	}
	
	
	//显示宫格商品列表页
	@RequestMapping("list_Product")
	public String productL(int categoryid, HttpServletRequest request){
		
		
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				//存入会员价
				double plusprice = product.getPrice()*product.getDiscount();
				BigDecimal doubleplusprice = new BigDecimal(plusprice);
				plusprice = doubleplusprice.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				product.setPlusprice(plusprice);
				
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				
				//是否为会员算会员价
				if(m.getLev().equals("普通用户")){
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
				}else{
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPlusprice();	
				}
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		//显示商品列表
			String index = request.getParameter("index");
		   	int pageindex = 1;
		   	if(index!=null){
		   		 pageindex = Integer.parseInt(index);
		   	}
	   	    Page<Object> page = PageHelper.startPage(pageindex,6);
			List<Product> list = productDAO.selectCategory(categoryid);
			for(int i=0;i<list.size();i++){
				List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
				Double score = 0.0;
				for(int a=0;a<commentlist.size();a++){
					score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
				}
				double averageStr=0.0;
				if(commentlist.size()!=0){
				double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
				averageStr = Double.valueOf(String.format("%.1f", average));
				}
				list.get(i).setLikescore(averageStr);
			}
			request.setAttribute("list", list);
			request.setAttribute("categoryid", categoryid);
			request.setAttribute("index", page.getPageNum());
			request.setAttribute("pages", page.getPages());
			request.setAttribute("total", page.getTotal());
			return "product_list";
		}
	
	//商品投票
	@RequestMapping("voteProdcut")
	public String voteProdcut(int id, HttpServletRequest request){
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
		HashMap map = new HashMap();
		map.put("productid", id);
		map.put("memberid", member.getId());
		List<Vote> list = voteDAO.selectAll(map);
		if(list.size()==0){
		Product product = productDAO.findById(id);
		productDAO.updateVote(product);
		
	    Vote vote = new Vote();
	    vote.setMemberid(member.getId());
	    vote.setProductid(id);
	    voteDAO.add(vote);
		
		return "redirect:productDetails.do?id="+id;
		}else{
		return "redirect:productDetails.do?id="+id+"&msg=message";
		}
		}else{
			return "login";
		}
	}
	
	//人气排行
	@RequestMapping("popularityProduct")
	public String popularityProduct(HttpServletRequest request){
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				//存入会员价
				double plusprice = product.getPrice()*product.getDiscount();
				BigDecimal doubleplusprice = new BigDecimal(plusprice);
				plusprice = doubleplusprice.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				product.setPlusprice(plusprice);
				
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				
				//是否为会员算会员价
				if(m.getLev().equals("普通用户")){
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
				}else{
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPlusprice();	
				}
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		
		
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectVote();
		request.setAttribute("list", list);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "votelist";
	}
	
	//预售
	@RequestMapping("yushouProduct")
	public String yushouProduct(HttpServletRequest request){
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				//存入会员价
				double plusprice = product.getPrice()*product.getDiscount();
				BigDecimal doubleplusprice = new BigDecimal(plusprice);
				plusprice = doubleplusprice.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				product.setPlusprice(plusprice);
				
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				
				//是否为会员算会员价
				if(m.getLev().equals("普通用户")){
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
				}else{
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPlusprice();	
				}
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		
		
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectYushou();
		request.setAttribute("list", list);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "yushoulist";
	}
	
	//更新限时商品
	@RequestMapping("admin/updateIsxs")
	public String updateIsxs(int id, HttpServletRequest request){
		String type = request.getParameter("type");
		Product product = new Product();
		product.setId(id);
		if(type.equals("yes")){
			product.setIsxs("no");
		}else{
			product.setIsxs("yes");
		}
		productDAO.updateIsxs(product);
		return "redirect:productList.do";
	}
	//更新预售状态
	@RequestMapping("admin/updateIsys")
	public String updateIsys(int id, HttpServletRequest request){
		String type = request.getParameter("type");
		Product product = new Product();
		product.setId(id);
		if(type.equals("yes")){
			product.setIsys("no");
		}else{
			product.setIsys("yes");
		}
		productDAO.updateIsys(product);
		return "redirect:productList.do";
	}
	

}
