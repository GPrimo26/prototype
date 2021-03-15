package com.sabirov.warehouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.sabirov.warehouse.R;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import userauth.AuthorizationGrpc;
import userauth.LoginRequest;
import userauth.Response;
import userauth.User;

import static com.sabirov.warehouse.Variables.ip;


public class AuthActivity extends AppCompatActivity {

    private EditText uid_et, password_et;
    private TextView pass_tv;
    private Button enter_btn;

    public interface OnTaskCompleted {
        void onTaskCompleted(User user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        findIDs();
        setActions();
        uid_et.requestFocus();

    }

    private void findIDs() {
        uid_et = findViewById(R.id.uid_et);
        password_et = findViewById(R.id.password_et);
        pass_tv = findViewById(R.id.pass_tv);
        enter_btn = findViewById(R.id.enter_btn);
    }

    private void setActions() {
        uid_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    pass_tv.setVisibility(View.VISIBLE);
                    password_et.setVisibility(View.VISIBLE);
                } else {
                    pass_tv.setVisibility(View.GONE);
                    password_et.setVisibility(View.GONE);
                    if (enter_btn.getVisibility() == View.VISIBLE) {
                        enter_btn.setVisibility(View.GONE);
                    }
                    password_et.setText("");
                }
            }
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    enter_btn.setVisibility(View.VISIBLE);
                } else {
                    enter_btn.setVisibility(View.GONE);
                }
            }
        });
        enter_btn.setOnClickListener(view -> {
            OnTaskCompleted onTaskCompleted = user_ -> {
                com.sabirov.warehouse.models.User user =
                        new com.sabirov.warehouse.models.User(user_.getUserId(), user_.getEmail(),
                                user_.getPhoneNumber(), user_.getFirstName(), user_.getLastName());
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            };
            String[] strings=new String[2];
            strings[0]=uid_et.getText().toString();
            strings[1]= password_et.getText().toString();
            new Auth(onTaskCompleted).execute(strings);
        });
    }


}

class Auth extends AsyncTask<String, Void, User>{
    Auth(AuthActivity.OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted=onTaskCompleted;
    }

    private AuthActivity.OnTaskCompleted onTaskCompleted;

    @Override
    protected User doInBackground(String... strings) {
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, 5067)
                    .usePlaintext().build();
            AuthorizationGrpc.AuthorizationBlockingStub stub = AuthorizationGrpc
                    .newBlockingStub(channel);
            LoginRequest request = LoginRequest.newBuilder().setUserName(strings[0])
                    .setPassword(strings[1]).build();
            Response response = stub.logIn(request);

            channel.shutdownNow();
            return response.getUser();
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user!=null) {
            onTaskCompleted.onTaskCompleted(user);
        }
    }
}

