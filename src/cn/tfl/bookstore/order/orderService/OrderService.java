package cn.tfl.bookstore.order.orderService;

import java.sql.SQLException;
import java.util.List;

import cn.tfl.bookstore.order.dao.OrderDao;
import cn.tfl.bookstore.order.domain.Order;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
  OrderDao orderDao = new OrderDao();

  
  
  /*
 * 添加订单
 * */
public void add(Order order) {
	// TODO Auto-generated method stub
      try {
		//开启事物
    	  JdbcUtils.beginTransaction();
    	  //插入订单
    	  orderDao.addOrder(order);
    	  //插入订单条目
    	  orderDao.addOrderItemList(order.getOrderItemList());
    	  //提交事物
    	  JdbcUtils.commitTransaction();
	} catch (Exception e) {
		try {
			JdbcUtils.rollbackTransaction();
		} catch (SQLException e2) {
			throw new RuntimeException();
		}
	}	
}

public List<Order> myOrder(String uid){
	return orderDao.findByUid(uid);
 }
/*
 加载订单
 * */

   public Order load(String oid) {
	    return orderDao.load(oid);
   }
 /*
 * 确认订单
 * */
  public void confirm(String oid) throws OrderException{
     /*1校验订单*/
 int state = orderDao.getStateByOid(oid);//获取订单状态
 if(state!=3) throw new OrderException("订单确认失败");
 orderDao.updateState(oid, 4); 

}
  /*
   * 支付方式
   * */
  public void zhiFu(String oid){
	  int state = orderDao.getStateByOid(oid);
	  if(state == 1){
		  orderDao.updateState(oid, 2);
	  }
	  
  }
  
  
  }

