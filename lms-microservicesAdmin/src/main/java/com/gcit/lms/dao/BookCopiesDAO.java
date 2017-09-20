package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;

@Repository
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>>{
	
	
	public void addBookCopies(BookCopies bookCopies) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", new Object[]{bookCopies.getBookId(), bookCopies.getBranchId(), bookCopies.getNoOfCopies()});
	}
	
	public void updateBookCopiesOut(BookCopies bookCopies) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_book_copies set noOfCopies = noOfCopies - 1 where bookId = ? and branchId = ?", 
				new Object[]{bookCopies.getBookId(), bookCopies.getBranchId()});
	}
	
	public void updateBookCopiesIn(BookCopies bookCopies) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_book_copies set noOfCopies = noOfCopies + 1 where bookId = ? and branchId = ?", 
				new Object[]{bookCopies.getBookId(), bookCopies.getBranchId()});
	}
	
	public void updateBookCopies(BookCopies bookCopies) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?", 
				new Object[]{bookCopies.getNoOfCopies(),bookCopies.getBookId(), bookCopies.getBranchId()});
	}
	
	public List<BookCopies> getAllCopiesId(Integer branchId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<BookCopies> bookCopies =  template.query("select * from tbl_book_copies where branchId = ? and noOfCopies > 0", 
										new Object[]{branchId}, this);
		if(!bookCopies.isEmpty()){
			return bookCopies;
		}
		return null;
	}
	
	public BookCopies getAllCopiesbId(Integer bookId, Integer branchId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<BookCopies> bookCopies =  template.query("select * from tbl_book_copies where bookId = ? and branchId = ?", 
										new Object[]{bookId, branchId}, this);
		if(!bookCopies.isEmpty()){
			return bookCopies.get(0);
		}
		return null;
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs)
			throws SQLException {
		List<BookCopies> copiesList = new ArrayList<>();
		while(rs.next()){
			BookCopies bCopies = new BookCopies();
			bCopies.setBookId(rs.getInt("bookId"));
			bCopies.setBranchId(rs.getInt("branchId"));
			bCopies.setNoOfCopies(rs.getInt("noOfCopies"));
			copiesList.add(bCopies);
		}
		return copiesList;
	}
	
	public Integer getCopiesByBranch(Integer branchId){
		List<BookCopies> copies =  template.query("select * from tbl_book_copies where branchId IN (select branchId from tbl_library_branch where branchId = ?)", new Object[]{branchId}, this);
		if(!copies.isEmpty()){
			return copies.get(0).getNoOfCopies();
		}
		return null;
	}
	
	public Integer getCopiesByBook(Integer bookId){
		List<BookCopies> copies =  template.query("select * from tbl_book_copies where bookId IN (select bookId from tbl_book where bookId = ?)", new Object[]{bookId}, this);
		if(!copies.isEmpty()){
			return copies.get(0).getNoOfCopies();
		}
		return null;
	}
	
}

