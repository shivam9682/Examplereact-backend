package in.sp.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.spring.Entity.ContactMessage;

@Repository
public interface ContactRepository extends JpaRepository<ContactMessage, Long> {



	 long countByIsReadFalse();
}

