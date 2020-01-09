package com.bridgelabz.fundoonotes.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bridgelabz.fundoonotes.response.Response;

@RestControllerAdvice
public class RestExceptionHandler
{
    @ExceptionHandler(value = { IOException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequest(Exception ex)
    {
        return new Response( "Bad Request" ,400);
    }
    
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unKnownException(Exception ex)
    {
        return new Response( "Employee Not Found" , 400);
    }
}