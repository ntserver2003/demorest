package repositories;

import models.Document;
import models.DocumentSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Component
public class DocumentRepository {

    private DocumentCrud _crud;
    private EntityManager _manager;

    @Autowired
    public DocumentRepository(DocumentCrud documentCrud, EntityManager entityManager) {
        this._crud = documentCrud;
        this._manager = entityManager;
    }

    public Document addDocument(String description) {
        StoredProcedureQuery query = _manager.createStoredProcedureQuery("sp_InsertDocument");
        query.registerStoredProcedureParameter("description", String.class, ParameterMode.IN);
        query.setParameter("description", description);
        query.registerStoredProcedureParameter("id", Long.class, ParameterMode.OUT);
        query.execute();
        Document d = new Document((Long) query.getOutputParameterValue("id")
                , description);
        return d;
    }

    public boolean deleteDocument(Long id) {
        if (_crud.existsById(id)) {
            _crud.deleteById(id);
            return true;
        }
        return false;
    }

    public Document getDocument(Long id) {
        Optional<Document> document = _crud.findById(id);
        return document.orElse(null);
    }

    public List<Document> searchDocuments(String searchType, String searchValue) {
        if (searchType == null || searchType.isEmpty()) {
            throw new RuntimeException("Search type is null or empty");
        }
        switch (searchType.toLowerCase()) {
            case "like":
                searchValue = "%" + searchValue + "%";
                break;
            case "=":
            case ">":
            case "<":
            case "!=":
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        String sql = String.format(DocumentSql.SEARCH, searchType);

        Query query = _manager.createNativeQuery(sql, Document.class);

        query.setParameter("searchValue", searchValue);
        List list = query.getResultList();
        return list;
    }

    public List<Document> listTopDocuments(int top) {
        Query q = _manager.createNamedQuery(DocumentSql.NAMED_SELECT_ALL)
                .setMaxResults(top);
        List d = q.getResultList();
        return (List<Document>) d;
    }
}
