package repositories;

import models.Document;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DocumentRepository {

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
            List list = query.list();
            return (List<Document>) list;
        }
        finally {
        session.close();
        }
    }
}
