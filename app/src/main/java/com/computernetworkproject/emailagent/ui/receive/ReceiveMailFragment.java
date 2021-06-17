package com.computernetworkproject.emailagent.ui.receive;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.SocketEvents;

public class ReceiveMailFragment extends Fragment {
    private Handler handler; //线程控制器，安卓开发的原则是涉及网络请求必须用多线程
    private ProgressDialog progressDialog;
    private EditText sequence_mail;
    private TextView mail_number, mail_content;
    private Button button_confirm;
    private String userAddress;
    private String userPassword;
    private SocketEvents socketEvents;
    private int mailType, mailNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receive_mail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());//创建Dialog，提示正在查询数量
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for emails...");
        handler = new Handler();
        userAddress = getArguments().getString("userAddress");
        userPassword = getArguments().getString("userPassword");
        mailType = getArguments().getInt("mailType");
        socketEvents = new SocketEvents(userAddress, userPassword, mailType);
        sequence_mail = view.findViewById(R.id.edit_receive_number);
        mail_number = view.findViewById(R.id.text_receive_number);
        mail_content = view.findViewById(R.id.text_receive_content);
        button_confirm = view.findViewById(R.id.button_recceeive_confirm);

        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mailNumber = socketEvents.getMailNumber();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mailNumber == -1) {
                            Toast.makeText(getContext(),"Failed to inquire the mail number!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            mail_number.setText("Error");
                        }
                        else if(mailNumber == 0){
                            progressDialog.dismiss();
                            mail_number.setText("None email unchecked!");
                        }else if(mailNumber == 1){
                            progressDialog.dismiss();
                            mail_number.setText("1 email remains unchecked.");
                        }else{
                            progressDialog.dismiss();
                            mail_number.setText(mailNumber + " emails remain unchecked!");
                        }
                    }
                });
            }
        }).start();

        button_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int input_number = Integer.parseInt(sequence_mail.getText().toString());
                if(mailNumber < 1 || input_number < 1 || input_number > mailNumber){
                    Toast.makeText(getContext(),"Please enter the valid number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show(); //合法输入后，显示进度条
                new Thread(new Runnable() { //写线程（处理网络请求必须多线程），和检测账户合法性的时候类似，里面调用SocketEvents的sendMail方法
                    @Override
                    public void run() {
                        final String content = socketEvents.getMail(input_number);
                        Log.d("TESTCONTENT", content);
                        if(content != null){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    mail_content.setText(content);
                                }
                            });
                        }else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Failed to get the email content!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}