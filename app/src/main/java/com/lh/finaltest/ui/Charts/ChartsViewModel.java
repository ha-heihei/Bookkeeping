package com.lh.finaltest.ui.Charts;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChartsViewModel extends ViewModel {

    private static ChartsViewModel INSTANCE;
    public static ChartsViewModel getINSTANCE(Fragment fragment){
        if (INSTANCE==null){
            INSTANCE=new ViewModelProvider(fragment).get(ChartsViewModel.class);
        }
        return INSTANCE;
    }

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void setResult(String result){
        getResult().setValue(result);
    }

    private MutableLiveData<String> result=new MutableLiveData<>();
    private MutableLiveData<String[]>  names=new MutableLiveData<>();
    private MutableLiveData<Double[]>  datas=new MutableLiveData<>();

    public MutableLiveData<String[]> getNames() {
        return names;
    }

    public MutableLiveData<Double[]> getDatas() {
        return datas;
    }

    public void setNames(String names[]){
        getNames().setValue(names);
    }

    public void setDatas(Double datas[]){
        getDatas().setValue(datas);
    }

    // TODO: Implement the ViewModel
}
