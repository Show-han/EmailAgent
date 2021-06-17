package com.computernetworkproject.emailagent.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.database.Mail163;
import com.computernetworkproject.emailagent.database.Mailqq;


public class AddAccount163Fragment extends Fragment {

    private Mail163 mail163 = new Mail163();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        EditText editText_userName = view.findViewById(R.id.edittext_username_163);
        EditText editText_userAddress = view.findViewById(R.id.edittext_useremail_163);
        EditText editText_userAuthcode = view.findViewById(R.id.edittext_userauthcode_163);
        Button buttonSubmit = view.findViewById(R.id.button_submit_163);

        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mail163.setName(editText_userName.getText().toString());
                mail163.setAccount(editText_userAddress.getText().toString());
                mail163.setAuthcode(editText_userAuthcode.getText().toString());
                if(mail163.save()){
                    Toast.makeText(getContext(), "Successful!", Toast.LENGTH_SHORT).show();
                    navController.popBackStack();
                }else{
                    Toast.makeText(getContext(), "Failed to add the account!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_add_account163, container, false);
    }
}
