package com.computernetworkproject.emailagent.ui.account.results;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;


public class AccountDisplayFragment extends Fragment {
    private List<Mail163> mail163;
    private List<Mailqq> mailqq;
    private int mailType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mailType = getArguments().getInt("mailType");
        NavController navController = Navigation.findNavController(view);
        if(mailType == 1){
            mail163 = LitePal.findAll(Mail163.class);
            if(mail163.isEmpty()){
                Snackbar.make(view, "There is no recorded account. Add an account?", Snackbar.LENGTH_LONG)
                        .setAction("Add Account", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                navController.navigate(R.id.action_accountDisplayFragment_to_addAccount163Fragment);
                            }
                        })
                        .show();
            }
            AccountResultsMail163Adapter adapter = new AccountResultsMail163Adapter(this.getContext(), R.layout.account_results, mail163);
            ListView listView = (ListView) view.findViewById(R.id.accountResults_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ //监听每个点击listview的事件
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Mail163 mail = mail163.get(position);
                    Snackbar.make(view, "Are you sure to delete the account?", Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LitePal.delete(Mail163.class, mail.getId());
                            //更新listView的适配器，来更新界面数据
                            mail163 = LitePal.findAll(Mail163.class);
                            AccountResultsMail163Adapter adapter = new AccountResultsMail163Adapter(getContext(), R.layout.account_results, mail163);
                            listView.setAdapter(adapter);
                        }
                    }).show();
                }
            });
        }else{
            mailqq = LitePal.findAll(Mailqq.class);
            if(mailqq.isEmpty()){
                Snackbar.make(view, "There is no recorded account. Add an account?", Snackbar.LENGTH_LONG)
                        .setAction("Add Account", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                navController.navigate(R.id.action_accountDisplayFragment_to_addAccountQqFragment);
                            }
                        })
                        .show();
            }
            AccountResultsMailQqAdapter adapter = new AccountResultsMailQqAdapter(this.getContext(), R.layout.account_results, mailqq);
            ListView listView = (ListView) view.findViewById(R.id.accountResults_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ //监听每个点击listview的事件
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Mailqq mail = mailqq.get(position);
                    Snackbar.make(view, "Are you sure to delete the account?", Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LitePal.delete(Mailqq.class, mail.getId());
                            //更新listView的适配器，来更新界面数据
                            mailqq = LitePal.findAll(Mailqq.class);
                            AccountResultsMailQqAdapter adapter = new AccountResultsMailQqAdapter(getContext(), R.layout.account_results, mailqq);
                            listView.setAdapter(adapter);
                        }
                    }).show();
                }
            });
        }
        Button button_to_add = view.findViewById(R.id.button_to_add_account);
        button_to_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mailType==1){
                    navController.navigate(R.id.action_accountDisplayFragment_to_addAccount163Fragment);
                }else{
                    navController.navigate(R.id.action_accountDisplayFragment_to_addAccountQqFragment);
                }

            }
        });
    }
}