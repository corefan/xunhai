package com.sk.game.hxzt001;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sk.game.hxzt001.wxapi.WXPayEntryActivity;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {
	
	private Activity mActivity = null;      
	private Context mContext = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
    }
    
    public void pay(String payInfo, int type) throws JSONException{
    	Intent intent = null;
    	// 支付宝
    	if(type == Constants.PayType.AliPay){
    		intent = new Intent(mContext, AliPayEntryActivity.class);     
    	}else if(type == Constants.PayType.WXPay){
    		// 微信
    		intent = new Intent(mContext, WXPayEntryActivity.class);
    	}
    	toIntent(payInfo, intent);
    }
    
    private void toIntent(String payInfo, Intent intent){
    	intent.putExtra("payInfo", payInfo);
        mActivity.startActivity(intent);
    }
}
