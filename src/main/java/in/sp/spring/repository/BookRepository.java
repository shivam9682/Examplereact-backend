package in.sp.spring.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.spring.Entity.book;


public interface BookRepository extends JpaRepository<book, Integer> {
	  List<book> findByLibraryBranch(String branchName);
	  List<book> findByQuantityLessThan(int quantity);
	   
}