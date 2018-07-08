package repositories;

import models.Document;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@EnableJpaRepositories
@Component
public class DocumentRepository {

    @Autowired
    private DocumentCrud documentCrud;
    @Autowired
    private EntityManager entityManager;

    public static Document addDocument(String description )
    {
        Session session = main.Application.getFactory().openSession();
        Transaction tx = null;
        Long docId = null;
        try
        {
            Document d = new Document();
            d.setDescription(description);
            tx = session.beginTransaction();
            docId = (Long) session.save(d);
            tx.commit();
            return d;
        }catch (Exception e){
            if( tx != null) tx.rollback();
            return null;
        }finally {
            session.close();
        }

    }
    public boolean deleteDocument(Long id)
    {
        if(documentCrud.existsById(id)) {
            documentCrud.deleteById(id);
            return true;
        }
        return false;
    }
    public static Document getDocument(Long id)
    {
        Session session = main.Application.getFactory().openSession();
        try {
            Document d = session.get(Document.class, id);
            return d;
        } catch (Exception e)
        {
            return null;
        }
        finally {
            session.close();
        }
    }

    public static List<Document> searchDocuments(String searchType, String searchValue)
    {
        Session session = main.Application.getFactory().openSession();
        searchValue = "%"+searchValue+"%";
        Query query = session.createQuery("from Document where description like :searchValue");

        try {
            query.setParameter("searchValue", searchValue);
            List list = query.getResultList();
            return (List<Document>) list;
        }
        finally {
        session.close();
        }
    }

    public List<Document> listTopDocuments(int top)
    {
        Query q = entityManager.createNamedQuery("selectDocuments")
                .setMaxResults(top);
        List d = q.getResultList();
        return (List<Document>)d;
    }
}
