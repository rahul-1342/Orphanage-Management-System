package com.example.orphanage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class ApplicationAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<Map<String, String>> applicationList;

    public ApplicationAdapter(Context context, List<Map<String, String>> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    @Override
    public int getCount() {
        return applicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return applicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.application_item, parent, false);
        }

        TextView tvAdopterName = convertView.findViewById(R.id.tvAdopterName);
        TextView tvContact = convertView.findViewById(R.id.tvContact);
        TextView tvChildPreference = convertView.findViewById(R.id.tvChildPreference);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        Map<String, String> application = applicationList.get(position);

        tvAdopterName.setText("Name: " + application.get("adopterName"));
        tvContact.setText("Contact: " + application.get("contact"));
        tvChildPreference.setText("Preference: " + application.get("childPreference"));
        tvStatus.setText("Status: " + application.get("status"));

        return convertView;
    }
}
