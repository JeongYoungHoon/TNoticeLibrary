package com.wenoun.library.notice.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.wenoun.library.notice.activity.TNoticeActivity;

/**
 * Created by jeyhoon on 16. 3. 3..
 */
public class BaseFragment extends Fragment {
    protected Context ctx=null;
    protected TNoticeActivity parent=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=getActivity().getBaseContext();
        parent=(TNoticeActivity)getActivity();
    }
}
