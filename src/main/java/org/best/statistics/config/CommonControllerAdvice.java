package org.best.statistics.config;

import lombok.extern.slf4j.Slf4j;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.util.DateUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.SessionException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * 全局异常统一处理
 */
@Slf4j
@ControllerAdvice
public class CommonControllerAdvice implements PriorityOrdered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat formatter = DateUtils.dateFormatter(DateUtils.Pattern.DATE_TIME_PATTERN);
        CustomDateEditor dateEditor = new CustomDateEditor(formatter, true);
        binder.registerCustomEditor(Date.class, dateEditor);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(SessionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public void exceptionHandler(SessionException e, HttpServletRequest request) {
        log.warn("[{}]session expired. uri:[{}]{}", TraceTagConstants.CONTROLLER_ADVICE, request.getMethod(),
                request.getRequestURI());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, HttpMessageNotWritableException.class})
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public void exceptionHandler(HttpMessageConversionException e, HttpServletRequest request) {
        log.warn("[{}]http message not converse:{}. uri:[{}]{}", TraceTagConstants.CONTROLLER_ADVICE, e.getMessage(),
                request.getMethod(),
                request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public void exceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(err -> {
            if (err == null) {
                return;
            }
            String field = err.getField();
            String defaultMessage = err.getDefaultMessage();
            Object rejectedValue = err.getRejectedValue();
            log.warn("[{}]method argument not valid. uri:[{}]{}, msg:{}, field:{}, value:{}",
                    TraceTagConstants.CONTROLLER_ADVICE,
                    request.getMethod(), request.getRequestURI(), defaultMessage, field, rejectedValue);
        });
    }
}
