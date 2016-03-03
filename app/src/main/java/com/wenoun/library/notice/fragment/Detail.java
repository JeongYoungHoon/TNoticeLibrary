package com.wenoun.library.notice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenoun.library.notice.R;
import com.wenoun.library.notice.db.DB;

/**
 * Created by jeyhoon on 16. 3. 3..
 */
public class Detail extends BaseFragment {
    private static View root=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            root=inflater.inflate(R.layout.fragment_empty,container,false);
        }catch(Exception e){}
        if(parent.getNotiNo()!=-1){
            ((TextView)root.findViewById(R.id.detail_msg)).setText(DB.Notice.getMsgFromNo(ctx,parent.getNotiNo()));
        }
        return root;
    }
}
