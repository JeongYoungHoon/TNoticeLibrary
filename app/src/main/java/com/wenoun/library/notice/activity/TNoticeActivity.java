/*
 * Copyright (c) 2015. WeNoun™. TANSAN, Since 2014.
 * Code By Jey.
 */

package com.wenoun.library.notice.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wenoun.library.notice.R;
import com.wenoun.library.notice.TNoticeConst;
import com.wenoun.library.notice.fragment.Detail;
import com.wenoun.library.notice.fragment.List;
import com.wenoun.library.notice.fragment.RealtimeList;


/**
 * Created by SnakeJey on 2015-01-27.
 */
public class TNoticeActivity extends Activity {
    private Context ctx=null;
//    private ScrollView scrollView=null;
    private String appName="";
    private int notiNo=-1;
    private boolean isRealtime=false;
    private FragmentManager fragmentManager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        if(getIntent().hasExtra(TNoticeConst.Key.APP_NAME)){
            appName=getIntent().getExtras().getString(TNoticeConst.Key.APP_NAME);
        }else{
            finish();
        }
        if(getIntent().hasExtra(TNoticeConst.Key.IS_REALTIME)){
            isRealtime=getIntent().getExtras().getBoolean(TNoticeConst.Key.IS_REALTIME);
        }
        setContentView(R.layout.layout_notice);
        fragmentManager = getFragmentManager();
        if(isRealtime)
            setFragment(new RealtimeList(),getString(R.string.notice));
        else
            setFragment(new List(),getString(R.string.notice));
//        scrollView=(ScrollView)findViewById(R.id.notice_sv);
//        showDialog();


    }
    public String getAppName(){
        return this.appName;
    }
    public int getNotiNo(){
        return this.notiNo;
    }
    public void setFragment(Fragment fragment){
        setFragment(fragment,"");
    }
    public void setFragment(Fragment fragment,String title){
        setTitle(title);
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notice_fragment, fragment);
        fragmentTransaction.commit();
    }
    public void addFragment(Fragment fragment, String title){
        setTitle(title);
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notice_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void addFragment(Fragment fragment){
        addFragment(fragment,"");
    }
    public void showDetail(String title,int idx){
        notiNo=idx;
        addFragment(new Detail(),title);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void goMain(View v){
        onBackPressed();
    }

    public void setTitle(String title){
        ((TextView)findViewById(R.id.notice_title)).setText(title);
    }
    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount()>0) {
            fragmentManager.popBackStack();
            try {
                setTitle(getString(R.string.notice));
                notiNo=-1;
            }catch(Exception e){}
        }else
            super.onBackPressed();
    }

}
