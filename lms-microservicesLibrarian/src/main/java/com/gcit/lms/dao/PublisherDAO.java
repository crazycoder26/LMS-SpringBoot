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
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

@Repository
public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>>{

	@Lazy
	@Autowired
	BookDAO bookDao;
	
	public void addPublisher(Publisher publisher) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		template.update("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values(?, ?, ?)", new Object[]{publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone()});
		Integer pubId = addPublisherWithID(publisher);
		List<Book> books = publisher.getBooks();
		if(!books.isEmpty() && books != null){
			for(Book b : books){
				template.update("update tbl_book set tbl_book.pubId = ? where tbl_book.bookId = ?", new Object[]{pubId,b.getBookId()});
			}
		}
	}

	public Integer addPublisherWithID(Publisher publisher) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values(?, ?, ?)";
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, publisher.getPublisherName());
				ps.setString(2, publisher.getPublisherAddress());
				ps.setString(3, publisher.getPublisherPhone());
				return ps;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void updatePublisher(Publisher publisher) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?", 
				new Object[]{publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId()});
	}
	
	public void deletePublisher(Publisher publisher) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_book set tbl_book.pubId = null where pubId = ?", new Object[]{publisher.getPublisherId()});
		template.update("delete from tbl_publisher where publisherId = ?", new Object[]{publisher.getPublisherId()});
	}
	
	public List<Publisher> readAllPublishers(Integer pageNo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		setPageNo(pageNo);
		return template.query("select * from tbl_publisher", this);
		
	}
	
	public Integer getCountOfPublishers() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.queryForObject("select count(*) COUNT from tbl_publisher", Integer.class);
	}
	
	public Publisher readPublisherByPK(Integer publisherId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<Publisher> publishers =  template.query("select * from tbl_publisher where publisherId = ?", new Object[]{publisherId}, this);
		if(!publishers.isEmpty()){
			return publishers.get(0);
		}
		return null;
	}
	
	public Publisher readPublisherByID(Integer PubId) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		List<Publisher> listPublisher = template.query("select * from tbl_publisher where publisherId = ?", new Object[] { PubId }, this);
		if (listPublisher != null && !listPublisher.isEmpty()) {
			return listPublisher.get(0);
		}
		return null;
	}
	
	
	@Override
	public List<Publisher> extractData(ResultSet rs)
			throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while(rs.next()){
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("publisherId"));
			publisher.setPublisherName(rs.getString("publisherName"));
			publisher.setPublisherAddress(rs.getString("publisherAddress"));
			publisher.setPublisherPhone(rs.getString("publisherPhone"));
			List<Book> books = bookDao.readBooksByPublisher(publisher.getPublisherId());
			publisher.setBooks(books);
			publishers.add(publisher);
		}
		return publishers;
	}

	
	public List<Publisher> readPublishersByName(String publisherName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		publisherName = "%"+publisherName+"%";
		return template.query("select * from tbl_publisher where publisherName LIKE  ?", new Object[]{publisherName}, this);
	}
}