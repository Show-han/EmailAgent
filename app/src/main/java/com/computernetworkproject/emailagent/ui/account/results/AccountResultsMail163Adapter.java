package com.computernetworkproject.emailagent.ui.account.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.database.Mail163;

import java.util.List;

public class AccountResultsMail163Adapter extends ArrayAdapter<Mail163> {
    private int resourceId;
    public AccountResultsMail163Adapter(Context context, int textViewResourceId, List<Mail163> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Mail163 mail163 = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        TextView mailAccount = (TextView) view.findViewById(R.id.email_account);
        TextView password= (TextView) view.findViewById(R.id.password);
        userName.setText(mail163.getName());
        mailAccount.setText(mail163.getAccount());
        password.setText(mail163.getAuthcode());
        return view;
    }
}
