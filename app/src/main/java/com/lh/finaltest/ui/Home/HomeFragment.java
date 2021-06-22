package com.lh.finaltest.ui.Home;

import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.lh.finaltest.AddCharge;
import com.lh.finaltest.MainActivity;
import com.lh.finaltest.R;
import com.lh.finaltest.adapter.HomeAdapter;
import com.lh.finaltest.db.Entity.Charge;
import com.lh.finaltest.db.Service.Service;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private Service service;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<Charge> list;
    private Menu mymenu;

    private String userid;

    public HomeFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_nav_menu,menu);

        mymenu=menu;                //变量全局化

        SearchView searchView=(SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mViewModel.setLiveData(service.showQueryCahrge(userid,newText));
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_add:
                startActivity(new Intent(requireActivity(),AddCharge.class).putExtra("id",userid));
                break;
            case R.id.app_bar_setting:
                if(item.getTitle().equals("编辑账单")){
                    adapter.setSetting(true);
                    mViewModel.setLiveData(service.showAllCharge(userid));
                    item.setTitle("取消编辑");
                    mymenu.findItem(R.id.app_bar_delete).setVisible(true);
                }
                else{
                    adapter.setSetting(false);
                    mViewModel.setLiveData(service.showAllCharge(userid));
                    HomeAdapter.setBuilderLength();
                    item.setTitle("编辑账单");
                    mymenu.findItem(R.id.app_bar_delete).setVisible(false);
                }
                break;
            case R.id.app_bar_delete:
                String str[]=HomeAdapter.getCheckedChargeId().split(";");
                for (String s : str) {
                    service.deleteCahrgeItem(s);
                }
                HomeAdapter.setBuilderLength();
                adapter.setSetting(false);
                mymenu.findItem(R.id.app_bar_setting).setTitle("编辑账单");
                mymenu.findItem(R.id.app_bar_delete).setVisible(false);
                mViewModel.setLiveData(service.showAllCharge(userid));
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userid=MainActivity.getUserid();
        service=new Service(getContext());
        this.list=service.showAllCharge(this.userid);
        mViewModel = HomeViewModel.getINSTANCE(this);
        mViewModel.setLiveData(this.list);
        recyclerView =requireActivity().findViewById(R.id.home_cycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter=new HomeAdapter(mViewModel);

        adapter.setSetting(false);   //初始不显示复选框

        recyclerView.setAdapter(adapter);
        mViewModel.getLiveData().observe(requireActivity(), new Observer<List<Charge>>() {
            @Override
            public void onChanged(List<Charge> charges) {
                adapter.setList(charges);
                adapter.notifyDataSetChanged();
            }
        });


        // TODO: Use the ViewModel
    }

}
