package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;

@Repository
public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>>{
	
	
	public void addBorrower(Borrower borrower) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("insert into tbl_borrower (name, address, phone) values (?, ?, ?)", new Object[]{borrower.getName(), borrower.getAddress(), borrower.getPhone()});
	}
	
	public void updateBorrower(Borrower borrower) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ? ", 
				new Object[] {borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo()});
	}
	
	public void deleteBorrower(Borrower borrower) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("delete from tbl_borrower where cardNo = ?", new Object[]{borrower.getCardNo()});
	}

	public List<Borrower> readAllborrowers() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.query("select * from tbl_borrower", this);
	}
	
	public Integer getCountOfBorrowers() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return template.queryForObject("select count(*) COUNT from tbl_borrower", Integer.class);
	}
	
	public Borrower readBorrowerByPK(Integer cardNo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<Borrower> borrowers =  template.query("select * from tbl_borrower where cardNo = ?", new Object[]{cardNo}, this);
		if(!borrowers.isEmpty()){
			return borrowers.get(0);
		}
		return null;
	}
	
	@Override
	public List<Borrower> extractData(ResultSet rs)
			throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while(rs.next()){
			Borrower b = new Borrower();
			b.setCardNo(rs.getInt("cardNo"));
			b.setName(rs.getString("name"));
			b.setAddress(rs.getString("address"));
			b.setPhone(rs.getString("phone"));
			borrowers.add(b);
		}
		return borrowers;
		
	}

	
	
	public List<Borrower> readBorrowersByName(String borrowerName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		borrowerName = "%"+borrowerName+"%";
		return template.query("select * from tbl_borrower where name LIKE  ?", new Object[]{borrowerName}, this);
	}
	
	
}