package com.riskwizard.incidentlogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.riskwizard.incidentlogger.R;
import com.riskwizard.incidentlogger.helper.DateTimeHelper;
import com.riskwizard.incidentlogger.model.Incident;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mattg on 3/03/2015.
 */
public class IncidentsAdapter extends ArrayAdapter<Incident> {


    public IncidentsAdapter(Context context, ArrayList<Incident> incidents) {
        super(context, 0, incidents);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Incident incident = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_incident, parent, false);
        }
        // Lookup view for data population
        //TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvIncidentType = (TextView) convertView.findViewById(R.id.tvIncidentType);
        TextView tvCreatedBy = (TextView) convertView.findViewById(R.id.tvCreatedBy);
        TextView tvIncidentDate = (TextView) convertView.findViewById(R.id.tvCreatedDate);

        // Populate the data into the template view using the data object
        //tvId.setText(String.valueOf(incident.getId()));
        tvIncidentType.setText(incident.getIncidentType().getIncidentTypeName());
        tvCreatedBy.setText(incident.getCreatedBy().toString());
        //tvIncidentDate.setText(String.valueOf(incident.getIncidentDate()));

        //Date aDate = incident.getIncidentDate();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm a", Locale.getDefault());
        //tvIncidentDate.setText(String.valueOf(dateFormat.format(aDate)));

        if (incident.getIncidentDate() != null)
            tvIncidentDate.setText(DateTimeHelper.toLocaleDateTime(incident.getIncidentDate()));

        // Return the completed view to render on screen
        return convertView;
    }

}
