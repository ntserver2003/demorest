package controllers.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.ServiceResponse;

import static util.ServiceInfo.SERVICE_ROOT;

import java.util.List;


@RestController
@RequestMapping(produces = "application/json")
public class DocumentController {
    private repositories.DocumentRepository documentRepository;

    @Autowired
    public DocumentController(repositories.DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @RequestMapping(value = SERVICE_ROOT + "/documents/new", method = RequestMethod.GET)
    public ResponseEntity newDocument(@RequestParam(value = "description", required = false) String description) throws Exception {
        ServiceResponse<Document> response = new ServiceResponse<>();
        response.setResult(documentRepository.addDocument(description));
        return new ResponseEntity<>(response, response.getHttpStatus());

    }

    @RequestMapping(value = SERVICE_ROOT + "/documents/{id:[\\d]+}", method = RequestMethod.GET)
    public ResponseEntity getDocument(@PathVariable("id") Long id) {
        ServiceResponse<Document> response = new ServiceResponse<>();
        Document document = documentRepository.getDocument(id);
        if(document != null){
            response.setResult(document);
        } else {
            response.setError(String.format("Document id=%d not found", id));
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @RequestMapping(value = SERVICE_ROOT + "/documents/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity deleteDocument(@PathVariable("id") Long id) throws Exception {
        ServiceResponse<String> response = new ServiceResponse<>();
        if (documentRepository.deleteDocument(id)) {
            response.setResult("OK");
            return new ResponseEntity<>(response, response.getHttpStatus());
        }
        response.setError(new IllegalArgumentException(
                String.format("Document id=%d not found", id)
        ));
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @RequestMapping(value = SERVICE_ROOT + "/documents/search/{searchType}", method = RequestMethod.GET)
    public ResponseEntity searchDocuments(@PathVariable("searchType") String searchType
            , @RequestParam(value = "value") String searchValue) {
        ServiceResponse<List<Document>> response = new ServiceResponse<>();
        List l = documentRepository.searchDocuments(searchType, searchValue);
        if (l.isEmpty()) {
            response.setError("No documents found");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        } else {
            response.setResult(l);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @RequestMapping(value = SERVICE_ROOT + "/documents/top", method = RequestMethod.GET)
    public ResponseEntity topNDocuments(@RequestParam(value = "n", required = false) String top) throws JsonProcessingException {
        int topN;
        ServiceResponse<List<Document>> response = new ServiceResponse<>();
        if (top == null) {
            topN = 5;
        } else {
            topN = Integer.decode(top);
        }
        List<Document> l = documentRepository.listTopDocuments(topN);
        if (l == null || l.isEmpty()) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        } else {
            response.setResult(l);
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
