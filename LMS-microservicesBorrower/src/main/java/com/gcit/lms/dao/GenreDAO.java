package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Genre;

@Repository
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>>{

	
	public void addGenre(Genre genre) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("insert into tbl_genre (genre_name) values(?)", new Object[]{genre.getGenreName()});
	}
	
	public void updateGenre(Genre genre) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_genre set genre_name = ? where genre_id  = ?", new Object[]{genre.getGenreName(), genre.getGenreId()});
	}
	
	public void deleteGenre(Genre genre) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("delete from tbl_genre where genre_id = ?", new Object[]{genre.getGenreId()});
	}
		
	public void deleteBookGenre(Integer bookId){
		template.update("delete from tbl_book_genres where bookId = ?", new Object[]{bookId});
	}
	
	public List<Genre> readAllGenres() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.query("select * from tbl_genre", this);
	}
	
	public Genre readGenreByID(Integer genreID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		List<Genre> listGenre = (List<Genre>) template.query("select * from tbl_genre where genre_id = ?", new Object[] { genreID }, this);
		if (listGenre != null && !listGenre.isEmpty()) {
			return listGenre.get(0);
		}
		return null;
	}
	
	@Override
	public List<Genre> extractData(ResultSet rs)
			throws SQLException {
		List<Genre> genres = new ArrayList<>();
		while(rs.next()){
			Genre genre = new Genre();
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			genres.add(genre);
		}
		return genres;
	}
	
	public List<Genre> readGenreByBook(Integer bookId){
		return template.query("select * from tbl_genre where genre_id IN (select genre_id from tbl_book_genres where bookId =?)", new Object[]{bookId}, this);
	}

}

