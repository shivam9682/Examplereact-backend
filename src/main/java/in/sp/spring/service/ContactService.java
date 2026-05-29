package in.sp.spring.service;

import in.sp.spring.Entity.ContactMessage;
import java.util.List;
public interface ContactService {

    ContactMessage saveMessage(ContactMessage contactMessage);
    long getMessageCount();
    List<ContactMessage> getAllMessages();
}
