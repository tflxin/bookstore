package cn.tfl.bookstore.book.service;

import java.util.List;

import cn.tfl.bookstore.book.dao.BookDao;
import cn.tfl.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAll() {
		return bookDao.findAll();
	}

	/**
	 * 按分类查询图书
	 * @param cid
	 * @return
	 */
	public List<Book> findByCategory(String cid) {
		return bookDao.findByCategory(cid);
	}

	public Book load(String bid) {
		return bookDao.findByBid(bid);
	}

	public void delete(String bid) {
		bookDao.delete(bid);
		
	}

	public void edit(Book book) {
		// TODO Auto-generated method stub
		bookDao.edit(book );
	}

	public void add(Book book) {
		// TODO Auto-generated method stub
		bookDao.add(book);
	}
}
