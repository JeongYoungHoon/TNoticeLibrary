/*
 * Copyright (c) 2015. WeNoun™. TANSAN, Since 2014.
 * Code By Jey.
 */

package com.wenoun.library.notice.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ScrollView;

import com.wenoun.library.notice.R;
import com.wenoun.library.notice.TNotice;
import com.wenoun.library.notice.TNoticeConst;
import com.wenoun.library.notice.db.DB;


/**
 * Created by SnakeJey on 2015-01-27.
 */
public class TNoticeActivity extends Activity {
    private Context ctx=null;
    private ScrollView scrollView=null;
    private String appName="";
    private Dialog dialog=null;
    private BroadcastReceiver noticeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TNoticeConst.Action.GET_NOTICE_FINISH)){
                scrollView.addView(DB.Notice.getView(ctx));
                dismissDialog();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        if(getIntent().hasExtra(TNoticeConst.Key.APP_NAME)){
            appName=getIntent().getExtras().getString(TNoticeConst.Key.APP_NAME);
        }else{
            finish();
        }
        setContentView(R.layout.notice_layout);
        scrollView=(ScrollView)findViewById(R.id.notice_sv);
        showDialog();
        TNotice.startService(ctx,appName);

    }
    public Dialog getProgressDialog() {
        Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    public void dismissDialog(){
        if(null!=dialog)
            dialog.dismiss();
    }
    public void showDialog(){
        dialog=getProgressDialog();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(noticeReceiver,new IntentFilter(TNoticeConst.Action.GET_NOTICE_FINISH));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(noticeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void goMain(View v){
        finish();
    }

}
