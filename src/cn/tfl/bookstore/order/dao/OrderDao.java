package cn.tfl.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.tfl.bookstore.book.domain.Book;
import cn.tfl.bookstore.order.domain.Order;
import cn.tfl.bookstore.order.domain.OrderItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
  private QueryRunner qr= new TxQueryRunner();
/*
 * 添加订单
 * */
public void addOrder(Order order) {
	try {
		String sql = "insert into orders values(?,?,?,?,?,?)";
		/*
		 * 处理util的date转换成SQL的Timestamp
		 * 
		 * */
		Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
		Object[] params = {order.getOid(),timestamp,order.getTotal(),order.getState(),order.getOwner().getUid()
				,order.getAddress()};
		qr.update(sql,params);
		
	} catch (SQLException e) {
		throw new RuntimeException();
	}
}
////////////////////////插入订单条目///////////////////////
public void addOrderItemList(List<OrderItem> orderItemList) {
	try {
		String sql="insert intp orderitem value(?,?,?,?,?)";
		//插入的是订单条目，相当于一个map，多个一维数组
		//使用它queryRuner的batch（String sql,Object[][]params）
		Object[][] params=new Object[orderItemList.size()][];
		for(int i=0;i<orderItemList.size();i++){
			OrderItem item=orderItemList.get(i);
			params[i]=new Object[]{item.getIid(),item.getCount(),item.getSubtotal(),item.getOrder(),
			                       item.getBook().getBid()};
		}
		qr.batch(sql,params);//批处理
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	
   }
/*
 * 我的订单----即按照uid查询  查询的orderItem 是所有订单条目的集合
 *       ----------所以定义一个指定订单所有条目的方法loadOrderItems(order);--遍历orderItems集合为order对象添加他的所有的订单，
 *       ============生成的是一个map集合，转换成orderList集合，toOrderItemList(List<Map<String, Object>> mapList
 *       ====================所以定义一个方法，将mapList转换为orderlist: toOrderItem(Map<String, Object> map) 
 *       
 * */
//////////////////////按uid查询//////////////////////////////
public List<Order> findByUid(String uid) {
	// TODO Auto-generated method stub
	/*
	 * 通过uid查询出搜有的list《order》
	 * 遍历每一个Order 为其加载他的所有的OrderItem
	 * */
	try {
		/*
		 * 得到当前用户的所有订单
		 * */
		String sql="select * from order where uid=?";
		List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
		for (Order order: orderList){
			loadOrderItems(order);//为order对象添加他的所有的订单
		}
		/*
		 *循环每一个订单为其加载他的所有订单
		 * */
		for(Order order:orderList){
			loadOrderItems(order);//为order用户添加他的所有订单条目
			
		}
	} catch (SQLException e) {
		throw new RuntimeException();
	}
	return null;
}

////////////////加载指定条目下的所有的订单条目///////////////////////////
private void loadOrderItems(Order order) throws SQLException {
   /*
    * 查询两张表 orderItem，book
    * */
	String sql="select * from orderItem i ,book b where i.bid=b.bid where oid=?";
	//一行结果集对应的不再是一个Javabean 所以不再是使用BeanListHandler 而是MaoListHandler
	List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());
	//循环遍历每个map，使用map生成两个对象，OrderItem，Book 然后建立两者的关系，（把book设置给Orderitem）
	List<OrderItem> orderItemList = toOrderItemList(mapList);/////把map转化为一个OrderItem对像
	order.setOrderItemList(orderItemList);

}
/////////////////////////////把map转化为一个OrderItem对像///////////

private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
    List<OrderItem> orderItemList = new ArrayList<OrderItem>();
    for(Map<String,Object>map : mapList){
    	OrderItem item = toOrderItem(map);
    	orderItemList.add(item);
    }
    return orderItemList; 
}
////////////////////////toOrderItem(map)//////////////////
private OrderItem toOrderItem(Map<String, Object> map) {
    OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
    Book book =CommonUtils.toBean(map, Book.class);
    orderItem.setBook(book);
	return orderItem;
}

/**
 * 加载订单
 * @param oid
 * @return
 */
public Order load (String oid) {
	try {
		/*
		 * 1 得到 当前用户的所有订单
		 * */
		String sql="select* from orders where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		/*
		 * 2为order加载它的所有条目
		 * */
		loadOrderItems(order);
		/*
		 * 3 返回订单列表
		 * */
		return order;
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}	
/*
 *通过oid查询订单状态 
 * */ 
public int getStateByOid(String oid){
	try {
		String sql="select state from orders where oid=?";
		//Number num=(Number) qr.query(sql, new ScalarHandler(),oid);
		//return num.intValue();
		return (Integer)qr.query(sql, new ScalarHandler(),oid);
	} catch (SQLException e) {
		throw new RuntimeException();
	}

}


/*
  * 修改订单
  * */
public void updateState(String oid,int state){
	try {
		String sql="update orders set state=? where oid=?";
		qr.update(sql,state,oid);
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
	
}
	

	
}




