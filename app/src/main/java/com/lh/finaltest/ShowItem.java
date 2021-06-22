package com.lh.finaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.lh.finaltest.databinding.ActivityShowItemBinding;
import com.lh.finaltest.db.Entity.Charge;
import com.lh.finaltest.db.Service.Service;

public class ShowItem extends AppCompatActivity {

    private Service service;
    private String chargeid;
    private ActivityShowItemBinding binding;
    private Charge charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_item);
        setTitle("账单详情");
        service = new Service(this);
        chargeid = getIntent().getStringExtra("chargeid");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_item);
        initCharge();

        binding.addChargeDate.setEnabled(false);

        binding.showItemSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setAbled(binding.showItemSwitch.isChecked());
            }
        });

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name=binding.addChargeName.getText().toString();
                String money=binding.addChargeMoney.getText().toString();
                binding.addChargeBtnAdd.setEnabled(!name.isEmpty() &&(money.matches("^[1-9].*$")));
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
                Charge charge1=new Charge();
                charge1.setMoney(binding.addChargeMoney.getText().toString());
                charge1.setName(binding.addChargeName.getText().toString());
                charge1.setRemark(binding.addChargeRemark.getText().toString());
                charge1.setDate(binding.addChargeDate.getText().toString());
                charge1.setType(binding.addChargeTypeIn.isChecked()?"收入":"支出");
                charge1.setId(chargeid);
                service.updateChargeItem(charge1);
                Toast.makeText(ShowItem.this, "修改成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowItem.this,MainActivity.class).putExtra("id",charge.getUserid()));
            }
        });
    }

    private void initCharge() {
        charge = service.showChargeItem(chargeid);
        binding.addChargeMoney.setText(charge.getMoney());
        binding.addChargeName.setText(charge.getName());
        binding.addChargeRemark.setText(charge.getRemark());
        if (charge.getType().equals("收入"))
            binding.addChargeTypeIn.setChecked(true);
        else binding.addChargeTypeOut.setChecked(true);
        binding.addChargeDate.setText(charge.getDate());
        setAbled(false);
    }

    private void setAbled(boolean flag) {
        binding.addChargeMoney.setEnabled(flag);
        binding.addChargeName.setEnabled(flag);
        binding.addChargeRemark.setEnabled(flag);
        binding.addChargeTypeIn.setEnabled(flag);
        binding.addChargeTypeOut.setEnabled(flag);
        if (flag) {
            binding.addChargeBtnAdd.setVisibility(View.VISIBLE);
            binding.showItemTip.setText("修改模式");
        }
        else {
            binding.addChargeBtnAdd.setVisibility(View.GONE);
            binding.showItemTip.setText("浏览模式");
        }
    }
}
