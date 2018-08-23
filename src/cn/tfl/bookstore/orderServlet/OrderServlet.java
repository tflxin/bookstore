package cn.tfl.bookstore.orderServlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tfl.bookstore.cart.domin.Cart;
import cn.tfl.bookstore.cart.domin.CartItem;
import cn.tfl.bookstore.order.domain.Order;
import cn.tfl.bookstore.order.domain.OrderItem;
import cn.tfl.bookstore.order.orderService.OrderException;
import cn.tfl.bookstore.order.orderService.OrderService;
import cn.tfl.bookstore.order.web.servlet.PaymentUtil;
import cn.tfl.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
    OrderService orderService = new OrderService();
 /*
  *添加订单
  *把session中的车用来生成对象
  * */
    public String add(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	/*
    	 * 1cong session 中得到cart对象，
    	 *
    	 * 保存order到request域中，转发到/jsps/order/desc.jsp
    	 * */
    	Cart cart = (Cart) request.getSession().getAttribute("cart");
    	/////////////////////////////////////////////////////////
    	//使用对象cart生成order对象
    	/*
    	 * 把cart---------order，先创建一个order对象，并设置其属性

    	 * 设置编号，设置下单时间、设置订单状态1未付款，得到下单人，设置订单所有者，设置订单合计，从cart中获取
    	 * */
    	Order order = new Order();
    	order.setOid(CommonUtils.uuid());
    	order.setOrdertime(new Date());
    	order.setState(1);
    	
    	User user = (User) request.getSession().getAttribute("session_user");
    	order.setOwner(user);
        order.setTotal(cart.getTotal());  
    	/////////////////////////////////////////////////////////////
        /*
    	 * 创建订单条目
    	 * 把cartItemList-------------------orderItemList
    	 * */
        //新建一个订单条目对象
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        //循环遍历Cart中的所有的CartItem对象，使用每一个CartItem创建OrderItem对象，并添加到集合中
        for(CartItem cartItem : cart.getCartItems()){
              OrderItem oi = new OrderItem();
              
              oi.setIid(CommonUtils.uuid());
              oi.setCount(cartItem.getCount());
              oi.setBook(cartItem.getBook());
              oi.setSubtotal(cartItem.getSubtotal());
              oi.setOrder(order);
              //把订单条目添加到集合中
              orderItemList.add(oi);
        }     
         //把所有的订单条目添加到订单中
        order.setOrderItemList(orderItemList);
        //清空购物车
        cart.clear();
        ///////////////////  调用service方法添加订单///////////////////////////
        orderService.add(order);
        //////////////////////////保存order到request域中//////////////////////////
        request.setAttribute("order", order);
        return "/jsps/order/desc.jsp";    
    }
    /*
     * 我的订单（其实就是按照uid查询）
     * */
   
    public String myOrders(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	/*
    	 * 从session中得到当前用户，在获取其uid
    	 * 使用uid调用orderService#myOrders（Use。getUid（0）
    	 * 把订单表保存在request域中，转发到/jsps/order/list.jsp
    	 * */
    	User user=(User) request.getSession().getAttribute("session_user");
    	List<Order> orderList = orderService.myOrder(user.getUid());
    	request.setAttribute("orderList", orderList);
        return "/jsps/order/desc.jsp";    
    }
    
    /**
	 * 加载订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 得到oid参数 2. 使用oid调用service方法得到Order 3.
		 * 保存到request域，转发到/jsps/order/desc.jsp
		 */
		request.setAttribute("order",orderService.load(request.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 确认收货
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取oid参数 2. 调用service方法 > 如果有异常，保存异常信息，转发到msg.jsp 3.
		 * 保存成功信息，转发到msg.jsp
		 */
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "交易成功！");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}   
	
	/*
	 *支付去银行 
	 * */
	public String zhifu(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		Properties props = new Properties();
		InputStream input= this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(input);
		
		/*
		 * 准备13+1参数
		 * */
		/*
		 * 准备13参数
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";

		/*
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);

		/*
		 * 连接易宝的网址和13+1个参数
		 */
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		System.out.println(url);

		/*
		 * 重定向到易宝
		 */
		response.sendRedirect(url.toString());
        
		return null;
	}

	/**
	 * 这个方法是易宝回调方法 我们必须要判断调用本方法的是不是易宝！
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 获取11 + 1
		 */
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");

		String hmac = request.getParameter("hmac");

		/*
		 * 2. 校验访问者是否为易宝！
		 */
		Properties props  = new Properties();
		InputStream input=this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(input);

		
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if(!bool){//如果校验失败
			request.setAttribute("msg", "校验失败");
			return "f:/jsps/msg";
		}
	
		
		/*
		 * 3. 获取状态订单，确定是否要修改订单状态，以及添加积分等业务操作
		 */
		orderService.zhiFu(r6_Order);//有可能对数据库进行操作，也可能不操作！
		
		/*
		 * 4. 判断当前回调方式
		 *   如果为点对点，需要回馈以success开头的字符串
		 */
		if(r9_BType.equals("2")) {
			response.getWriter().print("success");																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																														
		}
		
		/*
		 * 5. 保存成功信息，转发到msg.jsp
		 */
		request.setAttribute("msg", "支付成功！等待卖家发货！你慢慢等~");
		return "f:/jsps/msg.jsp";
	}
									
		
	}
	
