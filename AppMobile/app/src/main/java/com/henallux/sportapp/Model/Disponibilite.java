package com.henallux.sportapp.Model;

/**
 * Created by fran on 06-12-17.
 */

public class Disponibilite {
    public Integer sportId,complexeSportifId;
    public String userId;

    public Disponibilite(Sport sport, ComplexeSportif complexeSportif, User user) {
        this.setComplexeSportifId(complexeSportif);
        this.sportId=sport.getId();
        this.setUserId(user);
    }

    public void setComplexeSportifId(ComplexeSportif complexeSportif) {
        if(complexeSportif!=null)
            this.complexeSportifId = complexeSportif.getId();
        this.complexeSportifId=null;
    }

    public void setUserId(User user) {
        if(user!=null)
            this.userId = user.getId();
        this.userId=null;
    }
}