package util;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class ServiceResponse<T> implements Serializable {
    @JsonProperty("result")
    private T _result;

    public T getResult() {
        return _result;
    }

    public void setResult(T result) {
        this._result = result;
        this._httpStatus = HttpStatus.OK;
    }

    @JsonProperty("error")
    private ServiceError _error;

    public void setError(String msg) {
        setError(msg, null);
    }

    public void setError(Exception ex) {
        setError(null, ex);
    }

    private void setError(String message, Exception ex) {
        // Ничего не передали - выходим
        if ((message == null || message.isEmpty()) && ex == null) return;
        // Объекта ServiceError нет - создадим
        if (this._error == null) {
            this._error = new ServiceError();
        }
        // Есть сообщение
        if (message != null && !message.isEmpty()) {
            this._error.setMessage(message);
        } else // сообщение пустое - берем из Exception
        {
            this._error.setMessage(ex.getMessage());
        }
        if (ex != null) {
            this._error.setException(ex);
        }
        this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @JsonIgnore
    private HttpStatus _httpStatus;

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return _httpStatus;
    }

    public void setHttpStatus(HttpStatus _httpStatus) {
        this._httpStatus = _httpStatus;
    }

    public ServiceResponse(){}
    private static ObjectWriter _writer;
    public String Serialize()
    {
        if (_writer == null)
        {
            _writer = new ObjectMapper().writer();
        }
        try {
            return _writer.writeValueAsString(this);
        } catch (Exception ex) {

            ServiceError se = new ServiceError();
            se.setMessage("Unable serialize ServiceResponse");
            se.setException(ex);
            return se.getMessage() + ":" + se.getException();
        }
    }

}
