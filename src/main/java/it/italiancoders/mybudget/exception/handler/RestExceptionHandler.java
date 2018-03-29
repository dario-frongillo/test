package it.italiancoders.mybudget.exception.handler;

import it.italiancoders.mybudget.exception.RestException;
import it.italiancoders.mybudget.exception.error.ErrorDetail;
import it.italiancoders.mybudget.exception.error.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler  {

    protected final static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setSubcode(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle(messageSource.getMessage("BadCredentialsException.title",null,locale));
        errorDetail.setDetail(messageSource.getMessage("BadCredentialsException.detail",null,locale));
        errorDetail.setDeveloperMessage(rnfe.getMessage());
        errorDetail.setException(rnfe.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> handleInvalidGrantException(OptimisticLockingFailureException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setSubcode(HttpStatus.CONFLICT.value());
        errorDetail.setTitle(messageSource.getMessage("OptimisticLockingFailureException.title",null,locale));
        errorDetail.setDetail(messageSource.getMessage("OptimisticLockingFailureException.detail",null,locale));
        errorDetail.setDeveloperMessage(rnfe.getMessage());
        errorDetail.setException(rnfe.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.CONFLICT);
    }

    /*
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleMethodConstrainct(ConstraintViolationException manve ) {

        ErrorDetail errorDetail = new ErrorDetail();

        Locale locale = LocaleContextHolder.getLocale();
        // Populate errorDetail instance
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setSubcode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle(messageSource.getMessage("ConstraintViolationException.title",null,locale));
        errorDetail.setDetail(messageSource.getMessage("ConstraintViolationException.detail",null,locale));
        errorDetail.setException(manve.getClass().getName());
        errorDetail.setDeveloperMessage(manve.getMessage());
        errorDetail.setConstrainctErrors( new HashMap<String, List<ConstrainctError>>() );
        List<ConstraintViolation> constrainctsViolated=new ArrayList<>();
        constrainctsViolated.addAll(manve.getConstraintViolations());

        // Create ValidationError instances
        for(ConstraintViolation violation : constrainctsViolated) {
            String field=violation.getPropertyPath().toString();
            String entity=violation.getRootBeanClass().getCanonicalName();
            String message=violation.getMessage() ;
            String code=violation.getConstraintDescriptor().getAnnotation().annotationType().getName();
            List<ConstrainctError> validationErrorList = errorDetail.getConstrainctErrors().get(field);
            if(validationErrorList == null) {
                validationErrorList = new ArrayList<ConstrainctError>();
                errorDetail.getConstrainctErrors().put(field, validationErrorList);
            }
            ConstrainctError constrainctError = new ConstrainctError();
            constrainctError.setCode(code);
            constrainctError.setMessage(message);
            validationErrorList.add(constrainctError);
        }
        return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler({RestException.class})
    public ResponseEntity<Object> handleRestApiException(final RestException ex, final WebRequest request) {
        log.error(ex.getMessage(), ex);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stack = sw.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ErrorDetail errorDetail =
                ErrorDetail.newBuilder()
                        .title(ex.getTitle())
                        .detail(ex.getDetail())
                        .code(ex.getCode())
                        .validationErrors(null)
                        .status(ex.getStatus().value())
                        .exception(ex.getClass().getName())
                        .developerMessage(stack.substring(0,300))
                        .build();

        return new ResponseEntity<>(errorDetail, headers, ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException manve,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        ErrorDetail errorDetail =
                ErrorDetail.newBuilder()
                        .title(messageSource.getMessage("ValidationFailed.title",null,locale) )
                        .detail(messageSource.getMessage("ValidationFailed.detail",null,locale) )
                        .code(HttpStatus.BAD_REQUEST.value())
                        .validationErrors(new HashMap<String, List<ValidationError>>())
                        .exception(manve.getClass().getName())
                        .timeStamp(Instant.now().getEpochSecond())
                        .developerMessage(manve.toString())
                        .build();


        // Create ValidationError instances
        List<FieldError> fieldErrors =  manve.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {

            String field=fieldError.getField();
            String message=fieldError.getDefaultMessage();
            List<ValidationError> validationErrors = errorDetail.getValidationErrors().get(field);

            if(validationErrors == null) {
                validationErrors = new ArrayList<ValidationError>();
                try{
                    field =messageSource.getMessage(field,null,locale);
                }catch (Exception e){

                }
                errorDetail.getValidationErrors().put(field, validationErrors);
            }

            ValidationError validationError = new ValidationError();
            validationError.setCode(fieldError.getCodes()[0]);
            validationError.setMessage(message);
            validationErrors.add(validationError);

        });

        if(errorDetail.getValidationErrors() != null && errorDetail.getValidationErrors().size() != 0){
            String msgDetail = errorDetail.getValidationErrors().keySet().stream().findFirst().orElse(null);
            msgDetail = msgDetail.toUpperCase() + ":"+errorDetail.getValidationErrors().get(msgDetail).get(0).getMessage();
            errorDetail.setDetail(msgDetail);
        }
        return new ResponseEntity<>(errorDetail, headers, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setSubcode(status.value());
        errorDetail.setTitle(messageSource.getMessage("HttpMessageNotReadableException.title",null,locale) );
        errorDetail.setDetail(messageSource.getMessage("HttpMessageNotReadableException.detail",null,locale) );
        errorDetail.setDeveloperMessage(ex.getMessage());
        errorDetail.setException(ex.getClass().getName());

        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

}
