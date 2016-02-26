package com.wenoun.library.notice;

import android.content.Context;
import android.content.Intent;

import com.wenoun.library.notice.activity.TNoticeActivity;
import com.wenoun.library.notice.activity.TNoticeRealtimeActivity;
import com.wenoun.library.notice.service.TNoticeService;

/**
 * Created by jeyhoon on 16. 2. 26..
 */
public class TNotice {
    private Context ctx=null;
    private String appName="";
    public TNotice(Context ctx) {
        this.ctx = ctx;
    }

    public TNotice(Context ctx, String appName) {
        this.ctx = ctx;
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public TNotice setAppName(String appName) {
        this.appName = appName;
        return this;
    }
    public void startService(){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startService(new Intent(ctx, TNoticeService.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
    public void startActivity(){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startActivity(new Intent(ctx, TNoticeActivity.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
    public void startRealtimeActivity(){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startActivity(new Intent(ctx, TNoticeRealtimeActivity.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
    public static void startService(Context ctx,String appName){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startService(new Intent(ctx, TNoticeService.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
    public static void startActivity(Context ctx,String appName){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startActivity(new Intent(ctx, TNoticeActivity.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
    public static void startRealtimeActivity(Context ctx,String appName){
        if(appName.equals("")||appName.length()<=0)return ;
        else
            ctx.startActivity(new Intent(ctx, TNoticeRealtimeActivity.class).putExtra(TNoticeConst.Key.APP_NAME,appName));
    }
}