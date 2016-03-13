package de.admir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CrudOperationException extends RuntimeException {
    public CrudOperationException(String s) {
        super(s);
    }
}
