package util;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
class ServiceError {
    @JsonProperty("message")
    private String _message;
    @JsonProperty("exception")
    private Exception _exception;

    public String get_message() {
        if (_message != null) {
            return _message;
        }
        if(_exception != null) {
            return _exception.getMessage();
        }
        return null;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_exception(Exception _exception) {
        this._exception = _exception;
    }
}

@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class ServiceResponse<T> implements Serializable{
    @JsonProperty("result")
    private T _result;

    @JsonProperty("error")
    private ServiceError _error;

    private static ObjectWriter _writer;
    public static ObjectWriter getWriter() {
        if (_writer == null) {
            _writer = new ObjectMapper().writer();
        }
        return _writer;
    }

    public ServiceResponse SetError(String msg) {
        SetError(msg, null);
        return this;
    }
    public ServiceResponse SetError(Exception ex) {
        SetError(null, ex);
        return this;
    }
    public ServiceResponse SetError(String message, Exception ex)
    {
        // Ничего не передали - выходим
        if ((message == null || message.isEmpty()) && ex == null) return this;
        // Объекта ServiceError нет - создадим
        if(this._error == null) {
            this._error = new ServiceError();
        }
        // Есть сообщение
        if(message != null && !message.isEmpty())
        {
            this._error.set_message(message);
        } else // сообщение пустое - берем из Exception
        {
            this._error.set_message(ex.getMessage());
        }
        if(ex != null)
        {
            this._error.set_exception(ex);
        }
        return this;
    }

    public ServiceResponse(T result)
    {
        this._result = result;
    }
    public ServiceResponse(){}
    public static String Serialize(Object result) throws JsonProcessingException
    {
        return new ServiceResponse<>(result).Serialize();
    }

    public String Serialize() throws JsonProcessingException
    {
        try {
            return getWriter().writeValueAsString(this);
        } catch (Exception ex) {
            ServiceError se = new ServiceError();
            se.set_message("Unable serialize ServiceResponse");
            se.set_exception(ex);
            return getWriter().writeValueAsString(ex);
        }
    }
    public T get_result() {
        return _result;
    }
    public void set_result(T result)
    {
        this._result = result;
    }
}
