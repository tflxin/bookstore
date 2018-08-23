package cn.tfl.bookstore.category.service;

import java.util.List;

import cn.tfl.bookstore.category.dao.CategoryDao;
import cn.tfl.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();

	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		return categoryDao.findAll();
	} 
}