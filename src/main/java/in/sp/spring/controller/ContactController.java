package in.sp.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.sp.spring.Entity.ContactMessage;
import in.sp.spring.repository.ContactRepository;
import in.sp.spring.service.ContactService;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    // Save Contact Form
    @PostMapping
    public ResponseEntity<?> saveContact(
            @RequestBody ContactMessage contactMessage) {

        ContactMessage savedMessage =
                contactService.saveMessage(contactMessage);

        return ResponseEntity.ok(savedMessage);
    }

    // Get All Messages
    @GetMapping
    public ResponseEntity<?> getAllMessages() {

        try {
            List<ContactMessage> messages =
                    contactRepository.findAll();

            return ResponseEntity.ok(messages);

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    // Unread Notification Count
    @GetMapping("/count")
    public ResponseEntity<Long> getNotificationCount() {

        long count = contactRepository.countByIsReadFalse();

        return ResponseEntity.ok(count);
    }

    // Mark All Notifications Read
    @PutMapping("/read-all")
    public ResponseEntity<String> markAllRead() {

        List<ContactMessage> messages =
                contactRepository.findAll();

        for (ContactMessage msg : messages) {
           msg.setIsRead(true);
        }

        contactRepository.saveAll(messages);

        return ResponseEntity.ok(
                "All notifications marked as read");
    }
}
