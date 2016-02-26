package com.wenoun.library.notice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wenoun.library.notice.GetJSONThread;
import com.wenoun.library.notice.TNoticeConst;
import com.wenoun.library.notice.data.Noti;
import com.wenoun.library.notice.db.DB;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jeyhoon on 15. 10. 23..
 */
public class TNoticeService extends Service {
    private Context ctx=null;
    private String appName="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ctx=this;
        if(startId==1){
            if(intent.hasExtra(TNoticeConst.Key.APP_NAME)){
                appName=intent.getExtras().getString(TNoticeConst.Key.APP_NAME);
                checkNotice();
            }else{
                stopSelf();
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void checkNotice() {
        String url = "http://noti.wenoun.com/select_noti_max_no.php?app_name="+appName;
        // Toast.makeText(SplashActivity.this, "ss", 0).show();
        // 스레드 객체를 생성해서 작업을 시킨다.
        GetJSONThread thread = new GetJSONThread(new Handler(){
            public void handleMessage(android.os.Message msg){
                switch(msg.what){
                    case 2:
                        String jsonStr1 = (String) msg.obj;
                        jsonStr1 = jsonStr1.replace("]", "");
                        jsonStr1 = jsonStr1.replace("[", "");

                        // Toast.makeText(SplashActivity.this, jsonStr1, 0).show();
                        try {

                            String result;
                            JSONObject jsonObj = new JSONObject(jsonStr1);
                            // JSONArray 객체 얻어오기
                            result=jsonObj.getString("result");
                            Log.d("NotiAct", jsonObj.toString() + "");
                            //JSONArray jsonArray=new JSONArray(jsonStr1);
                            if(result.equals("FINISH")){
                                String maxNoStr=jsonObj.getString("max_no");
                                int maxNo=0;
                                try{
                                    maxNo= Integer.parseInt(maxNoStr);
                                }catch(NumberFormatException e){
                                    maxNo=0;
                                }
                                if(!DB.Notice.isNo(ctx,maxNo)){
                                    getNotice();
                                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent().setAction(TNoticeConst.Action.NEW_NOTICE));
//                                    Util.NEW.setNewNotice(ctx,true);
                                }else{
                                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent().setAction(TNoticeConst.Action.GET_NOTICE_FINISH));
                                    stopSelf();
                                }
//                                DB.Notice.insertSyncNotiArray(ctx, arrlist);
                                //Adapter=new NotiDataAdapter(ctx,arrlist);
                                //listView.setAdapter(Adapter);
                            } else {


                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }, null, url);
        thread.start();
    }
    public void getNotice() {
        String url = "http://noti.wenoun.com/select_noti.php?app_name="+appName;
        // Toast.makeText(SplashActivity.this, "ss", 0).show();
        // 스레드 객체를 생성해서 작업을 시킨다.
        GetJSONThread thread = new GetJSONThread(vHandler, null, url);
        thread.start();
    }
    private ArrayList<Noti> arrlist=new ArrayList<Noti>();
    public Handler vHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 전송결과를 메시지로 받는다
            switch (msg.what) {
                case 0: // 성공
                    String result1 = (String) msg.obj;
                case 1: // 실패
                    String result2 = (String) msg.obj;
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
                        result=jsonObj.getString("result");
                        Log.d("NotiAct", jsonObj.toString() + "");
                        //JSONArray jsonArray=new JSONArray(jsonStr1);
                        if(result.equals("FINISH")){
                            for(int i=0; i<jsonObj.length()-1;i++){
                                JSONObject obj=jsonObj.getJSONObject(i+"");
                                int no; String title,notiMsg,date;
                                no=obj.getInt("no");
                                title=obj.getString("title");
                                notiMsg=obj.getString("msg");
                                date=obj.getString("date");
                                arrlist.add(new Noti(no,title,notiMsg,date));
                            }
                            DB.Notice.insertSyncNotiArray(ctx, arrlist);
                            LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent().setAction(TNoticeConst.Action.GET_NOTICE_FINISH));
                            //Adapter=new NotiDataAdapter(ctx,arrlist);
                            //listView.setAdapter(Adapter);
                        } else {


                        }
                        stopSelf();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3: // getJSON실패
                    String jsonStr2 = (String) msg.obj;
                    break;
            }
            stopSelf();
        }
    };
}
