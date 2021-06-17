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
import com.computernetworkproject.emailagent.database.Mailqq;


public class AddAccountQqFragment extends Fragment {

    private Mailqq mailqq = new Mailqq();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        EditText editText_userName = view.findViewById(R.id.edittext_username_qq);
        EditText editText_userAddress = view.findViewById(R.id.edittext_useremail_qq);
        EditText editText_userAuthcode = view.findViewById(R.id.edittext_userauthcode_qq);
        Button buttonSubmit = view.findViewById(R.id.button_submit_qq);

        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mailqq.setName(editText_userName.getText().toString());
                mailqq.setAccount(editText_userAddress.getText().toString());
                mailqq.setAuthcode(editText_userAuthcode.getText().toString());
                if(mailqq.save()){
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


        return inflater.inflate(R.layout.fragment_add_account_qq, container, false);
    }
}
