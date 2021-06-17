package com.computernetworkproject.emailagent.ui.account.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.database.Mailqq;

import java.util.List;

public class AccountResultsMailQqAdapter extends ArrayAdapter<Mailqq> {
    private int resourceId;
    public AccountResultsMailQqAdapter(Context context, int textViewResourceId, List<Mailqq> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Mailqq mailqq = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        TextView mailAccount = (TextView) view.findViewById(R.id.email_account);
        TextView password= (TextView) view.findViewById(R.id.password);
        userName.setText(mailqq.getName());
        mailAccount.setText(mailqq.getAccount());
        password.setText(mailqq.getAuthcode());
        return view;
    }
}
