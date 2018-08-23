package cn.tfl.bookstore.book.web.servlet.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tfl.bookstore.book.domain.Book;
import cn.tfl.bookstore.book.service.BookService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.tfl.bookstore.category.domain.Category;
import cn.tfl.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {
    
	 private BookService bookService = new BookService();
	 private CategoryService categoryService = new CategoryService();
	 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		   request.setCharacterEncoding("UTF-8");
		   response.setContentType("text/html;chyarset=utf-8");
		   
		   /*
		    * 1 把表单数据封装到Book对象中
		    * *上传三步 
		    * */
		   //创建工厂
		   DiskFileItemFactory factory = new DiskFileItemFactory(15*1024, new File("F:/f/temp"));
		   //得到解析器
		   ServletFileUpload sfu = new ServletFileUpload(factory);
		   //设置单个文件打下
		   sfu.setFileSizeMax(20*1024);
		   //使用解析器去解析request对象，得到	list<FileItem>
		   
		   try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			/*
			 * 把fileItemList中的数据封装到Book对象中
			 *   在所有普通字段的变单字段数据先封装到Map中
			 *     再把map中的数据封装到book中
			 * */
			Map<String,String> map = new HashMap<String,String>();
			for (FileItem fileItem: fileItemList){
				if(fileItem.isFormField()){
					map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				}
			}
			
			Book book = CommonUtils.toBean(map, Book.class);
			//为book指定bid
			book.setAuthor(CommonUtils.uuid());
			/*
			 * 需要把map中的cid封装到category对象中，再把category 赋值给Book
			 * */
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			/*
			 * 保存上传的文件，保存目录和文件名称
			 * */	
			String savepath = this.getServletContext().getRealPath("book_img");
			String filename = CommonUtils.uuid()+"_"+fileItemList.get(1).getName();
			
		   /*校验扩展名*/
			if(!filename.toLowerCase().endsWith("jpg")){
				request.setAttribute("msg", "图片的扩展名不对");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add,jsp").forward(request, response);
				/**getRequestDispatcher是服务器内部跳转，地址栏信息不变，只能跳转到web应用内的网页。 
				sendRedirect是页面重定向，地址栏信息改变，可以跳转到任意网页。 
				这两条网上都查得到，但看着比较乱，现提供我测试的结果（仅供参考）： 
				1.getRequestDispatcher分成两种，可以用request调用，也可以用getServletContext()调用 
				   不同的是request.getRequestDispatcher(url)的url可以是相对路径也可以是绝对路径。 
				   而this.getServletContext().getRequestDispatcher(url)的url只能是绝对路径。 
				*/
				return ;
			}
		   /*
		    * 使用目录和文件名创建目标文件
		    * */
			File destFile = new File(savepath,filename);
				//保存上传文件到目标位置
			   	fileItemList.get(1).write(destFile);
			/*
			 * 3 设置Book对象的image，即把图片路径设置给book的image
			 * */
			   	book.setImage("book_image"+filename);
		   /*
		    * 使用Bbookservice完成保存
		    * */
			bookService.ad                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    d(book);
			   
			   
		} catch (Exception e) {
			// TODO: handle exception
		}
		   
		   
		   
		   
		   
	}
	 

}
