package com.henallux.sportapp.Exception;

/**
 * Created by fran on 07-12-17.
 */

public class EmptyFieldException extends Exception {

    public String getException(String champ){
        return champ + " vide";
    }
}
