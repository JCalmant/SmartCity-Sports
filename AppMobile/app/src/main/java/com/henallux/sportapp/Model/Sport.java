package com.henallux.sportapp.Model;

/**
 * Created by fran on 04-12-17.
 */

public class Sport {
    private Integer sportId;
    private String libellé;
    public Sport(Integer id, String libellé){
        sportId=id;
        this.libellé=libellé;
    }

    @Override
    public String toString(){
        return this.libellé;
    }

    public Integer getId(){
        return this.sportId;
    }
}