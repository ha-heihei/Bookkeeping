package com.lh.finaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lh.finaltest.databinding.ActivityRegsiterBinding;
import com.lh.finaltest.db.Entity.User;
import com.lh.finaltest.db.Service.Service;

public class Regsiter extends AppCompatActivity {

    private ActivityRegsiterBinding binding;
    private Service service=new Service(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_regsiter);
        setTitle("注册页面");
        binding= DataBindingUtil.setContentView(this,R.layout.activity_regsiter);
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.editName.getText().toString();
                String pwd=binding.editPwd.getText().toString();
                if (service.checkUserExist(name,null,false)){
                    Toast.makeText(Regsiter.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                }else {
                    service.insertUser(new User(name,pwd));
                    Toast.makeText(Regsiter.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Regsiter.this, Login.class));
                }
            }
        });
    }
}
