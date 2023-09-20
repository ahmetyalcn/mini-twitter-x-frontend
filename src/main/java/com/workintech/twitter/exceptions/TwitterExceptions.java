package com.workintech.twitter.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TwitterExceptions extends RuntimeException{
    private HttpStatus httpStatus;

    public TwitterExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
