package com.gcit.lms.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdminService {

	DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
	
	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	BookDAO bookDao;
	
	@Autowired
	BorrowerDAO borrowerDao;
	
	@Autowired
	BookLoansDAO bLoansDao;
	
	@Autowired
	BookCopiesDAO bCopiesDao;
	
	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	GenreDAO genreDao;
	
	
	
// <----------------------------------------------- Author Transactions -------------------------------------------------->	
	@RequestMapping(value = "/author", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	@Transactional
	public void addAuthor(@RequestBody Author author) throws SQLException{//	
	System.out.println(author.getAuthorName());
		try {
			if(author.getAuthorId()!=null){
				authorDao.updateAuthor(author);
			}else{
				authorDao.addAuthor(author);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/author/{authorId}", method = RequestMethod.PUT, consumes="application/json")
	@Transactional
	public void updateAuthor(@PathVariable Integer authorId, @RequestBody Author author) throws SQLException{//	
	System.out.println(author.getAuthorName());
		try {
			if(author.getAuthorName() == null){
				authorDao.deleteAuthor(author);
			}else if(author.getAuthorId() != null){
				authorDao.updateAuthor(author);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/getAuthorByPK/{authorId}", method = RequestMethod.GET, produces="application/json")
	public Author getAuthorByPK(@PathVariable Integer authorId) throws SQLException{
		try {
			return authorDao.readAuthorByPK(authorId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/authors", method = RequestMethod.GET, produces="application/json")
	public List<Author> getAllAuthors() throws SQLException{
		try {
			List<Author> authors = authorDao.readAllAuthors();
			for(Author a : authors){
				a.setBooks(bookDao.readBooksByAuthor(a.getAuthorId()));
			}
			return authors;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/author", method = RequestMethod.GET, produces="application/json")
	public Author initAuthor() throws SQLException{
		return new Author();
	}
	
	@RequestMapping(value = "/initAuthorList", method = RequestMethod.GET, produces="application/json")
	public List<Author> initAuthorList() throws SQLException{
		return new ArrayList<Author>();
	}
	
	@RequestMapping(value = "/author/{authorName}", method = RequestMethod.GET, produces="application/json")
	public List<Author> getAuthorsByName(@PathVariable String authorName) throws SQLException{
		try {
			List<Author> authors = new ArrayList<Author>();
			if(authorName!=null && !authorName.isEmpty()){
				authors = authorDao.readAuthorsByName(authorName);
			}else{
				authors = authorDao.readAllAuthors();
			}
			for(Author a : authors){
				a.setBooks(bookDao.readBooksByAuthor(a.getAuthorId()));
			}
			return authors;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
//	@RequestMapping(value = "/author/{authorId}", method = RequestMethod.DELETE, consumes="application/json")
//	public void deleteAuthor(@PathVariable Integer authorId) throws SQLException {
//		Author author = new Author();
//		try {
//			author.setAuthorId(authorId);
//			authorDao.deleteAuthor(author);
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void deleteBookAuthor(Integer bookId) throws SQLException {
		try {
			authorDao.deleteBookAuthor(bookId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public Integer getAuthorsCount(String searchString) throws SQLException{
		 
		try {
			return authorDao.getCountOfAuthors(searchString);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
// <----------------------------------------------- Author Transactions End ----------------------------------------------->
	
	
// <----------------------------------------------- Book Transactions------------------------------------------------------>
	
	@RequestMapping(value = "/initBook", method = RequestMethod.GET, produces="application/json")
	public Book initBook() throws SQLException{
		return new Book();
	}
	
	@RequestMapping(value = "/initList", method = RequestMethod.GET, produces="application/json")
	public List<Book> initBookList() throws SQLException{
		return new ArrayList<Book>();
	}
	
	@RequestMapping(value = "/deleteBook/{bookId}", method = RequestMethod.GET, produces="application/json")
	public Book deleteBook(@PathVariable Integer bookId) throws SQLException {
		Book book = new Book();
		try {
			book.setBookId(bookId);
			bookDao.deleteBook(book);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return book;
	}
	
	@RequestMapping(value = "/addBook", method = RequestMethod.POST, consumes="application/json")
	@Transactional
	public void addBook(String b) throws SQLException, JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Book book = mapper.readValue(b, Book.class);
		try {
			if(book.getBookId() != null){
				bookDao.updateBook(book);
			}else{
				bookDao.addBook(book);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping(value = "/getBookByPK/{bookId}", method = RequestMethod.GET, produces="application/json")
	public Book getBookByPK(@PathVariable Integer bookId) throws SQLException{
		 
		try {
			return bookDao.readBookByPK(bookId);  
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	@RequestMapping(value = "/saveBook", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
//	@Transactional
//	public void saveBook(@RequestBody Book book) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//		try {
//			if (book.getBookId() != null) {
//				bookDao.updateBook(book);
//			} else {
//				//bdao.addBook(book);
//				Integer bookId = bookDao.addBookWithID(book);
//				
//				if(book.getAuthors()!=null && !book.getAuthors().isEmpty()){
//					for(Author a: book.getAuthors()){
//						bookDao.addBookAuthors(bookId, a.getAuthorId());
//					}
//				}
//				if(book.getGenres()!=null && !book.getGenres().isEmpty()){
//					for(Genre a: book.getGenres()){
//						bookDao.addBookGenres(bookId, a.getGenreId());
//					}
//				}
//				if(book.getPublisher()!=null) {
//					//System.out.println("Pub Id: "+ book.getPublisher().getPublisherId());
//					bookDao.updateBookPublisher(bookId,book.getPublisher().getPublisherId());
//				}
//			}
//			  
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} 
//	}
	
	@RequestMapping(value = "/saveBook/{title}/{a}/{g}/{pId}", method = RequestMethod.GET, produces = "application/json")
	public Book saveBook(@PathVariable String title, @PathVariable Integer[] a, @PathVariable Integer[] g, @PathVariable Integer pId) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Book book = new Book();
		Publisher publisher = new Publisher();
		List<Author> authors = new ArrayList<>();
		List<Genre> genres = new ArrayList<>();
		for(Integer i : a){
			Author author = new Author();
			author.setAuthorId(i);
			authors.add(author);
		}
		
		for(Integer i : g){
			Genre genre = new Genre();
			genre.setGenreId(i);
			genres.add(genre);
		}
		publisher.setPublisherId(pId);
		book.setTitle(title);
		book.setPublisher(publisher);
		book.setAuthors(authors);
		book.setGenres(genres);
		try {
			if (book.getBookId() != null) {
				bookDao.updateBook(book);
			} else {
				//bdao.addBook(book);
				Integer bookId = bookDao.addBookWithID(book);
				
				if(book.getAuthors()!=null && !book.getAuthors().isEmpty()){
					for(Author at: book.getAuthors()){
						bookDao.addBookAuthors(bookId, at.getAuthorId());
					}
				}
				if(book.getGenres()!=null && !book.getGenres().isEmpty()){
					for(Genre gt: book.getGenres()){
						bookDao.addBookGenres(bookId, gt.getGenreId());
					}
				}
				if(book.getPublisher()!=null) {
					//System.out.println("Pub Id: "+ book.getPublisher().getPublisherId());
					bookDao.updateBookPublisher(bookId,book.getPublisher().getPublisherId());
				}
			}
			  
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	@RequestMapping(value = "/viewBooks/{pageNo}", method = RequestMethod.GET, produces="application/json")
	public List<Book> getAllBooks(@PathVariable int pageNo) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{ 
		try {
			List<Book> books =  bookDao.readAllBooks(pageNo);
			for(Book b : books){
				b.setAuthors(authorDao.readAuthorsByBook(b.getBookId()));
				b.setGenres(genreDao.readGenreByBook(b.getBookId()));
				b.setNoCopies(bCopiesDao.getCopiesByBook(b.getBookId()));
			}
			return books;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/searchBooks/{bookName}", method = RequestMethod.GET, produces="application/json")
	public List<Book> getBooksByName(@PathVariable String bookName) throws SQLException{
		try {
			List<Book> books =  bookDao.readBooksByName(bookName);
			for(Book b : books){
				b.setAuthors(authorDao.readAuthorsByBook(b.getBookId()));
				b.setGenres(genreDao.readGenreByBook(b.getBookId()));
				b.setNoCopies(bCopiesDao.getCopiesByBook(b.getBookId()));
			}
			return books;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public Integer getBooksCount() throws SQLException{
		try { 
			return bookDao.getCountOfBooks(); // I was here : go to bookDao //
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
// <----------------------------------------------- Book Transactions End------------------------------------------------------>	
	
	
// <----------------------------------------------- Borrower Transactions------------------------------------------------------>
	
	
	@RequestMapping(value = "/borrower", method = RequestMethod.POST, consumes="application/json")
	public void addBorrower(@RequestBody Borrower borrower) throws SQLException{
		try {
			if(borrower.getCardNo() != null){
				borrowerDao.updateBorrower(borrower);
			}else{
				borrowerDao.addBorrower(borrower);
			}
			  
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}	
	}
	
	@RequestMapping(value = "/borrower/{cardNo}", method = RequestMethod.PUT, consumes="application/json")
	public Borrower updateBorrower(@PathVariable Integer cardNo, @RequestBody Borrower borrower) throws SQLException{
		try {
			if(borrower.getName() == null){
				borrowerDao.deleteBorrower(borrower);
			}else if(borrower.getCardNo() != null){
				borrowerDao.updateBorrower(borrower);
			}
			  
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}	
		return borrower;
	}
	
	public Borrower getBorrowerByPK(Integer cardNo) throws SQLException{
		try {
			return borrowerDao.readBorrowerByPK(cardNo);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/borrowers", method = RequestMethod.GET, produces="application/json")
	public List<Borrower> getAllBorrowers() throws SQLException{
		try {
			return borrowerDao.readAllborrowers();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/borrower/{borrowerName}", method = RequestMethod.GET, produces = "application/json")
	public List<Borrower> getBorrowersByName(String borrowersName) throws SQLException{ 
		try {
			return borrowerDao.readBorrowersByName(borrowersName);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public Integer getBorrowersCount() throws SQLException{
		 
		try {
			return borrowerDao.getCountOfBorrowers();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	
// <----------------------------------------------- Borrower Transactions End------------------------------------------------------>	
	

// <----------------------------------------------- Loan Transactions-------------------------------------------------------------->	
	@RequestMapping(value = "/loan/{days}", method = RequestMethod.PUT, consumes="application/json")
	public void overrideDate(@PathVariable Integer days, @RequestBody BookLoans loans) throws SQLException{
			long millis = System.currentTimeMillis();  
		    Date dateOut = new Date(millis);
		    long ltime = dateOut.getTime() + days*24*60*60*1000;
		    Date dueDate = new Date(ltime);
		    loans.setDueDate(dueDate);
		try {
			if(loans.getDueDate() != null){
				bLoansDao.updateLoansDue(loans);
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping(value = "/loans/{pageNo}", method = RequestMethod.GET, produces="application/json")
	public List<BookLoans> getAllBookLoans(@PathVariable int pageNo) throws SQLException{
		try {
			List<BookLoans> loans = bLoansDao.readAllOverrideLoans(pageNo);
			for(BookLoans l : loans){
				l.setBook(bookDao.readBookByPK(l.getBookId()));
			}
			return loans;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public BookLoans getBooKLoansByPK(DateTime dateOut) throws SQLException{
		try {
			
			return bLoansDao.readBookLoansByPK(dateOut);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}

// <----------------------------------------------- Loan Transactions End------------------------------------------------------>	
	
	
	
// <----------------------------------------------- Publisher Transactions------------------------------------------------------>	
	
	// Add publisher //
	@RequestMapping(value = "/publisher", method = RequestMethod.POST, consumes="application/json")
	@Transactional
	public Publisher addPublisher(@RequestBody Publisher publisher) throws SQLException{ 
		try {
			if(publisher.getPublisherId() != null){
				publisherDao.updatePublisher(publisher);
			}else{
				publisherDao.addPublisher(publisher);
			}
			  
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return publisher;
	}
	
	
	@RequestMapping(value = "/publisher", method = RequestMethod.GET, produces="application/json")
	public Publisher initPublisher() throws SQLException{
		return new Publisher();
	}
	
	public Publisher getPublisherById(Integer pubId) throws SQLException{
		try{
			return publisherDao.readPublisherByID(pubId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/publishers/{pageNo}", method = RequestMethod.GET, produces="application/json")
	public List<Publisher> getAllPublishers(@PathVariable int pageNo) throws SQLException{
		try {
			return publisherDao.readAllPublishers(pageNo);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/publisher/{publisherName}", method = RequestMethod.GET, produces="application/json")
	public List<Publisher> getPublishersByName(@PathVariable String publisherName) throws SQLException{
		List<Publisher> publishers = new ArrayList<>();
		try {
			if(publisherName != null && !publisherName.isEmpty()){
				publishers =  publisherDao.readPublishersByName(publisherName);
			}else{
				publishers = publisherDao.readAllPublishers(1);
			}
			return publishers;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Integer getPublishersCount() throws SQLException{
		try {
			return publisherDao.getCountOfPublishers();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping(value = "/updatePublisher/{publisherId}", method = RequestMethod.PUT, consumes="application/json")
	public void updateOrDeletePublisher(@PathVariable Integer publisherId, @RequestBody Publisher publisher) throws SQLException {
		try {
			if(publisher.getPublisherName() == null){
				publisherDao.deletePublisher(publisher);
			}else if(publisher.getPublisherId() != null){
				publisherDao.updatePublisher(publisher);
			}
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Publisher getPublisherByPK(Integer publisherId) throws SQLException{
		try {
			return publisherDao.readPublisherByPK(publisherId);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
// <----------------------------------------------- Publisher Transactions End------------------------------------------------------>	


// <----------------------------------------------- Genre Transactions-------------------------------------------------------------->
	public Genre getGenreById(Integer genreId) throws SQLException{
		try{
			
			return genreDao.readGenreByID(genreId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/genre", method = RequestMethod.GET, produces = "application/json")
	public Genre initGenre(){
		return new Genre();
	}
	
	@RequestMapping(value = "/initGenreList", method = RequestMethod.GET, produces = "application/json")
	public List<Genre> initGenreList(){
		return new ArrayList<Genre>(0);
	}
	
	@RequestMapping(value = "/genres", method = RequestMethod.GET, produces = "application/json")
	public List<Genre> getAllGenres() throws SQLException{
		try {
			return genreDao.readAllGenres();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
// <-----------------------------------------------Genre Transactions End--------------------------------------------------------->	
	
}
