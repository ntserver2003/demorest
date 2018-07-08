package models;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;


@Entity
@Table(name= "documents")
@NamedNativeQueries({
        @NamedNativeQuery(name = "selectDocuments"
                , query = "SELECT * FROM Documents"
                , resultClass = Document.class)
})
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long _id;
    @Column(name = "description")
    private String _description;
    public Document()
    {

    }
    public Document(long id, String description)
    {
        this._id = id;
        this._description = description;
    }
    public long getId() {
        return _id;
    }
    public void setId(Integer id) { _id = id;}

    @JsonProperty("descr")
    public String getDescription()
    {
        return _description;
    }
    public void setDescription(String descr) {_description = descr;}

}
