package com.lh.finaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.lh.finaltest.databinding.ActivityAddChargeBinding;
import com.lh.finaltest.db.Entity.Charge;
import com.lh.finaltest.db.Service.Service;

public class AddCharge extends AppCompatActivity {

    private ActivityAddChargeBinding binding;
    private Service service;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_charge);
        setTitle("添加账单");
        userid=getIntent().getStringExtra("id");
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_charge);
        binding.addChargeBtnAdd.setEnabled(false);
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name=binding.addChargeName.getText().toString().trim();
                String money=binding.addChargeMoney.getText().toString().trim();
                binding.addChargeBtnAdd.setEnabled(!name.isEmpty() && (money.matches("^[1-9][0-9]*$")));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.addChargeName.addTextChangedListener(textWatcher);
        binding.addChargeMoney.addTextChangedListener(textWatcher);

        binding.addChargeBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service=new Service(AddCharge.this);
                Charge charge=new Charge();
                charge.setUserid(userid);
                charge.setType(binding.addChargeTypeIn.isChecked()?"收入":"支出");
                charge.setRemark(binding.addChargeRemark.getText().toString());
                charge.setName(binding.addChargeName.getText().toString());
                charge.setMoney(binding.addChargeMoney.getText().toString());
                service.insertCharge(charge);
                Toast.makeText(AddCharge.this, "添加成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddCharge.this,MainActivity.class).putExtra("id",userid));
            }
        });
    }
}
