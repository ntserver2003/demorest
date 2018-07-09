package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.hibernate.exception.SQLGrammarException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class ServiceError {
    @JsonProperty("message")
    private String _message;
    public String getMessage() {
        if (_message != null) {
            return _message;
        }
        return (_exception != null) ? _exception : null;
    }

    public void setMessage(String _message) {
        this._message = _message;
    }
    @JsonProperty("exception")
    //private Exception _exception;
    private String _exception;
    public String getException()
    {
        return _exception == null ? "<null>" : _exception;
    }
    public void setException(Exception exception) {
        this._exception = formatException(exception);
    }
    public static String formatException(Exception exception) {
        StringBuilder sb = new StringBuilder();
        Throwable cause = exception.getCause();
        // SQL обрабатываем специально
        if( cause != null) {
            if (cause instanceof SQLGrammarException)
            {
                SQLGrammarException sqlGrammarException = ((SQLGrammarException) cause);
                sb.append("SQL: ");
                sb.append(sqlGrammarException.getSQL());
                sb.append(System.lineSeparator());
                SQLException sqlException = sqlGrammarException.getSQLException();
                if(sqlException!=null){
                    sb.append("SQL Exception: ");
                    sb.append(sqlException.getMessage());
                    sb.append(System.lineSeparator());
                }

            }
            /*cause = cause.getCause();
            if(cause != null ) {
                if(cause instanceof SQLServerException)
                {
                    sb.append("SQL Exception: ");
                    sb.append(cause.toString());
                } else {
                    sb.append("Caused by:");
                    sb.append(cause.toString());
                    sb.append(System.lineSeparator());
                }
            }*/
        } else
        {
            sb.append("exception: ");
            sb.append(exception.toString());
            sb.append(System.lineSeparator());
        }
        sb.append("stack:");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        sb.append(sw.toString());
        return sb.toString();
    }
}
