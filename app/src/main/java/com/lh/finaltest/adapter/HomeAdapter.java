package com.lh.finaltest.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lh.finaltest.R;
import com.lh.finaltest.ShowItem;
import com.lh.finaltest.db.Entity.Charge;
import com.lh.finaltest.db.Service.Service;
import com.lh.finaltest.ui.Home.HomeViewModel;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {


    private HomeViewModel viewModel;
    private List<Charge> list=null;
    private Service service;
    private boolean isSetting=false;    //判断出现复选框

    private static StringBuilder builder=new StringBuilder();


    public void setSetting(boolean setting) {
        isSetting = setting;
    }


    public HomeAdapter(HomeViewModel viewModel) {
       this.viewModel=viewModel;
       this.list=viewModel.getLiveData().getValue();
    }

    public void setList(List<Charge> list) {
        this.list = list;
    }

    public static void setCheckChargeId(String id,boolean flag){
        if(flag)
            builder.append(id).append(";");
        else{
            builder.replace(builder.indexOf(id),builder.indexOf(id)+id.length()+1,"");
            Log.d("Mytest",builder.toString());
        }
    }

    public static void setBuilderLength(){
        builder.setLength(0);
    }

    public static String getCheckedChargeId(){
        return builder.toString();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View item=inflater.inflate(R.layout.cell_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        if(list!=null) {
            final Charge charge = list.get(position);
            holder.number.setText(String.valueOf(position+1));
            holder.type.setText(charge.getType());
            holder.name.setText(charge.getName());
            holder.date.setText(charge.getDate());
            if(isSetting) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.checkBox.isChecked())
                            setCheckChargeId(charge.getId(),true);
                        else
                            setCheckChargeId(charge.getId(),false);
                    }
                });
            }
            else holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {    //进入账单详情
                    holder.itemView.getContext()
                            .startActivity(new Intent(holder.itemView.getContext(), ShowItem.class).putExtra("chargeid",charge.getId()));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {    //长按出现菜单
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu=new PopupMenu(holder.itemView.getContext(),v);
                    popupMenu.getMenuInflater().inflate(R.menu.delete_tip_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            service=new Service(holder.itemView.getContext());
                            switch (item.getItemId()){
                                case R.id.delete_tip_yes:
                                    service.deleteCahrgeItem(charge.getId());
                                    viewModel.setLiveData(service.showAllCharge(charge.getUserid()));
                                    Toast.makeText(holder.itemView.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    break;
                                default:break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView number,date,name,type;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.charge_name);
            date=itemView.findViewById(R.id.charge_date);
            type=itemView.findViewById(R.id.charge_type);
            number=itemView.findViewById(R.id.charge_number);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
