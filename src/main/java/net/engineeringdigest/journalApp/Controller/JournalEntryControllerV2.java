package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUsername(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> addJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry= journalEntryService.getJournalEntryById(myId);
            if(journalEntry.isPresent()) {
                return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        boolean removed = journalEntryService.deleteJournalEntryById(myId, userName);
        if(removed) return new ResponseEntity<JournalEntry>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> temp= journalEntryService.getJournalEntryById(myId);
            if(temp.isPresent()) {
                JournalEntry old = temp.get();
                old.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : old.getTitle());
                old.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : old.getContent());
                journalEntryService.updateEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
