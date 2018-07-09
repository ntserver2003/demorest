package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.io.Serializable;
import static models.DocumentSql.NAMED_SELECT_ALL;

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = NAMED_SELECT_ALL
                , query = "SELECT * FROM Documents"
                , resultClass = Document.class)
})
@Table(name = "documents")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long _id;
    @Column(name = "description")
    private String _description;

    public Document() {

    }

    public Document(long id, String description) {
        this._id = id;
        this._description = description;
    }

    public long getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    @JsonProperty("descr")
    public String getDescription() {
        return _description;
    }

    public void setDescription(String descr) {
        _description = descr;
    }

}
