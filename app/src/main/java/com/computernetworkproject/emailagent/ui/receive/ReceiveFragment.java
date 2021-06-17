package com.computernetworkproject.emailagent.ui.receive;

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

public class ReceiveFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receive_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_163 = view.findViewById(R.id.button_receive_163);
        Button button_qq = view.findViewById(R.id.button_receive_qq);


        button_163.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mailType", 1);
                Navigation.findNavController(v).navigate(R.id.action_nav_receive_to_selectAccountFragment2, bundle);
            }
        });
        button_qq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mailType", 2);
                Navigation.findNavController(v).navigate(R.id.action_nav_receive_to_selectAccountFragment2, bundle);
            }
        });
    }
}