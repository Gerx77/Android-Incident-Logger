package com.riskwizard.incidentlogger.model;

/**
 * Created by mattg on 5/02/2015.
 */
public class IncidentType {

    int id;
    String incidenttype_name;
    String incidenttype_description;

    // Constructors
    public IncidentType() {
    }

    public IncidentType(String incidenttype_name) {
        this.incidenttype_name = incidenttype_name;
    }

    public IncidentType(int id, String incidenttype_name) {
        this.id = id;
        this.incidenttype_name = incidenttype_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIncidentTypeName(String incidenttype_name) {
        this.incidenttype_name = incidenttype_name;
    }

    public String getIncidentTypeName() {
        return this.incidenttype_name;
    }

    public void setIncidentTypeDescription(String incidenttype_description) {
        this.incidenttype_description = incidenttype_description;
    }

    public String getIncidentTypeDescription() {
        return this.incidenttype_description;
    }

    @Override
    public String toString () {
        return incidenttype_name;
    }
}


