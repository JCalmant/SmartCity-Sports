package com.henallux.sportapp.Model.DTO;

import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.io.Serializable;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fran on 10-12-17.
 */

public class UserDTO implements Serializable{

    private String id;
    private String username;
    private Date dateNaissance;
    private boolean sexe;
    private String photo;
    private String aboutMe;
    private String profession;
    private ArrayList<DisponibiliteDTO> disponibilites;
    private ArrayList<AmiDTO> amis;

    public UserDTO(String id,String username,Date dateNaissance,boolean sexe,String photo,String aboutMe,String profession, ArrayList<DisponibiliteDTO> disponibilites, ArrayList<AmiDTO> amis){
        setAboutMe(aboutMe);
        setAmis(amis);
        setDateNaissance(dateNaissance);
        setDisponibilites(disponibilites);
        setId(id);
        setPhoto(photo);
        setProfession(profession);
        setUsername(username);
        setSexe(sexe);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSexe(Boolean sexe) {
        this.sexe = sexe;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getProfession() {
        return profession;
    }

    public String getId() {
        return id;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public boolean getSexe() {
        return sexe;
    }

    public void setDisponibilites(ArrayList<DisponibiliteDTO> disponibilites) {
        this.disponibilites = disponibilites;
    }

    public ArrayList<AmiDTO> getAmis() {
        return this.amis;
    }

    public ArrayList<DisponibiliteDTO> getDisponibilites() {
        return disponibilites;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getPhoto() {
        return photo;
    }

    public void setDateNaissance(Date dateNaissance) {
        // Structure d'une DateTime reçue par c# 1997-01-31T00:00:00
        this.dateNaissance = dateNaissance;
    }

    public void setAmis(ArrayList<AmiDTO> amis) {
        this.amis = amis;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAge(){
        LocalDate birthdate = new LocalDate(dateNaissance);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        return age.getYears();
    }

    public AmiDTO getAmitié(String ami,String amiBis){
        int i = 0;
        while(!((getAmis()
                .get(i)
                .getAmiAjouté()
                .equals(ami) ||
              getAmis()
                .get(i)
                .getAmiAjouteur()
                .equals(ami))
                &&
                (getAmis()
                .get(i)
                .getAmiAjouté()
                .equals(amiBis) ||
              getAmis()
                .get(i)
                .getAmiAjouteur()
                .equals(amiBis)))){
            i++;
        }
        return getAmis().get(i);
    }
}
