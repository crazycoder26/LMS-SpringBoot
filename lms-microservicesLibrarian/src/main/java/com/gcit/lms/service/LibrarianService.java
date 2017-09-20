package com.gcit.lms.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;


@RestController
public class LibrarianService {
	
	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	BookDAO bookDao;
	
	@Autowired
	BorrowerDAO borrowerDao;
	
	@Autowired
	BookCopiesDAO bCopiesDao;
	
	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	GenreDAO genreDao;
	
	@CrossOrigin
	@RequestMapping(value = "/branches/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public List<LibraryBranch> getAllBranches(@PathVariable int pageNo) throws SQLException{
		System.out.println("Inside service");
		try {
			return branchDao.readAllBranches(pageNo);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public BookCopies getBookCopiesById(Integer bookId, Integer branchId) throws SQLException{
		try {
			return bCopiesDao.getAllCopiesbId(bookId,branchId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public String getBorrowerName(Integer cardNo) throws SQLException{
		try {
			Borrower borrower = borrowerDao.readBorrowerByPK(cardNo);
			if(borrower != null){
				return borrower.getName();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/branch", method = RequestMethod.GET, produces="application/json")
	public LibraryBranch initBranch() throws SQLException{
		return new LibraryBranch();
	}
	
	@RequestMapping(value = "/initLoans", method = RequestMethod.GET, produces="application/json")
	public BookLoans initLoans() throws SQLException{
		return new BookLoans();
	}
	
	@RequestMapping(value = "/viewBranchBooks/{branchId}", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getAllBooksWithBranch( @PathVariable Integer branchId) throws SQLException{
		System.out.println(branchId);
		try {
			List<Book> books = bookDao.readAllbooksWithBranch(branchId);
			for(Book b : books){
				b.setAuthors(authorDao.readAuthorsByBook(b.getBookId()));
				b.setGenres(genreDao.readGenreByBook(b.getBookId()));
				b.setNoCopies(bCopiesDao.getCopiesByBook(b.getBookId()));
			}
			return books;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public LibraryBranch getBranchPK(Integer branchId) throws SQLException{
		try {
			return branchDao.readBranchByPK(branchId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	@RequestMapping(value = "/addOrUpdateBranch", method = RequestMethod.POST, consumes = "application/json")
	@Transactional
	public void addBranch(@RequestBody LibraryBranch branch) throws SQLException{
		try {
			if(branch.getBranchId()!=null){
				branchDao.updateBranch(branch);
			}else{
				branchDao.addBranch(branch);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/branch", method = RequestMethod.PUT, consumes = "application/json")
	public void updateBranch(@RequestBody LibraryBranch branch) throws SQLException{
		try {
			if(branch.getBranchId() != null){
				branchDao.updateBranch(branch);
			}else{
				branchDao.addBranch(branch);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/bookCopies", method = RequestMethod.PUT, consumes = "application/json")
	public void updateCopies(@RequestBody BookCopies copies) throws SQLException{
		try {
			if(copies.getBookId() != null){
				bCopiesDao.updateBookCopies(copies);
			}else{
				bCopiesDao.addBookCopies(copies);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping(value = "/branch/{branchName}", method = RequestMethod.GET, produces = "application/json")
	public List<LibraryBranch> getBranchesByName(@PathVariable String branchName) throws SQLException{ 
		try {
			List<LibraryBranch> branches = new ArrayList<>();
			if(branchName != null && !branchName.isEmpty()){
				branches = branchDao.readBranchesByName(branchName);
			}else{
				branches = branchDao.readAllBranches(1);
			}
			return branches;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
}
