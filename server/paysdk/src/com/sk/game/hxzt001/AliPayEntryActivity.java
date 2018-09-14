package com.sk.game.hxzt001;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.unity3d.player.UnityPlayer;

public class AliPayEntryActivity extends Activity {
	private static final int SDK_PAY_FLAG = 1; // 支付

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String orderInfo = this.getIntent().getStringExtra("payInfo");
		this.SetAlipayInfo(orderInfo);
	}

	/** 设置订单信息 */
	private void SetAlipayInfo(final String orderInfo) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(AliPayEntryActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult(
						(Map<String, String>) msg.obj);
				/**
				 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				// 同步返回需要验证的信息
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(AliPayEntryActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					UnityPlayer.UnitySendMessage("PayMgr", "PayResult",
							"支付成功");
					finish();
				} else if (TextUtils.equals(resultStatus, "8000")) {
					// 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
					Toast.makeText(AliPayEntryActivity.this, "支付中",
							Toast.LENGTH_SHORT).show();
					UnityPlayer.UnitySendMessage("PayMgr", "PayResult",
							"支付中");
					finish();
				} else {
					Toast.makeText(AliPayEntryActivity.this, "支付失败",
							Toast.LENGTH_SHORT).show();
					UnityPlayer.UnitySendMessage("PayMgr", "PayResult",
							"支付失败");
					finish();
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * get the sdk version. 获取SDK版本号
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

}
