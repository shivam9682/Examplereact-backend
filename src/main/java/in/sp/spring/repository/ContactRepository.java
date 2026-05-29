package in.sp.spring.repository;

<<<<<<< HEAD
=======



>>>>>>> 098ecfa (Fixed List imports)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.spring.Entity.ContactMessage;

@Repository
public interface ContactRepository extends JpaRepository<ContactMessage, Long> {
<<<<<<< HEAD

}
=======
	 long countByIsReadFalse();
}
>>>>>>> 098ecfa (Fixed List imports)
