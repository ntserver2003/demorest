package components;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import util.ServiceError;
import util.ServiceResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler
    protected ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request)
    {
        ServiceResponse response = new ServiceResponse();
        response.setError("request: " + request.toString()
                + "\r\n error: " + ServiceError.formatException(ex));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return handleExceptionInternal(ex, response.Serialize(), headers, response.getHttpStatus(), request);
    }
}
