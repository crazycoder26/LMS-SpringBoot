package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;
import com.gcit.lms.service.ConnectionUtil;

@Repository
public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>{
	
	@Autowired
	BookCopiesDAO bCopiesDao;
	
	@Autowired
	GenreDAO gerneDao;
	
	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	GenreDAO genreDao;
	
	public void addBook(Book book) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("insert into tbl_book (bookName) values (?)", new Object[]{book.getTitle()});
	}
	
	public void updateBook(Book book) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_book set title = ? where bookId = ?", new Object[] {book.getTitle(), book.getBookId()});
	}
	
	public void deleteBook(Book book) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		genreDao.deleteBookGenre(book.getBookId());
		authorDao.deleteBookAuthor(book.getBookId());
		template.update("delete from tbl_book where bookId = ?", new Object[]{book.getBookId()});

	}
	

	public List<Book> readAllBooks(int pageNo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		setPageNo(pageNo);
		return template.query("select * from tbl_book", this);
	}

	public List<Book> readAllbooksWithBranch(Integer branchId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		BookCopies bc = new BookCopies();
		List<BookCopies> copies  = bCopiesDao.getAllCopiesId(branchId);
		 List<Book> books = new ArrayList<>();
		 for(BookCopies bcs :  copies){
			 List<Book> b = template.query("select * from tbl_book where bookId = ?", new Object[]{bcs.getBookId()}, this);
			 if(!b.isEmpty()){
				 books.add(b.get(0));
			 }
		 }
		return books;

	}
	
	

	public Integer getCountOfBooks() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.queryForObject("select count(*) COUNT from tbl_book", Integer.class);
	}
	
	// Came from admin service getbookBYPk method go back there//
	public Book readBookByPK(Integer bookId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<Book> books =  template.query("select * from tbl_book where bookId = ?", new Object[]{bookId}, this);
		if(!books.isEmpty()){
			return books.get(0);
		}
		return null;
	}
	

	
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while(rs.next()){
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			Publisher pub = new Publisher();
			pub.setPublisherId(rs.getInt("pubId"));
			b.setPublisher(pub);
			books.add(b);
		}
		return books;
	}
	
	
	
	public Integer addBookWithID(Book book) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
//		return saveWithID("insert into tbl_book(title, pubId) values (?, ?)", new Object[] {book.getTitle(), book.getPublisher().getPublisherId()});
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_book(title, pubId) values (?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				ps.setInt(2, book.getPublisher().getPublisherId());
				return ps;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void addBookAuthors(Integer bookId, Integer authorId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		template.update("insert into tbl_book_authors values (?, ?)", new Object[] {bookId, authorId});
	}

	public void addBookGenres(Integer bookId, Integer genreId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		template.update("insert into tbl_book_genres values (?, ?)", new Object[] {genreId,bookId});
		
	}

	public void updateBookPublisher(Integer bookId, Integer publisherId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		template.update("update tbl_book set pubId = ? where bookId = ?", new Object[]{publisherId,bookId});
	}
	
	public List<Book> readBooksByName(String bookName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		bookName = "%"+bookName+"%";
		return template.query("select * from tbl_book where title LIKE  ?", new Object[]{bookName}, this);
	}
	
	public List<Book> readBooksByAuthor(Integer authorId){
		return template.query("select * from tbl_book where bookId IN (select bookId from tbl_book_authors where authorId = ?)", new Object[]{authorId}, this);
	}
	
	public List<Book> readBooksByPublisher(Integer publisherId){
		return template.query("select * from tbl_book where bookId IN (select bookId from tbl_publisher where publisherId = ?)", new Object[]{publisherId}, this);
	}
	
}
