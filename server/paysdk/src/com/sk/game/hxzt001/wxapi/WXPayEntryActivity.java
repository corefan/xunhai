package com.sk.game.hxzt001.wxapi;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.sk.game.hxzt001.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unity3d.player.UnityPlayer;

/**
 * 微信支付
 * 
 * @author mita
 * 
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		String orderInfo = this.getIntent().getStringExtra("payInfo");
		try {
			this.SetWXPayInfo(orderInfo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	/**
	 * 微信主动请求的回调函数
	 */
	@Override
	public void onReq(BaseReq req) {
	}

	/**
	 * 向微信请求的回调函数
	 */
	@Override
	public void onResp(BaseResp resp) {
		// 判断返回类型是否为支付
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// 对返回值做判断
			switch (resp.errCode) {
			case ErrCode.ERR_USER_CANCEL:
				// 取消
				Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
				UnityPlayer
						.UnitySendMessage("PayMgr", "PayResult", "支付取消");
				break;
			case ErrCode.ERR_OK:
				// 成功
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				UnityPlayer
						.UnitySendMessage("PayMgr", "PayResult", "支付成功");
				break;
			default:
				Toast.makeText(this, "支付失败错误码" + resp.errCode,Toast.LENGTH_LONG).show();
				UnityPlayer
						.UnitySendMessage("PayMgr", "PayResult", "支付失败");
				break;
			}
		}
		WXPayEntryActivity.this.finish();
	}

	/**
	 * 
	 * @param info
	 *            由服务器发来的支付信息
	 * @throws JSONException
	 */
	private void SetWXPayInfo(String info) throws JSONException {
		api.registerApp(Constants.APP_ID);
		if (isWeChatAppInstalled()) {
			JSONObject json = new JSONObject(info);

			PayReq req = new PayReq();
			// 此处的如json.getString("appid");中的appid，具体变量名由服务器指定，更改为服务器指定变量名即可
			req.appId = json.getString("appid");
			req.partnerId = json.getString("partnerid");
			req.prepayId = json.getString("prepayid");
			req.packageValue = json.getString("package");
			req.nonceStr = json.getString("noncestr");
			req.timeStamp = json.getString("timestamp");
			req.sign = json.getString("sign");

			// 调起支付
			api.sendReq(req);
		} else {
			// 未安装微信则弹出提示，关闭页面
			Toast.makeText(this, "尚未安装微信", Toast.LENGTH_SHORT).show();
			WXPayEntryActivity.this.finish();
		}
	}

	/**
	 * 判断设备内是否安装微信 使用api的方法及遍历包名做判断
	 * 
	 * @return true为已安装 false为未安装
	 */
	private boolean isWeChatAppInstalled() {
		if (api.isWXAppInstalled()) {
			return true;
		} else {
			// 获取packagemanager
			final PackageManager packageManager = this.getPackageManager();
			// 获取所有已安装程序的包信息
			List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
			if (pinfo != null) {
				for (int i = 0; i < pinfo.size(); i++) {
					String pn = pinfo.get(i).packageName;
					// "com.tencent.mm"为微信包名
					if (pn.equalsIgnoreCase("com.tencent.mm")) {
						return true;
					}
				}
			}
			return false;
		}
	}

}