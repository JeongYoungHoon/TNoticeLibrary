package com.wenoun.library.notice.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wenoun.library.notice.R;
import com.wenoun.library.notice.TNotice;
import com.wenoun.library.notice.TNoticeConst;
import com.wenoun.library.notice.data.NotiDataAdapter;
import com.wenoun.library.notice.db.DB;

/**
 * Created by jeyhoon on 16. 3. 3..
 */
public class List extends BaseFragment {
    private Dialog dialog=null;
    private static View root;
    private ListView listView;
    private NotiDataAdapter Adapter=null;
    private BroadcastReceiver noticeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TNoticeConst.Action.GET_NOTICE_FINISH)){
//                scrollView.addView(DB.Notice.getView(ctx));
                Adapter=DB.Notice.getTitleView(ctx);
                listView.setAdapter(Adapter);
                dismissDialog();
            }
        }
    };
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
                if(null!=Adapter){
                    parent.showDetail(Adapter.getItem(position).getTitle(),Adapter.getItem(position).getNo());
                }
            }
        });
        showDialog();
        TNotice.startService(getActivity(),parent.getAppName());
        return root;
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
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(noticeReceiver,new IntentFilter(TNoticeConst.Action.GET_NOTICE_FINISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(noticeReceiver);
    }


}
