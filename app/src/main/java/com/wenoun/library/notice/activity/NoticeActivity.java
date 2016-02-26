/*
 * Copyright (c) 2015. WeNoun™. TANSAN, Since 2014.
 * Code By Jey.
 */

package com.wenoun.library.notice.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.wenoun.library.notice.GetJSONThread;
import com.wenoun.library.notice.R;
import com.wenoun.library.notice.TNoticeConst;
import com.wenoun.library.notice.data.Noti;
import com.wenoun.library.notice.db.DB;

import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by SnakeJey on 2015-01-27.
 */
public class NoticeActivity extends Activity {
    protected Context ctx = null;
    protected String app_name="";
    //private ListView listView=null;
    //private NotiDataAdapter Adapter=null;
    protected ArrayList<Noti> arrlist = new ArrayList<Noti>();
    protected ScrollView scrollView = null;
    protected Dialog dialog = null;
    public Handler vHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 전송결과를 메시지로 받는다
            switch (msg.what) {
                case 0: // 성공
                    String result1 = (String) msg.obj;
                    if (null != dialog)
                        break;
                case 1: // 실패
                    String result2 = (String) msg.obj;
                    if (null != dialog)
                        dialog.dismiss();
                    break;
                case 2: // getJSON성공

                    String jsonStr1 = (String) msg.obj;
                    jsonStr1 = jsonStr1.replace("]", "");
                    jsonStr1 = jsonStr1.replace("[", "");

                    // Toast.makeText(SplashActivity.this, jsonStr1, 0).show();
                    try {

                        String result;
                        JSONObject jsonObj = new JSONObject(jsonStr1);
                        // JSONArray 객체 얻어오기
                        result = jsonObj.getString("result");
                        Log.d("NotiAct", jsonObj.toString() + "");
                        //JSONArray jsonArray=new JSONArray(jsonStr1);
                        if (result.equals("FINISH")) {
                            for (int i = 0; i < jsonObj.length() - 1; i++) {
                                JSONObject obj = jsonObj.getJSONObject(i + "");
                                int no;
                                String title, notiMsg, date;
                                no = obj.getInt("no");
                                title = obj.getString("title");
                                notiMsg = obj.getString("msg");
                                date = obj.getString("date");
                                arrlist.add(new Noti(no, title, notiMsg, date));
                            }
                            DB.Notice.insertSyncNotiArray(ctx, arrlist);
                            scrollView.addView(DB.Notice.getView(ctx));
                            //Adapter=new NotiDataAdapter(ctx,arrlist);
                            //listView.setAdapter(Adapter);
                        } else {


                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (null != dialog)
                        dialog.dismiss();
                    break;
                case 3: // getJSON실패
                    String jsonStr2 = (String) msg.obj;
                    if (null != dialog)
                        dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.notice_layout);
        if(app_name.equals("")||app_name.length()<=0) {
            if (getIntent().hasExtra(TNoticeConst.Key.APP_NAME)) {
                setAppName(getIntent().getExtras().getString(TNoticeConst.Key.APP_NAME));
            }else{
                finish();
            }
        }
        //listView=(ListView)findViewById(R.id.noti_lv);
        scrollView = (ScrollView) findViewById(R.id.notice_sv);
        Handler h = new Handler();
        h.postDelayed(new versioncheck_hendler(), 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=dialog){
            dialog.dismiss();
        }
    }

    public void goMain(View v) {
        finish();
    }

    public Dialog getProgressDialog() {
        Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    public void setAppName(String app_name){
        this.app_name=app_name;
    }
    public void getNotice() {
        String tmp = app_name;
        String url = "http://noti.wenoun.com/select_noti.php?app_name=" + tmp;
        // Toast.makeText(SplashActivity.this, "ss", 0).show();
        // 스레드 객체를 생성해서 작업을 시킨다.
        GetJSONThread thread = new GetJSONThread(vHandler, null, url);
        thread.start();
    }

    class versioncheck_hendler implements Runnable {
        public void run() {
            /*dialog = new ProgressDialog(SplashActivity.this);
            dialog.setTitle("버전체크중...");
			dialog.setMessage("잠시만 기다려 주세요...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);*/

            //splashIv.startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.rear_alpha));
            //splashIv.setImageResource(R.drawable.splash);
            /*logoIv.setVisibility(View.GONE);
            Animation frontAni=AnimationUtils.loadAnimation(ctx,R.anim.front_alpha);
            splashIv.startAnimation(frontAni);*/
            dialog = getProgressDialog();
            dialog.show();
            new Thread(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub


                    try {
                        Thread.sleep(50);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                    getNotice();
                    //CheckVersion();


                }
            }).start();
            // Toast.makeText(SplashActivity.this, InLogin[0]+"::"+InLogin[1],
            // 0).show();

        }
    }
}
