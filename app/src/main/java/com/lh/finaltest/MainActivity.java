package com.lh.finaltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static String userid=null;
    public static String getUserid() {
        return userid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        NavController controller= Navigation.findNavController(this,R.id.fragment);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,controller,appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView,controller);

        userid=getIntent().getStringExtra("id");
    }
}
