package ee.taltech.debty.controller;

import ee.taltech.debty.entity.Person;
import ee.taltech.debty.service.ContactService;
import ee.taltech.debty.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;
    private final UserService userService;

    public ContactController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @GetMapping("/all/{id}")
    public List<Person> getAllAvailableContacts(@PathVariable("id") Long id) {
        Person person = userService.getUserById(id).orElse(new Person());
        return contactService.getAllAvailableContacts(person);
    }

    @PostMapping("/add/{toId}/{contactId}")
    public void addContact(@PathVariable("toId") Long id, @PathVariable("contactId") Long contactId) {
        contactService.addContact(id, contactId);
    }

    @DeleteMapping("/remove/{fromId}/{contactId}")
    public void removeContact(@PathVariable("fromId") Long fromId, @PathVariable("contactId") Long contactId) {
         contactService.removeContactById(fromId, contactId);
    }

    @GetMapping("/id/{id}")
    public List<Person> getAllUserContacts(@PathVariable("id") Long id) {
        Optional<Person> personOptional = userService.getUserById(id);
        Person person = new Person();
        if (personOptional.isPresent()) person = personOptional.get();
        return contactService.getAllContacts(person);
    }

    @GetMapping("/waiting/{id}")
    public List<Person> getAllWaitingContacts(@PathVariable("id") Long id) {
        return contactService.getIncomingRequests(id);
    }

    @PostMapping("/accept/{toId}/{fromId}")
    public void acceptContact(@PathVariable("toId") Long toId, @PathVariable("fromId") Long fromId) {
        contactService.acceptContactForPersonFromPerson(toId, fromId);
    }
}

