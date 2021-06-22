package com.lh.finaltest.ui.Home;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lh.finaltest.db.Entity.Charge;

import java.util.List;

public class HomeViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private  static HomeViewModel INSTANCE=null;
    public static HomeViewModel getINSTANCE(Fragment fragment){
        if(INSTANCE==null){
          INSTANCE=new ViewModelProvider(fragment).get(HomeViewModel.class);
        }
        return INSTANCE;
    }

    private MutableLiveData<List<Charge>> liveData=new MutableLiveData<>();

    public MutableLiveData<List<Charge>> getLiveData(){
        return this.liveData;
    }

    public void setLiveData(List<Charge> list){
        getLiveData().setValue(list);
    }
}
