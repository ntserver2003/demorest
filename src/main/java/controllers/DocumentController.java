package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.DocumentRepository;
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

    @RequestMapping(value = SERVICE_ROOT+"/documents/new", method = RequestMethod.GET)
    public String newDocument(@RequestParam(value = "description") String description) throws Exception {
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

    @RequestMapping(value = SERVICE_ROOT+"/documents/{id}", method = RequestMethod.GET)
    public ServiceResponse getDocument(@PathVariable("id") Long id) {
        ServiceResponse response;
        try {
            response = new ServiceResponse<>(DocumentRepository.getDocument(id));
        } catch (Exception e) {
            response = new ServiceResponse().SetError(e);
        }
        return response;
    }

    @RequestMapping(value = SERVICE_ROOT+"/documents/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity deleteDocument(@PathVariable("id") Long id) throws Exception {
        if (documentRepository.deleteDocument(id)) {
            return new ResponseEntity<>(new ServiceResponse<>("OK"), HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ServiceResponse().SetError(
                        new IllegalArgumentException(
                                String.format("Document id=%d not found", id)
                        )), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = SERVICE_ROOT+"/documents/search/{searchType}", method = RequestMethod.GET)
    public ResponseEntity searchDocuments(@PathVariable("searchType") String searchType
            , @RequestParam(value = "value") String searchValue) {
        HttpStatus status;
        ServiceResponse<List<Document>> response = new ServiceResponse<>();
        try {
            List l = documentRepository.searchDocuments(searchType, searchValue);
            if (l.isEmpty()) {
                response.SetError("No documents found");
                status = HttpStatus.NOT_FOUND;
            }else
            {
                response.set_result(l);
                status = HttpStatus.OK;
            }
        } catch (Exception e)
        {
            response.SetError(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    @RequestMapping(value = SERVICE_ROOT+"/documents/top", method = RequestMethod.GET)
    public ResponseEntity topNDocuments(@RequestParam(value = "n", required = false) String top) throws JsonProcessingException {
        int topN;
        HttpStatus status;
        ServiceResponse<List<Document>> serviceResponse;
        try {
            if (top == null) {
                topN = 5;
            } else {
                topN = Integer.decode(top);
            }
            List<Document> l = documentRepository.listTopDocuments(topN);
            if (l == null || l.isEmpty()) {
                status = HttpStatus.NOT_FOUND;
            } else {
                status = HttpStatus.OK;
            }
            serviceResponse = new ServiceResponse<>(l);
        } catch (Exception e) {
            serviceResponse = new ServiceResponse<>();
            serviceResponse.SetError(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(serviceResponse, status);
    }
}
