package com.lh.finaltest.db.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lh.finaltest.db.DBUtil.DBUtil;
import com.lh.finaltest.db.Entity.Charge;
import com.lh.finaltest.db.Entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service {

    private final String model="1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private DBUtil dbUtil=null;

    public Service(Context context) {          //获取数据库连接
        this.dbUtil = new DBUtil(context,"charge_db",null,1);
    }

    private String getDateString(boolean flag){      //获取日期
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date=new Date();
        if(flag)
            return sdf.format(date);
        else return sdf2.format(date);
    }

    private String getIdentity(){        //获取编码
        StringBuilder info=new StringBuilder();
        for(int i=0;i<10;i++)
            info.append(model.charAt((int)(Math.random()*100)%62));
        return info.append(getDateString(false)).toString();
    }

    public void insertUser(User user){    //用户注册
        ContentValues values=new ContentValues();
        values.put("id",getIdentity());
        values.put("name",user.getName());
        values.put("pwd",user.getPwd());
        SQLiteDatabase db=dbUtil.getReadableDatabase();
        db.insert("user",null,values);
        db.close();
    }

    public boolean checkUserExist(String name,String pwd,boolean type){   //判断用户是否存在
        boolean flag=false;
        Cursor cursor=null;
        String sql=null;
        SQLiteDatabase db=dbUtil.getReadableDatabase();
        if(type) {
            sql= "select * from user where name=? and pwd=?";
            cursor= db.rawQuery(sql, new String[]{name, pwd});
        }else {
            sql="select * from user where name=?";
            cursor=db.rawQuery(sql,new String[]{name});
        }
        if(cursor.moveToFirst())
            flag=true;
        db.close();
        cursor.close();
        return flag;
    }

    public String getUserId(String name){   //获取用户id
        String str="";
        String sql="select id from user where name=?";
        SQLiteDatabase db=dbUtil.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,new String[]{name});
        if (cursor.moveToFirst())
            str=cursor.getString(cursor.getColumnIndex("id"));
        cursor.close();
        db.close();
        return str;
    }

    public void insertCharge(Charge charge){     //账单增加
        ContentValues values=new ContentValues();
        values.put("id",getIdentity());
        values.put("name",charge.getName());
        values.put("type",charge.getType());
        values.put("money",charge.getMoney());
        values.put("remark",charge.getRemark());
        values.put("date",getDateString(true));
        values.put("userid",charge.getUserid());
        SQLiteDatabase db=dbUtil.getReadableDatabase();
        db.insert("charge",null,values);
        db.close();
    }

    public List<Charge> showAllCharge(String userid){    //获取用户账目
        String sql="select * from charge where userid=? order by date desc";
        List<Charge> list=new ArrayList<>();
        if(dbUtil!=null) {
            SQLiteDatabase db = dbUtil.getWritableDatabase();
            Cursor cursor=db.rawQuery(sql,new String[]{userid});
            while (cursor.moveToNext()){
                Charge charge=new Charge();
                charge.setDate(cursor.getString(cursor.getColumnIndex("date")));
                charge.setId(cursor.getString(cursor.getColumnIndex("id")));
                charge.setMoney(cursor.getString(cursor.getColumnIndex("money")));
                charge.setName(cursor.getString(cursor.getColumnIndex("name")));
                charge.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                charge.setType(cursor.getString(cursor.getColumnIndex("type")));
                charge.setUserid(userid);
                list.add(charge);
            }
            cursor.close();
            db.close();
        }
        return list;
    }

    public List<Charge> showQueryCahrge(String userid,String key){  //条件查询
        String sql="select * from charge where userid=? and (date like '%"+key+"%' or name like '%"+key+"%')";
        List<Charge> list=new ArrayList<>();
        if(dbUtil!=null) {
            SQLiteDatabase db = dbUtil.getWritableDatabase();
            Cursor cursor=db.rawQuery(sql,new String[]{userid});
            while (cursor.moveToNext()){
                Charge charge=new Charge();
                charge.setDate(cursor.getString(cursor.getColumnIndex("date")));
                charge.setId(cursor.getString(cursor.getColumnIndex("id")));
                charge.setMoney(cursor.getString(cursor.getColumnIndex("money")));
                charge.setName(cursor.getString(cursor.getColumnIndex("name")));
                charge.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                charge.setType(cursor.getString(cursor.getColumnIndex("type")));
                charge.setUserid(userid);
                list.add(charge);
            }
            cursor.close();
            db.close();
        }
        return list;
    }

    public Charge showChargeItem(String id){     //获取单个账单
        String sql="select * from charge where id='"+id+"'";
        Charge charge=new Charge();
        SQLiteDatabase db=dbUtil.getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            charge.setDate(cursor.getString(cursor.getColumnIndex("date")));
            charge.setId(cursor.getString(cursor.getColumnIndex("id")));
            charge.setMoney(cursor.getString(cursor.getColumnIndex("money")));
            charge.setName(cursor.getString(cursor.getColumnIndex("name")));
            charge.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
            charge.setType(cursor.getString(cursor.getColumnIndex("type")));
            charge.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
        }
        return charge;
    }

    public void updateChargeItem(Charge charge){       //修改账单
        String sql="update charge set name='"+charge.getName()+"',type='"+charge.getType()+"'," +
                "remark='"+charge.getRemark()+"',date='"+charge.getDate()+"',money='"+charge.getMoney()+"' where id='"+charge.getId()+"'";
        SQLiteDatabase db=dbUtil.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void deleteCahrgeItem(String chargeid){    //删除账单
        String sql="delete from charge where id='"+chargeid+"'";
        SQLiteDatabase db=dbUtil.getWritableDatabase();
        db.execSQL(sql);
    }

    public String chartsData(String begindate,String enddate,String userid){
        String tempstr="";
        StringBuilder builder=new StringBuilder();
        String sql="select date from charge where userid='"+userid+"' and (date>='"+begindate+" 00:00:00' and date<='"+enddate+" 23:59:59')";
        SQLiteDatabase db=dbUtil.getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);

        while (cursor.moveToNext()) {
            if(!tempstr.contains(cursor.getString(cursor.getColumnIndex("date")).split(" ")[0]))
                 tempstr += cursor.getString(cursor.getColumnIndex("date")).split(" ")[0] + ";";
        }
        cursor.close();

        String str[]=tempstr.split(";");
        for (int i=0;i<str.length;i++){
            builder.append("{date:'").append(str[i]+"',");
            sql="select sum(money) from charge where userid='"+userid+"' and type='收入' and date like '"+str[i]+"%'";
            cursor=db.rawQuery(sql,null);
            cursor.moveToFirst();
            builder.append("chargein:'"+cursor.getString(0)).append("',");
            cursor.close();
            sql="select sum(money) from charge where userid='"+userid+"' and type='支出' and date like '"+str[i]+"%'";
            cursor=db.rawQuery(sql,null);
            cursor.moveToFirst();
            builder.append("chargeout:'"+cursor.getString(0)).append("',").append("day:[");
            cursor.close();

            sql="select * from charge where userid='"+userid+"' and date like '"+str[i]+"%'";
            cursor=db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                builder.append("{").append("name:'"+cursor.getString(cursor.getColumnIndex("name")))
                        .append("',type:'"+cursor.getString(cursor.getColumnIndex("type")))
                        .append("',money:'"+cursor.getString(cursor.getColumnIndex("money"))).append("'},");
            }
            builder.replace(builder.lastIndexOf(","),builder.lastIndexOf(",")+1,"");
            builder.append("]},");
            cursor.close();
        }
        builder.replace(builder.lastIndexOf(","),builder.lastIndexOf(",")+1,"");
        db.close();
        return "["+builder.toString()+"]";
    }

    public String getAllChargeMoney(String begindate,String enddate,String userid){    //获取用户总收入支出
        String chargein="",chargeout="";
        String sql="";
        SQLiteDatabase db=dbUtil.getWritableDatabase();
        sql="select sum(money) from charge where userid='"+userid+"' and (date>='"+begindate+" 00:00:00' and date<='"+enddate+" 23:59:59') and type='支出'";
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst())
            chargeout=cursor.getString(0);
        cursor.close();
        sql="select sum(money) from charge where userid='"+userid+"' and (date>='"+begindate+" 00:00:00' and date<='"+enddate+" 23:59:59') and type='收入'";
        cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst())
            chargein=cursor.getString(0);
        cursor.close();
        db.close();
        return chargein+";"+chargeout;
    }

}
