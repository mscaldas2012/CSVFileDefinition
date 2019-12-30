package gov.cdc.nccdphp.esurveillance.rest;

import gov.cdc.nccdphp.esurveillance.rest.model.CDCLogEntry;
import gov.cdc.nccdphp.esurveillance.rest.model.ERROR_CODES;
import gov.cdc.nccdphp.esurveillance.rest.model.ErrorReceipt;
import gov.cdc.nccdphp.esurveillance.rest.security.ServiceNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.InputMismatchException;

/**
 * Error handling for the application.
 * @author hxo5 created on 10/26/2017
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private CDCLoggerService cdcLoggerService;

	@Value("${cdc-logging.env}")
	private String environment;


	@ExceptionHandler(value = { NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        
    	logger.error("Error: "+ex.getMessage());
        ErrorReceipt error = new ErrorReceipt(ERROR_CODES.NOT_FOUND, ex.getMessage(), 
        		HttpStatus.NOT_FOUND.value(), request.getRequestURL().toString(), 
        		ex.getClass().getName());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    	logger.error("Error: "+ex.getMessage());
		ErrorReceipt error = new ErrorReceipt(ERROR_CODES.NOT_FOUND, "Invalid path",
				HttpStatus.NOT_FOUND.value(), request.getDescription(false), 
				ex.getClass().getName());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}    
    
    @ExceptionHandler(value = { IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, HttpServletRequest request) {

    	logger.error("Error: "+ex.getMessage());
        ErrorReceipt error = new ErrorReceipt(ERROR_CODES.BAD_REQUEST, ex.getMessage(), 
        		HttpStatus.BAD_REQUEST.value(), request.getRequestURL().toString(), 
        		ex.getClass().getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }    

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
    	String message = ex.getBindingResult().getFieldError().getDefaultMessage();
    	if(message==null || message.length() == 0){
    		message = ex.getMessage();
    	}
    	logger.error("Status:"+status+", request:"+request.toString()+"ex:"+ex.getMessage());
		ErrorReceipt error = new ErrorReceipt(ERROR_CODES.BAD_REQUEST, message, 
				HttpStatus.BAD_REQUEST.value(), request.getDescription(false), 
				ex.getClass().getName());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(value = { DuplicateKeyException.class,
    							InputMismatchException.class,
    							})
    protected ResponseEntity<Object> handleDuplicateException(RuntimeException ex, HttpServletRequest request) {
        
    	logger.error("Error: "+ex.getMessage());
        ErrorReceipt error = new ErrorReceipt(ERROR_CODES.CONFLICT, ex.getMessage(),
        		HttpStatus.CONFLICT.value(), request.getRequestURL().toString(), 
        		ex.getClass().getName());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }    
    
    @Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

    	logger.error("Error: "+ex.getMessage());
		ErrorReceipt error = new ErrorReceipt(ERROR_CODES.BAD_REQUEST, "Invalid method request", 
				HttpStatus.METHOD_NOT_ALLOWED.value(), request.getDescription(false), 
				ex.getClass().getName());
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
	}
    
    @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

    	logger.error("Error: "+ex.getMessage());
    	//ex.getMostSpecificCause().getMessage()
		ErrorReceipt error = new ErrorReceipt(ERROR_CODES.BAD_REQUEST, "The message is not readable" , 
				HttpStatus.NOT_ACCEPTABLE.value(), request.getDescription(false), 
				ex.getClass().getName());
		
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorReceipt handleGenericError(HttpServletRequest req, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();

		CDCLogEntry logEntry = new CDCLogEntry(environment, "CSVFileDef_01", req.getRequestURL().toString(), "handleGenericError", e.getMessage(), stackTrace);
		//Send logEntry

		cdcLoggerService.sendError(logEntry);
		return new ErrorReceipt(ERROR_CODES.INTERNAL_SERVER_ERROR, e.getMessage(), 500, req.getServletPath(),  e.getClass().getName());
	}

	@ExceptionHandler(ServiceNotAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorReceipt> handleUnauthorizedError(HttpServletRequest req, ServiceNotAuthorizedException e) {
//		CDCLogEntry logEntry = new CDCLogEntry(environment, "CSVFileDef_01", req.getRequestURL().toString(), "handleUnauthorizedError", e.getMessage(), null);
//		cdcLoggerService.sendError(logEntry);
		ErrorReceipt error =  new ErrorReceipt(ERROR_CODES.UNAUTHORIZED, e.getMessage(), 401, req.getServletPath(),  e.getClass().getName());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

	}
}
