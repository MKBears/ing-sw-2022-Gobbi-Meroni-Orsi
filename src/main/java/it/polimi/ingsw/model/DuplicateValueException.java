package it.polimi.ingsw.model;

public class DuplicateValueException extends Exception{

    public DuplicateValueException (String errorMessage){
        super(errorMessage);
    }

}
