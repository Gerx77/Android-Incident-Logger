package com.riskwizard.incidentlogger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mattg on 5/02/2015.
 */
public class Incident {

    int id;
	IncidentType type;
	Contact created_by;
	Date incident_datetime;
	List<Contact> notify;
	String description;
	String initial_action;
	boolean reportable;
    List<Attachment> attachments;

    // constructors
    public Incident() {

    }

    public Incident(IncidentType type, Contact created_by) {
        this.type = type;
        this.created_by = created_by;
    }


    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setIncidentType(IncidentType type) {
        this.type = type;
    }

    public IncidentType getIncidentType() {
        return this.type;
    }

    public void setCreatedBy(Contact created_by) {
        this.created_by = created_by;
    }

    public Contact getCreatedBy() {
        return this.created_by;
    }

    public void setIncidentDate(Date incident_date) {
        this.incident_datetime = incident_date;
    }

    public Date getIncidentDate() {
        return this.incident_datetime;
    }

    public void setNotifyContacts(List<Contact> notify) {
        this.notify = notify;
    }

    public List<Contact> getNotifyContacts() {
        if (this.notify == null)
            return new ArrayList<>();
        else
            return this.notify;
    }

	public void setDescription(String description)
	{
		this.description = description;
	}

    public String getDescription() {
        return this.description;
    }

	public void setInitialAction(String initial_action)
	{
		this.initial_action = initial_action;
	}

    public String getInitialAction() {
        return this.initial_action;
    }

	public void setReportable(boolean reportable)
	{
		this.reportable = reportable;
	}

    public boolean getReportable() {
        return this.reportable;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
        if (this.attachments == null)
            return new ArrayList<>();
        else
            return this.attachments;
    }

    @Override
    public String toString () {
        return String.format("%s - [%s]", type, incident_datetime);
    }
}
