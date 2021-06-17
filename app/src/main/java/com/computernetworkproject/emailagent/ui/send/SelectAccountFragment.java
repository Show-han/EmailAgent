package com.computernetworkproject.emailagent.ui.send;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.computernetworkproject.emailagent.R;
import com.computernetworkproject.emailagent.SocketEvents;
import com.computernetworkproject.emailagent.database.Mail163;
import com.computernetworkproject.emailagent.database.Mailqq;
import com.computernetworkproject.emailagent.ui.account.results.AccountResultsMail163Adapter;
import com.computernetworkproject.emailagent.ui.account.results.AccountResultsMailQqAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;

import java.util.List;

public class SelectAccountFragment extends Fragment {
    private List<Mail163> mail163;
    private List<Mailqq> mailqq;
    private int mailType;
    private Handler handler;
    private ProgressDialog progressDialog;
    private SocketEvents socketEvents;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        progressDialog = new ProgressDialog(getContext());//创建加载框
        progressDialog.setMessage("Login...");
        progressDialog.setCancelable(false);
        mailType = getArguments().getInt("mailType"); //获得邮件服务器种类
        NavController navController = Navigation.findNavController(view); //初始化Navigation的controller
        if(mailType == 1){
            mail163 = LitePal.findAll(Mail163.class); //在数据库中加载所有的注册过的账户
            if(mail163.isEmpty()){
                Snackbar.make(view, "There is no recorded account. Add an account?", Snackbar.LENGTH_LONG)
                        .setAction("Add Account", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                navController.navigate(R.id.action_selectAccountFragment_to_addAccount163Fragment);
                            }
                        })
                        .show();
            }
            AccountResultsMail163Adapter adapter = new AccountResultsMail163Adapter(this.getContext(), R.layout.account_results, mail163);
            ListView listView = (ListView) view.findViewById(R.id.select_accountResults_list_view);
            listView.setAdapter(adapter); //设置listView的adapter，加载所有的account结果
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ //监听每个点击listview的事件
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Mail163 mail = mail163.get(position); //通过position获取点击的mail
                    socketEvents = new SocketEvents(mail.getAccount(), mail.getAuthcode(), mailType); //构造socketevent对象，调用写好的方法
                    progressDialog.show();
                    new Thread(new Runnable() { //创建一个线程，匿名内部类写run，然后直接start
                        @Override
                        public void run() {
                            if(socketEvents.checkAccount()){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Bundle bundle = new Bundle(); //将这个fragment里的各种用户信息传给下一个fragment
                                        bundle.putString("userAddress", mail.getAccount() + "@163.com");
                                        bundle.putString("userPassword", mail.getAuthcode());
                                        bundle.putInt("mailType", mailType);
                                        navController.navigate(R.id.action_selectAccountFragment_to_sendMailFragment, bundle);
                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
        }else{ //qq与上面同理，不再写注释
            mailqq = LitePal.findAll(Mailqq.class);
            if(mailqq.isEmpty()){
                Snackbar.make(view, "There is no recorded account. Add an account?", Snackbar.LENGTH_LONG)
                        .setAction("Add Account", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                navController.navigate(R.id.action_selectAccountFragment_to_addAccountQqFragment);
                            }
                        })
                        .show();
            }
            AccountResultsMailQqAdapter adapter = new AccountResultsMailQqAdapter(this.getContext(), R.layout.account_results, mailqq);
            ListView listView = (ListView) view.findViewById(R.id.select_accountResults_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Mailqq mail = mailqq.get(position);

                    socketEvents = new SocketEvents(mail.getAccount(), mail.getAuthcode(), mailType);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(socketEvents.checkAccount()){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userAddress", mail.getAccount() + "@qq.com");
                                        bundle.putString("userPassword", mail.getAuthcode());
                                        bundle.putInt("mailType", mailType);
                                        navController.navigate(R.id.action_selectAccountFragment_to_sendMailFragment, bundle);
                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Login failed!", Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
        }
        Button button_to_add = view.findViewById(R.id.button_to_add_account); //这个按钮可以导航到添加账户fragment
        button_to_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mailType==1){
                    navController.navigate(R.id.action_selectAccountFragment_to_addAccount163Fragment);
                }else{
                    navController.navigate(R.id.action_selectAccountFragment_to_addAccountQqFragment);
                }

            }
        });
    }
}