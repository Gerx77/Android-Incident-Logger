package com.riskwizard.incidentlogger.model;

/**
 * Created by mattg on 5/02/2015.
 */
public class Contact {

    int id;
    String last_name;
    String first_name;
    String position_title;
    String company;
    String email_address;

    //region Constructors
    public Contact() {
    }

    public Contact(String last_name, String first_name) {
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public Contact(int id, String last_name, String first_name) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public Contact(int id, String last_name, String first_name, String position_title, String company, String email_address) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.position_title = position_title;
        this.company = company;
        this.email_address = email_address;
    }
    //endregion


    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getLastName() {
        return this.last_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public void setPositionTitle(String position_title) {
        this.position_title = position_title;
    }

    public String getPositionTitle() {
        return this.position_title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return this.company;
    }

    public void setEmailAddress(String email_address) {
        this.email_address = email_address;
    }

    public String getEmailAddress() {
        return this.email_address;
    }


    @Override
    public String toString () {
        return String.format("%s %s", first_name, last_name);
    }
}


