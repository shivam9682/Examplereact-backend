package in.sp.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.sp.spring.Entity.ContactMessage;
import in.sp.spring.service.ContactService;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<?> saveContact(@RequestBody ContactMessage contactMessage) {

        ContactMessage savedMessage =
                contactService.saveMessage(contactMessage);

        return ResponseEntity.ok(savedMessage);
    }
    @GetMapping("/count") 
    public ResponseEntity<Long> getNotificationCount() {
        long count = contactRepository.count(); 
        return ResponseEntity.ok(count);
    }
}
