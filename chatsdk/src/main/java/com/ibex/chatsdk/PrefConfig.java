package com.ibex.chatsdk;

import static android.content.Context.MODE_PRIVATE;

import static com.ibex.chatsdk.Constatnts.CHAT_PREF;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {
    Context context;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit ;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CHAT_PREF,MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

    }

    public void save(String key, String value){
        if(!value.isEmpty()) {
            myEdit.putString(key, value);
            myEdit.commit();
            myEdit.apply();
        }
    }

    public void save(String key, int value){
        if(value!= 0 || value<=0) {
            myEdit.putInt(key, value);
            myEdit.commit();
            myEdit.apply();
        }
    }

    public String getdata(String key){
            return sharedPreferences.getString(key,"");
    }

    public int getData(String key){
        return  sharedPreferences.getInt(key,0);
    }

    public void clear(){
       myEdit.clear().commit();
   //    myEdit.apply();
    }

    public void removeObject(String key){
        myEdit.remove(key);
        myEdit.apply();
    }
}
