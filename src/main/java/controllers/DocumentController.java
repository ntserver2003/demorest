package controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Document;
import org.springframework.web.bind.annotation.*;
import repositories.DocumentRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DocumentController {
    private final AtomicLong _id = new AtomicLong();
    @RequestMapping(value = "/documents/new", method = RequestMethod.GET, produces = "application/json")
    public String newDocument(@RequestParam(value = "description") String description) throws Exception
    {
        /*Document d  =new Document(_id.incrementAndGet(), description);
        ObjectMapper mapper = new ObjectMapper();
        List<String> ls = new ArrayList();
        ls.add(Long.toString(d.getId()) + ":" + d.getDescription());
        //return mapper.writeValueAsString(ls);
        return mapper.writeValueAsString(d);
       // return Long.toString(d.getId()) + ":" + d.getDescription();
        */
        ObjectMapper mapper = new ObjectMapper();
        Document d = DocumentRepository.addDocument(description);
        return mapper.writeValueAsString(d);
    }
    @RequestMapping(value = "/documents/{id}", method = RequestMethod.GET, produces = "application/json")
    public Document getDocument(@PathVariable("id") Long id)
    {
        return DocumentRepository.getDocument(id);
    }

    @RequestMapping(value = "/documents/search/{searchType}", method = RequestMethod.GET, produces = "application/json")
    public List<Document> searchDocuments(@PathVariable("searchType") String searchType
            ,@RequestParam(value = "value") String searchValue)
    {
        return DocumentRepository.searchDocuments(searchType, searchValue);
    }
}
