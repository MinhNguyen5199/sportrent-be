/**
* *
*
* @author  Meron seyoum
* @version 1.0
* @since   2022-09-24
*/
package com.SportRentalInventorySystem.BackEnd.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}