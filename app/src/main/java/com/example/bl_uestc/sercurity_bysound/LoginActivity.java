package com.example.bl_uestc.sercurity_bysound;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.account)
    EditText account;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.btn_login)
    Button login;

    @Bind(R.id.btn_forget)
    TextView forget;

    @Bind(R.id.btn_register)
    TextView register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
