package com.wenoun.library.notice.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wenoun.library.notice.GetJSONThread;
import com.wenoun.library.notice.R;
import com.wenoun.library.notice.data.Noti;
import com.wenoun.library.notice.db.DB;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jeyhoon on 16. 3. 3..
 */
public class RealtimeList extends BaseFragment {
    private Dialog dialog=null;
    private static View root;
    private ListView listView;
    protected ArrayList<Noti> arrlist = new ArrayList<Noti>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            root=inflater.inflate(R.layout.fragment_list,container,false);
        }catch(Exception e){

        }
        listView=(ListView)root.findViewById(R.id.list_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, int position, long id) {
                if(arrlist.size()>0){
                    parent.showDetail(arrlist.get(position).getTitle(),arrlist.get(position).getNo());
                }
            }
        });
        showDialog();
        Handler h = new Handler();
        h.postDelayed(new versioncheck_hendler(), 0);
        return inflater.inflate(R.layout.fragment_list,container,false);
    }
    public Dialog getProgressDialog() {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
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
    public void getNotice() {
        String tmp = parent.getAppName();
        String url = "http://noti.wenoun.com/select_noti.php?app_name=" + tmp;
        // Toast.makeText(SplashActivity.this, "ss", 0).show();
        // 스레드 객체를 생성해서 작업을 시킨다.
        GetJSONThread thread = new GetJSONThread(new Handler() {
            public void handleMessage(android.os.Message msg) {
                // 전송결과를 메시지로 받는다
                switch (msg.what) {
                    case 0: // 성공
                        String result1 = (String) msg.obj;
                        dismissDialog();
                        break;
                    case 1: // 실패
                        String result2 = (String) msg.obj;
                        dismissDialog();
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
                                listView.setAdapter(DB.Notice.getTitleView(ctx));
//                                scrollView.addView(DB.Notice.getView(ctx));
                                //Adapter=new NotiDataAdapter(ctx,arrlist);
                                //listView.setAdapter(Adapter);
                            } else {


                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        dismissDialog();
                        break;
                    case 3: // getJSON실패
                        String jsonStr2 = (String) msg.obj;
                        dismissDialog();
                        break;
                }
            }
        }, null, url);
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
