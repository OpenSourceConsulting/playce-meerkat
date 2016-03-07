package com.athena.meerkat.controller.web.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    //public static final String DEFAULT_ERROR_VIEW = "error";
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public SimpleJsonResponse defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        LOGGER.error(e.getLocalizedMessage(), e);
        SimpleJsonResponse json = new SimpleJsonResponse();
        
        json.setSuccess(false);
        json.setMsg(e.toString());
        
        return json;
        
        // Otherwise setup and send the user to a default error-view.
        /*
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
        */
    }
}