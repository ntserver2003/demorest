package repositories;

import models.Document;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.print.Doc;
import java.util.List;

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

    public static Document addDocument(String description) {
        Session session = main.Application.getFactory().openSession();
        Transaction tx = null;
        Long docId = null;
        try {
            Document d = new Document();
            d.setDescription(description);
            tx = session.beginTransaction();
            docId = (Long) session.save(d);
            tx.commit();
            return d;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return null;
        } finally {
            session.close();
        }

    }

    public boolean deleteDocument(Long id) {
        if (_crud.existsById(id)) {
            _crud.deleteById(id);
            return true;
        }
        return false;
    }

    public static Document getDocument(Long id) {
        Session session = main.Application.getFactory().openSession();
        try {
            Document d = session.get(Document.class, id);
            return d;
        } finally {
            session.close();
        }
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

        if (searchType.compareToIgnoreCase("like") == 0) {
            searchValue = "%" + searchValue + "%";
        }
        String sql = String.format("select * from dbo.Documents d where d.description %s :searchValue"
                , searchType);

        Query query = _manager.createNativeQuery(sql);

        query.setParameter("searchValue", searchValue);
        List list = query.getResultList();
        return (List<Document>) list;
    }

    public List<Document> listTopDocuments(int top) {
        Query q = _manager.createNamedQuery("Documents.SelectAll")
                .setMaxResults(top);
        List d = q.getResultList();
        return (List<Document>) d;
    }
}
