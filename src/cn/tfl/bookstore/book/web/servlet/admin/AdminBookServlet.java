package cn.tfl.bookstore.book.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tfl.bookstore.book.domain.Book;
import cn.tfl.bookstore.book.service.BookService;
import cn.tfl.bookstore.category.domain.Category;
import cn.tfl.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
  private CategoryService categoryService = new CategoryService();//图书管理---我的图书订单
  private BookService bookService = new BookService();
 /*
  * 添加图书
  * @param request
  * @param response
  * @return 
  * @throws ServletException
  * @throw ServletException
  * */
	public String addPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	/*
	 * 查询所有分类。保存到request域，转发到add.jsp
	 * add.jsp把所有的分类使用下拉列表显示在表单中
	 * */
		request.setAttribute("categoryList",categoryService.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	//////////////////获取我的订单中的book对象，以及全部的图书分类///////////////////////////
	
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取参数bid，通过bid调用service方法得到Book对象
		 * 获取所有的分类，保存到request域中
		 * 保存book到request域中。转发到desc.jsp
		 * */
		request.setAttribute("book", bookService.load(request.getParameter("bid")));//获取book对象
		request.setAttribute("categoryList", categoryService.findAll());//获取所有的分类
		return "f:/adminjsps/admin/book/desc.jsp";
	}	
	/*
	 * 查看所有的图书（我的订单中的所有的图书）
	 * */	
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    //获取所有的图书，并保存在request域中
		request.setAttribute("bookList", bookService.findAll()); 
		//并返回到图书列表
		return "f:/adminjsps/admin/book/list.jsp";
	}
		
	/*
	 * 删除图书
	 * */	
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		bookService.delete("bid");
		return findAll(request,response);	
	}

	
	/*
	 * 修改图书
	 * */	
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book=CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(category);
		
		bookService.edit(book);
		return findAll(request,response);
	}
		
		
		
		
		
		
		
		

}
