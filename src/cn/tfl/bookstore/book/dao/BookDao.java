package cn.tfl.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;

import cn.tfl.bookstore.book.domain.Book;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAll(){
		try {
			String sql = "select * from book";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按分类查询
	 * @param cid
	 * @return
	 */
	public List<Book> findByCategory(String cid){
		try {
			String sql = "select *from book where cid=?";
			return qr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载方法
	 * @param bid
	 * @return
	 */
	public Book findByBid(String bid) {
		try {
			String sql = "select * from book where bid=?";
			return qr.query(sql, new BeanHandler<Book>(Book.class), bid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
/*
 * 删除图书
 * */
	public void delete(String bid) {
		// TODO Auto-generated method stub
		try {
			String  sql = "update book set del=true where bid=?";
			qr.update(sql,bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void edit(Book book) {
		try {
			String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
			Object[] params = {book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),
					book.getCategory().getCid(),book.getBid()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void add(Book book) {
		// TODO Auto-generated method stub
		try {
			String sql="insert into book values(?,?,?,?,?)";
			Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor()
					,book.getImage(),book.getCategory().getCid()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
