package com.lh.finaltest.ui.Charts;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lh.finaltest.MainActivity;
import com.lh.finaltest.R;
import com.lh.finaltest.db.Service.Service;

public class ChartsFragment extends Fragment {

    private ChartsViewModel mViewModel;

    private WebView webView;

    private Service service;

    private String userid;
    private Button search_btn;
    private RadioButton bar_btn,line_btn;
    private EditText begin_date,end_date;
    private String result;
    private TextView tip_in,tip_out;
    private String allcharge_in_out;

    public static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.charts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bar_btn=requireActivity().findViewById(R.id.charts_bar);
        line_btn=requireActivity().findViewById(R.id.charts_line);
        begin_date=requireActivity().findViewById(R.id.begin_date);
        end_date=requireActivity().findViewById(R.id.end_date);
        search_btn=requireActivity().findViewById(R.id.search_btn);
        tip_in=requireActivity().findViewById(R.id.charts_tip_in);
        tip_out=requireActivity().findViewById(R.id.charts_tip_out);

        userid= MainActivity.getUserid();
        service=new Service(getContext());

        webView=requireActivity().findViewById(R.id.webView);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/echarts.html");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bdate=begin_date.getText().toString();
                String edate=end_date.getText().toString();
                result=service.chartsData(bdate,edate,userid);
                allcharge_in_out=service.getAllChargeMoney(bdate,edate,userid);
                tip_in.setText("总收入:"+allcharge_in_out.split(";")[0]);
                tip_out.setText("总支出:"+allcharge_in_out.split(";")[1]);
                webView.loadUrl("javascript:createChart("+result+","+(bar_btn.isChecked()?"'bar'":"'line'")+");");
            }
        });

        line_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:createChart("+result+","+(bar_btn.isChecked()?"'bar'":"'line'")+");");
            }
        });

        bar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:createChart("+result+","+(bar_btn.isChecked()?"'bar'":"'line'")+");");
            }
        });

        // TODO: Use the ViewModel
    }

}
