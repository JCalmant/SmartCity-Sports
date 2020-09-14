package com.henallux.sportapp.Exception;

/**
 * Created by fran on 07-12-17.
 */

public class FormatException extends Exception {
    public String getException(String nom){
        return nom+" ne respecte pas le format demandé";
    }
}
