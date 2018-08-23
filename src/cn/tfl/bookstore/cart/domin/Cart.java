package cn.tfl.bookstore.cart.domin;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
/*
 * 购物车类
 * */
	 private Map<String,CartItem> map = new LinkedHashMap<String,CartItem>();
	 
	 /*
	  * 计算合计
	  * */
	 public double getTotal(){
		 BigDecimal total=new BigDecimal("0");
		 for (CartItem cartItem : map.values()){
	          BigDecimal subtotal = new BigDecimal(""+cartItem.getSubtotal());
	          total=total.add(subtotal);
		 }
		 return total.doubleValue();	 
	 }
	 /*
	  *添加条目倒车中
	  * */
	 public void add(CartItem cartItem){
		 if(map.containsKey(cartItem.getBook().getBid())){//判断原来车子中有么 该条目
			 CartItem _cartItem=map.get(cartItem.getBook().getBid());//返回原来的条目
             _cartItem.setCount(_cartItem.getCount()+cartItem.getCount());//设置数量
             map.put(cartItem.getBook().getBid(), _cartItem);
		 } else {
			 map.put(cartItem.getBook().getBid(),cartItem);
		 }
	 }
	 /*
	  * 清空所有条目
	  * */
	 public void clear(){
		 map.clear();
	 }
	 /*
	  * 删除指定条目
	  * */
	 public void delete(String bid){
		 map.remove(bid);
	 }
       /*
        * 获取所有条目
        * */	 
	 public Collection<CartItem> getCartItems(){
		 
		 return map.values();
	 }
}
