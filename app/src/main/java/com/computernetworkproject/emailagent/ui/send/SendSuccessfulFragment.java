package com.computernetworkproject.emailagent.ui.send;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.computernetworkproject.emailagent.R;

public class SendSuccessfulFragment extends Fragment {
    private Button button_another, button_home;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_successful, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_another = view.findViewById(R.id.button_send_another);
        button_home = view.findViewById(R.id.button_send_return);
        navController = Navigation.findNavController(view);

        button_another.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_sendSuccessfulFragment_to_nav_home);
            }
        });
    }
}