package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;;

@Repository
public class BranchDAO extends BaseDAO<LibraryBranch> implements ResultSetExtractor<List<LibraryBranch>>{

	@Autowired
	BookCopiesDAO bCopiesDao;
	
	public void addBranch(LibraryBranch branch) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", 
				new Object[]{branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public void updateBranch(LibraryBranch branch) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?", 
				new Object[]{branch.getBranchName(), branch.getBranchAddress(), branch.getBranchId()});
	}
	
	public void deleteBranch(LibraryBranch branch) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		template.update("delete from tbl_library_branch where branchId = ?", new Object[]{branch.getBranchId()});
	}

	public List<LibraryBranch> readAllBranches(int pageNo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		setPageNo(pageNo);
		return template.query("select * from tbl_library_branch join tbl_book_copies on tbl_library_branch.branchId = tbl_book_copies.branchId where tbl_book_copies.noOfCopies > 0", this);
	}

	public LibraryBranch readBranchByPK(Integer branchId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		List<LibraryBranch> branches =  template.query("select * from tbl_library_branch where branchId = ?", new Object[]{branchId}, this);
		if(!branches.isEmpty()){
			return branches.get(0);
		}
		return null;
	}
	
	@Override
	public List<LibraryBranch> extractData(ResultSet rs)
			throws SQLException {
		List<LibraryBranch> branches = new ArrayList<>();
		while(rs.next()){
			LibraryBranch branch = new LibraryBranch();
			branch.setBranchId(rs.getInt("branchId"));
			branch.setBranchName(rs.getString("branchName"));
			branch.setBranchAddress(rs.getString("branchAddress"));
			Integer copies = bCopiesDao.getCopiesByBranch(branch.getBranchId());
			branch.setBookCopies(copies);
			branches.add(branch);
		}
		return branches;
	}
	
	public List<LibraryBranch> readBranchesByName(String branchName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		branchName = "%"+branchName+"%";
		return template.query("select * from tbl_library_branch where branchName LIKE  ?", new Object[]{branchName}, this);
	}

}

