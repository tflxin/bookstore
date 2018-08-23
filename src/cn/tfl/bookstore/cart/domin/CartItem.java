package cn.tfl.bookstore.cart.domin;

import java.math.BigDecimal;

import cn.tfl.bookstore.book.domain.Book;

public class CartItem {
   private int  count;
   private Book book;
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public Book getBook() {
	return book;
}
public void setBook(Book book) {
	this.book = book;
}
   public double getSubtotal(){//小计方法没有成员
	  BigDecimal d1 = new BigDecimal(book.getPrice()+"");
	  BigDecimal d2BigDecimal = new BigDecimal(count+"");
	  return book.getPrice()*count;
   }
}
