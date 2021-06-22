package com.lh.finaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lh.finaltest.databinding.ActivityLoginBinding;
import com.lh.finaltest.db.Service.Service;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Service service=new Service(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        setTitle("登陆界面");
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.editName.getText().toString();
                String pwd=binding.editPwd.getText().toString();
                if(service.checkUserExist(name,pwd,true)){
                    Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class).putExtra("id",service.getUserId(name)));
                }else
                    Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Regsiter.class));
            }
        });
    }
}
