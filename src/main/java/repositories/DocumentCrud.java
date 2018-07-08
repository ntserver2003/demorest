package repositories;
import models.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentCrud extends CrudRepository<Document, Long>{
}
