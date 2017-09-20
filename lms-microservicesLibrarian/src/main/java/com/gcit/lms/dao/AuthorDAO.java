package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

@Repository
public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>>{
	
	@Lazy
	@Autowired
	BookDAO bookDao;
	
	public void addAuthor(Author author) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		System.out.println(author.getAuthorName());
		Integer authorId = addAuthorWithId(author);
		List<Book> books = author.getBooks();
		if(books != null){
			for(Book b : books){
				template.update("insert into tbl_book_authors (bookId, authorId) values (?, ?)", new Object[]{b.getBookId(), authorId});
			}
		}
	}
	
	public Integer addAuthorWithId(Author author){
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_author (authorName) values (?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, author.getAuthorName());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}
	
	public void updateAuthor(Author author) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_author set authorName = ? where authorId = ?", new Object[] {author.getAuthorName(), author.getAuthorId()});
		List<Book> books = author.getBooks();
		if(null != books && !books.isEmpty()){
			for(Book b : books){
				template.update("insert into tbl_book_authors (bookId, authorId) values (?, ?)", new Object[]{b.getBookId(), author.getAuthorId()});
			}
		}
	}
	
	public void deleteAuthor(Author author) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		System.out.println(author.getAuthorId());
		template.update("delete from tbl_author where authorId = ?", new Object[]{author.getAuthorId()});
		List<Book> books = author.getBooks();
		if(books != null){
			for(Book b : books){
				deleteBookAuthor(b.getBookId());
			}
		}
	}
	
	public void deleteBookAuthor(Integer bookId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_authors where bookId = ?", new Object[]{bookId});
	}
	
	public List<Author> readAllAuthors() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.query("select distinct * from tbl_author", this);
	}
	
	public Integer getCountOfAuthors(String searchString) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if(searchString!=null && !searchString.isEmpty()){
			searchString = "%"+searchString+"%";
			return template.queryForObject("select count(*) COUNT from tbl_author where authorName like ?", new Object[]{searchString}, Integer.class);
		}else{
			return template.queryForObject("select count(*) COUNT from tbl_author", Integer.class);
		}
	}
	
	public Author readAuthorByPK(Integer authorId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<Author> authors =  template.query("select * from tbl_author where authorId = ?", new Object[]{authorId}, this);
		if(!authors.isEmpty()){
			return authors.get(0);
		}
		return null;
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException  {
		List<Author> authors = new ArrayList<>(); 
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
//			List<Book> books = bookDao.readBooksByAuthor(a.getAuthorId());
//			a.setBooks(books);
			authors.add(a);
		}
		return authors;
	}
	
	

	public List<Author> readAuthorsByName(String authorName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		authorName = "%"+authorName+"%";
		return template.query("select * from tbl_author where authorName LIKE  ?", new Object[]{authorName}, this);
	}
	
	public List<Author> readAuthorsByBook(Integer bookId){
		return template.query("select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId =?)", new Object[]{bookId}, this);
	}

}
