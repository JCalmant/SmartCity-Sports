package com.henallux.sportapp.Model;

import com.henallux.sportapp.Exception.EmptyFieldException;
import com.henallux.sportapp.Exception.FormatException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fran on 04-12-17.
 */

public class User implements Serializable{
    private String id,userName, password, email, aboutMe, profession,photo;
    private Boolean sexe;
    private Date dateNaissance;
    private ArrayList<Disponibilite> disponibilites;

    public static final Boolean FEMME = true;
    public static final Boolean HOMME = false;

    public User(){}

    public User(String userName, String password) throws FormatException, EmptyFieldException {
        this.setUserName(userName);
        this.setPassword(password);
    }

    public User(String userName, String password, String email) throws FormatException, EmptyFieldException {
        this(userName,password);
        setEmail(email);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setId(String id){
        this.id=id;
    }
    public String getUserName(){
        return this.userName;
    }
    public ArrayList<Disponibilite> getDisponibilites(){
        if(this.disponibilites==null)return disponibilites=new ArrayList<Disponibilite>();
        return this.disponibilites;
    }

    public void setPassword(String password) throws FormatException, EmptyFieldException {
        if(!password.isEmpty()) {
            if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}")) {
                this.password = password;
            } else {
                throw new FormatException();
            }
        }
        else{
            throw  new EmptyFieldException();
        }
    }

    public void setEmail (String email) throws FormatException, EmptyFieldException {

        if(!email.isEmpty()) {
            if (email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
                this.email = email;
            } else {
                throw new FormatException();
            }
        }
        else{
            throw new EmptyFieldException();
        }
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setDateNaissance(Date dateNaissance) {
            this.dateNaissance = dateNaissance;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSexe(Boolean sexe) {
        this.sexe = sexe;
    }

    public void setUserName(String userName) throws EmptyFieldException {
        if(!userName.isEmpty()) {
            this.userName = userName;
        }
        else{
            throw new EmptyFieldException();
        }
    }

    public String getEmail() {
        return email;
    }
}