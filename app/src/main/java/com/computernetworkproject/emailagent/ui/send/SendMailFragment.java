package com.computernetworkproject.emailagent.ui.send;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.SocketEvents;

public class SendMailFragment extends Fragment {
    private Handler handler; //线程控制器，安卓开发的原则是涉及网络请求必须用多线程
    private ProgressDialog progressDialog;
    private EditText edit_receiver, edit_subject, edit_content;//分别指定用户填写的接收者、邮件主题和内容
    private TextView textview_sender, button_send; //指定发件人，这里为了ui界面明了传入这个信息
    private String userAddress;
    private String userPassword;
    private NavController navController;
    private SocketEvents socketEvents;
    private int mailType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_send_mail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());//创建Dialog，提示正在发送邮件
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        handler = new Handler();
        userAddress = getArguments().getString("userAddress");
        userPassword = getArguments().getString("userPassword");
        mailType = getArguments().getInt("mailType");
        edit_receiver = view.findViewById(R.id.send_receiver);
        edit_subject = view.findViewById(R.id.send_subject);
        edit_content = view.findViewById(R.id.send_content);
        textview_sender = view.findViewById(R.id.send_sender);
        button_send = view.findViewById(R.id.text_send_mail);
        textview_sender.setText(userAddress);
        navController = Navigation.findNavController(view); //初始化Navigation的controller
        button_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String receiver = edit_receiver.getText().toString();
                String subject = edit_subject.getText().toString();
                String content = edit_content.getText().toString();
                if(receiver.isEmpty() || subject.isEmpty() || content.isEmpty()){
                    Toast.makeText(getContext(),"Please enter the completed information!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show(); //合法输入后，显示进度条
                socketEvents = new SocketEvents(userAddress, userPassword, mailType, receiver, subject, content);
                new Thread(new Runnable() { //写线程（处理网络请求必须多线程），和检测账户合法性的时候类似，里面调用SocketEvents的sendMail方法
                    @Override
                    public void run() {
                        if(socketEvents.sendEmail()){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    navController.navigate(R.id.action_sendMailFragment_to_sendSuccessfulFragment);
                                }
                            });
                        }else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Sending failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}