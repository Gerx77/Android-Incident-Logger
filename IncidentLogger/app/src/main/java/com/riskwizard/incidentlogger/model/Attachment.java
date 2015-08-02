package com.riskwizard.incidentlogger.model;

import java.util.Date;

/**
 * Created by mattg on 10/02/2015.
 */
public class Attachment {

    int id;
    String filename;
    String description;
    Contact attached_by;
    Date attached_date;
    String type;
    String size;
    String physical_filename;

    //region Constructors
    public Attachment() {
    }

    public Attachment(String physical_filename) {
        this.physical_filename = physical_filename;
    }

    public Attachment(int id, String filename, String description, Contact attached_by, Date attached_date, String type, String size, String physical_filename) {
        this.id = id;
        this.filename = filename;
        this.description = description;
        this.attached_by = attached_by;
        this.attached_date = attached_date;
        this.type = type;
        this.size = size;
        this.physical_filename = physical_filename;
    }
    //endregion

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setAttachedBy(Contact attached_by) {
        this.attached_by = attached_by;
    }

    public Contact getAttachedBy() {
        return this.attached_by;
    }

    public void setAttachedDate(Date attached_date) {
        this.attached_date = attached_date;
    }

    public Date getAttachedDate() {
        return this.attached_date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    public void setPhysicalFilename(String physical_filename) {
        this.physical_filename = physical_filename;
    }

    public String getPhysicalFilename() {
        return this.physical_filename;
    }

    @Override
    public String toString () {
        return String.format("%s", filename);
    }

}
