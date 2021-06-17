package com.computernetworkproject.emailagent.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.computernetworkproject.emailagent.R;

public class AccountFragment extends Fragment {


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //在account fragment中，设置监听器分别导航至163_account和qq_account
        Button button_163 = view.findViewById(R.id.button_to_account_163);
        Button button_qq = view.findViewById(R.id.button_to_account_qq);
        button_163.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mailType", 1);
                //163邮箱的mailType为1
                Navigation.findNavController(v).navigate(R.id.action_nav_account_to_accountDisplayFragment, bundle);
            }
        });
        button_qq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mailType", 2);
                //qq邮箱的mailType为2
                Navigation.findNavController(v).navigate(R.id.action_nav_account_to_accountDisplayFragment, bundle);
            }
        });
    }
}