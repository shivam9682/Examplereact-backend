package in.sp.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import in.sp.spring.Entity.ContactMessage;
import in.sp.spring.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public ContactMessage saveMessage(ContactMessage contactMessage) {
        return contactRepository.save(contactMessage);
    }
    
    @Override
    public long getMessageCount() {
        return contactRepository.count();
    }
    @Override public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }
}
