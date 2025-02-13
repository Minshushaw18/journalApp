package net.engineeringdigest.journalApp.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries")
// will tell spring that the given class is a mapped entity to mongodb collection and the instance of this will be a document
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id // now id will be unique key
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
}
