package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.DateService;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.domain.config.BaseServerConfig;
import com.service.IAccountService;
import com.service.IBaseDataService;
import com.util.CommonUtil;
import com.util.HttpUtil;
import com.util.LogUtil;

/**
 * 账户登录
 * @author ken
 * 2014-3-8
 */
public class LoginServlet extends BaseServlet {

	private static final long serialVersionUID = 5681739853641641114L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			if("xh".equals(Config.AGENT)){
				//迅海
				doLogin(req, resp);
			}else if("donghai".equals(Config.AGENT)){
				//东海
				donghaiLogin(req, resp);
			}else if("yunyou".equals(Config.AGENT)){
				//云游
				yunyouLogin(req, resp);
			}else if("zhongfu".equals(Config.AGENT)){
				//中富
				zhongfuLogin(req, resp);
			}else if("juliang".equals(Config.AGENT)){
				//聚量
				juliangLogin(req, resp);
			}
			

		} catch (Exception e) {
			LogUtil.error("登陆异常: ",e);
		}
	}

	/**
	 * 处理账户登录 appid=xxx&userId=xxx&userName=xxx&passWord=xxx&tourist=xxx&token=xxx&time=xxx&sign=xxx
	 */
	private void doLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {

		JSONObject result = new JSONObject();
		
		String appid = req.getParameter("appid");
		String userId = req.getParameter("userId");
		String userName = req.getParameter("userName");
		String passWord = req.getParameter("passWord");
		String tourist = req.getParameter("tourist");
		String token = req.getParameter("token");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		
		if(userName == null || userName.trim().equals("")){
			//1:账号为空
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		if(passWord == null || passWord.trim().equals("")){
			//2:密码为空
			result.put("result", 2);
			this.postData(resp, result.toString());
			return;
		}
		
		int pwdLenth = passWord.length();
		if(pwdLenth < 6 || pwdLenth > 15){
			//3:密码长度有误
			result.put("result", 3);
			this.postData(resp, result.toString());
			return;
		}
		
		if(time == null || time.trim().equals("")  || sign == null || sign.trim().equals("")
				|| tourist == null || tourist.trim().equals("")){
			//6 登录失败
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(appid+userId+userName+passWord+tourist+token+time+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//6 登录失败
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		this.postData(resp, this.sucLogin(0, userName, passWord, null, Integer.valueOf(tourist), 0));
	}

	/**
	 * 东海运营  处理账户登录 https://github.com/donghaigame/DHSDKServerDemo
	 */
	private void donghaiLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		
		String appid = req.getParameter("appid");
		String userId = req.getParameter("userId");
		String userName = req.getParameter("userName");
		String passWord = req.getParameter("passWord");
		String tourist = req.getParameter("tourist");
		String token = req.getParameter("token");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		JSONObject result = new JSONObject();
		
		if(userId == null || "".equals(userId.trim()) 
		   || userName == null || "".equals(userName.trim())
		   || time == null || "".equals(time.trim())
		   || sign == null || "".equals(sign.trim())){
			//登录失败
			LogUtil.error("donghaiLogin 参数有误");
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(appid+userId+userName+passWord+tourist+token+time+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//6 登录失败
			LogUtil.error("donghaiLogin sign验证有误");
			
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		this.postData(resp, this.sucLogin(Long.valueOf(userId), userName, "123456", null, 1, Integer.valueOf(appid)));
		
	}
	
	/**
	 * 云游运营  处理账户登录  http://lll.lygames.cc/index.php?m=index&c=user&a=Token&uid=客户端传给的uid&token=token 码
	 */
	private void yunyouLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		
		JSONObject result = new JSONObject();
		
		String appid = req.getParameter("appid");
		
		if(appid.equals("1") || appid.equals("2") || appid.equals("3")){   //大唐降魔传  大唐仙侠  降魔苍穹
			String userName = req.getParameter("userName"); 
			String token = req.getParameter("token");
			
			if(userName == null || "".equals(userName.trim()) 
			   || token == null || "".equals(token.trim())){
				//登录失败
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "http://lll.lygames.cc/index.php";
			
			StringBuilder param = new StringBuilder();
			param.append("m=index");
			param.append("&c=user");
			param.append("&a=Token");
			param.append("&uid=").append(userName);
			param.append("&token=").append(token);
			
			String js = HttpUtil.sendGet(url, param.toString());
			if(js == null){
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("isSuccess") .equals("1")){

				this.postData(resp, this.sucLogin(0, userName, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("275")){ //剑雨江湖
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			
			if(uid == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String Login_Key = "6d430a1ae023494e968e9efa28d38e93";
			String url = "http://cfsdk2.cftdcm.com:8080/payserver/account/check_login";
			
			StringBuilder param = new StringBuilder();
			param.append("app="+appid);
			param.append("&token="+token);
			param.append("&ts="+System.currentTimeMillis());
			param.append("&uin=").append(uid);
			String sign = MD5Service.encryptToLowerString(param.toString() + Login_Key);
			param.append("&sign=").append(sign);
			
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			if(js.equals("0")){
				
				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else {
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("4")){ //诛仙青云录
			String uid = req.getParameter("userId");
			String userName = req.getParameter("userName");
			String token = req.getParameter("token");
			
			if(uid == null || userName == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String Login_Key = "Wykj4iHo452mVthczm9jKNAWsIfUzqgp";
			String url = "http://carapi.hbook.us/game/sdklogin";
			
			String timestamp = DateService.dateFormt(new Date(),"yyyyMMddHHmmss");
			StringBuilder param = new StringBuilder();
			param.append("userid="+uid);
			param.append("&timestamp="+timestamp);
			param.append("&companycode=dxyydtzx");
			String sign = MD5Service.encryptToUpperString("timestamp="+timestamp+"&userid="+uid+"&key="+Login_Key);
			param.append("&sign=").append(sign);
			
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			if(js == null){
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("errorcode").equals("0")){

				String uname = resultJson.getString("username");
				if(!userName.equals(uname)){
					LogUtil.error("登录失败 appid："+appid+" uname："+uname+"  sdkName="+userName);
					result.put("result", 6);
					this.postData(resp, result.toString());
					return;
				}
				this.postData(resp, this.sucLogin(0, uname, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
			
		}

	}
	
	/**
	 * 中富登录
	 */
	private void zhongfuLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		JSONObject result = new JSONObject();
		
		String appid = req.getParameter("appid");
		if(appid == null){
			//登录失败
			LogUtil.error("zhongfuLogin appid："+appid);
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		if(appid.equals("1271")){ 	//仙剑长安
		
			String sessionId = req.getParameter("token");
			if(sessionId == null){
				//登录失败
				LogUtil.error("zhongfuLogin SFToken："+sessionId);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String Login_Key = "02906a907397b816cfbf4fa5b4e6a6f7";
			String url = "https://api.nkyouxi.com/sdkapi.php";
			
            int time = ParseNow();
            String param = String.format("ac=check&appid=%s&sdkversion=2.1.7&sessionid=%s&time=%s", appid, GetEncode(sessionId), time);
            String code = String.format("ac=check&appid=%s&sdkversion=2.1.7&sessionid=%s&time=%s%s", appid, GetEncode(sessionId.replace(" ", "+")), time, Login_Key);
            String sign = getMD5(code);
            param += "&sign=" + sign;
            
            String js = HttpUtil.httpsPost(url, param, "application/x-www-form-urlencoded", null);
            //如果成功的话返回值格式：{"userInfo":{"username":"yx598640","uid":"6596"},"code":1}
            if (js == null){
				//登录失败
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
            }
        	JSONObject resultJson = new JSONObject(js);
        	int jsCode = resultJson.getInt("code");
        	if(jsCode == 1){
        		JSONObject userInfo = resultJson.getJSONObject("userInfo");
//        		long userId = userInfo.getLong("uid");
        		String userName = userInfo.getString("username");
        		
        		this.postData(resp, this.sucLogin(0, userName, "123456", null, 1, Integer.valueOf(appid)));
        	}else{
				//登录失败
        		LogUtil.error("zhongfuLogin login返回错误："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
        	}
		}else if(appid.equals("324") || appid.equals("53") 
				|| appid.equals("106") || appid.equals("323")
				|| appid.equals("325")){ //百转修仙  幻域修仙 仙道至尊 仙侠大劫主 修仙道主
			
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			
			if(uid == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String Login_Key = "d5bc0399e17f4dca8c1e5af4d138dfe2";
			if(appid.equals("53")){
				Login_Key = "d04e60cfa9eb47c5be7c74855ccc0d01";
			}else if(appid.equals("106")){
				Login_Key = "ddaf665347b6478c86da101d7a5e766e";
			}else if(appid.equals("323")){
				Login_Key = "7c2e95e1d6974a8aa0865a71717003af";
			}else if(appid.equals("325")){
				Login_Key = "9a3aeccbd52c4a7e97711d92f9be9f37";
			}
			String url = "http://cfsdk2.cftdcm.com:8080/payserver/account/check_login";
			
			StringBuilder param = new StringBuilder();
			param.append("app="+appid);
			param.append("&token="+token);
			param.append("&ts="+System.currentTimeMillis());
			param.append("&uin=").append(uid);
			String sign = MD5Service.encryptToLowerString(param.toString() + Login_Key);
			param.append("&sign=").append(sign);
			
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			if(js.equals("0")){
				
				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else {
				//登录失败
				LogUtil.error("登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
			
		}else if(appid.equals("799") || appid.equals("150000007") 
				|| appid.equals("150000008")|| appid.equals("150000009") 
				|| appid.equals("150000010") ){ //大唐山海缘  妖魔大陆  御剑降魔录  斩妖奇侠 诛妖 
			
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			
			if(uid == null  || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "https://api.youximax.com/newapi.php/User/check_login_token";
			if(appid.equals("799")){
				url = "http://apiqw.3z.cc/newapi.php/User/check_login_token";
			}
			
			StringBuilder param = new StringBuilder();
			param.append("?uid="+uid);
			param.append("&token="+token);
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("status") .equals("1")){

				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("1") || appid.equals("2")){ //大唐修仙传  风暴国度
			
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			
			if(uid == null  || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());  
				return;
			}
			
			String url = "http://api.sy.7k7k.com/index.php/newUser/checkuser";
			String key =MD5Service.encryptToLowerString(MD5Service.encryptToLowerString("api.sy.7k7k.com/index.php") + "uid="+uid+"&vkey="+token); 
			StringBuilder param = new StringBuilder();
			param.append("uid="+uid);
			param.append("&key="+key);
			param.append("&vkey="+token);
			
			String js = HttpUtil.sendGet(url, param.toString());
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("status") .equals("0")){

				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("10080") || appid.equals("10012")){ //焚天决 御仙诀
			String token = req.getParameter("token");
			if(token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "http://gameuser.xx183.cn:7070/third/getUserInfo";
			String key = "LNLFQHKHPALYUWHK";
			if(appid.equals("10012")){
				key = "FNAFOUAPTJODFKPN";
			}
			String secret = MD5Service.encryptToLowerString(appid +"&"+ key);
			
			JSONObject param = new JSONObject();
			param.put("accessToken", token);
			param.put("secret", secret);
			param.put("appId", appid);
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/json;charset=uft-8");
			JSONObject resultJson = new JSONObject(js);
			long rsCode = resultJson.getLong("result");
			
			if(rsCode == 0){
				String userName = resultJson.getString("account");
				this.postData(resp, this.sucLogin(0, userName, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败  rsCode："+rsCode+" msg:"+resultJson.getString("msg"));
				
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("6327") || appid.equals("6328") 
				|| appid.equals("6333") || appid.equals("6010")
				|| appid.equals("6089") || appid.equals("6094")
				|| appid.equals("6095")){ //太古封神 太古伏魔录 武动九州 仙侠幻梦录 隋唐修仙传 修仙侠隐
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			if(uid == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "http://api.100game.cn/api/cp/user/check";
			
			String app_key = "957fd6a0159072e37bea8011bb80e7c0";
			if(appid.equals("6328")){
				app_key = "79500a2d1c3db0bf9cc8e8637ef9b270";
			}else if(appid.equals("6333")){
				app_key = "76267feade4396e62b9d62af4b6dbce4";
			}else if(appid.equals("6010")){
				app_key = "2cff258feccca00617a71301bc2b68d7";
				
				url = "https://api.8fen.2lyx.com/api/v7/cp/user/check";
			}else if(appid.equals("6089")){
				app_key = "dea52672cfed4d7b4e071967a9a2da45";
				
				url = "https://api.wx903.com/api/v7/cp/user/check";
			}else if(appid.equals("6094")){
				app_key = "1093cacfbd5c8be8e5ab6e7c7f2ee2dd";
				
				url = "https://api.wx903.com/api/v7/cp/user/check";
			}else if(appid.equals("6095")){
				app_key = "ca8c3511907b7937161660d5dfdfa551";
				
				url = "https://api.wx903.com/api/v7/cp/user/check";
			}
			
			StringBuilder param = new StringBuilder();
			param.append("?");
			param.append("app_id="+appid);
			param.append("&mem_id="+uid);
			param.append("&user_token="+token);
			String sign = MD5Service.encryptToLowerString("app_id="+appid+"&mem_id="+uid+"&user_token="+token+"&app_key="+app_key);
			param.append("&sign"+sign);
			
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("status").equals("0")){
				
				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败  js："+js);
				
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("10") || appid.equals("11") 
				|| appid.equals("12") || appid.equals("13") 
				|| appid.equals("14")){ //古剑苍穹 九州剑雨  太古仙盟 天剑侠客 天之剑
			String uid = req.getParameter("userId");
			String userName = req.getParameter("userName");
			String token = req.getParameter("token");
			if(uid == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "http://quickgame.sdk.quicksdk.net/webapi/checkUserInfo";
			
			StringBuilder param = new StringBuilder();
			param.append("uid="+uid);
			param.append("&token="+token);
			
			String js = HttpUtil.sendGet(url, param.toString());
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("status") .equals("true")){

				this.postData(resp, this.sucLogin(0, userName, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败  js："+resultJson);
				
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}else if(appid.equals("2018000008") || appid.equals("2018000009")){  //苍穹传奇 仙侠轩辕世界
			String uid = req.getParameter("userId");
			String token = req.getParameter("token");
			
			if(uid == null || token == null){
				//登录失败
				LogUtil.error("登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String appkey = "aa52b72559547bfd4135605bed186f29";
			if(appid.equals("2018000009")){
				appkey = "baa5834a6effd194113d956f7206929d";
			}
			
			String url = "http://120.78.131.209/appcenterserver/sdk/service/check_token";
			
			StringBuilder param = new StringBuilder();
			param.append("?");
			param.append("appid="+appid);
			param.append("&accesstoken="+token);
			String sign = MD5Service.encryptToLowerString(appid + token + appkey);
			param.append("&sign"+sign);
			
			String js = HttpUtil.httpsRequest(url, param.toString(), "application/x-www-form-urlencoded");
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("code") .equals("0")){

				uid = resultJson.getString("accountid");
				
				this.postData(resp, this.sucLogin(0, uid, "123456", null, 1, Integer.valueOf(appid)));
			}else{
				//登录失败
				LogUtil.error("登录失败  js："+resultJson);
				
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
			
		}
		
	}
	
	/**
	 * 中富-聚量登录
	 */
	private void juliangLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		JSONObject result = new JSONObject();
		
		String appid = req.getParameter("appid");
		if(appid == null){
			//登录失败
			LogUtil.error("juliangLogin appid："+appid);
			result.put("result", 6);
			this.postData(resp, result.toString());
			return;
		}
		
		if(appid.equals("3")){ 	//剑霸山河

			String userName = req.getParameter("userName");
			String token = req.getParameter("token");
			String sdk = req.getParameter("time");
			String app = req.getParameter("passWord");
			
			if(userName == null || token == null || sdk == null || app == null){
				//登录失败
				LogUtil.error("juliangLogin登录失败  appid："+appid);
				result.put("result", 6);
				this.postData(resp, result.toString());
				return;
			}
			
			String url = "http://sync.1sdk.cn/login/check.html";
			
			StringBuilder param = new StringBuilder();
			param.append("sdk="+sdk);
			param.append("&app="+app);
			param.append("&uin="+GetEncode(userName));
			param.append("&sess="+GetEncode(token));
			
			System.out.println("param="+param.toString());
			String js = HttpUtil.sendGet(url, param.toString());
			if(js.equals("0")){
				
				this.postData(resp, this.sucLogin(0, userName, "123456", null, 1, Integer.valueOf(appid)));
			}else {
				//登录失败
				LogUtil.error("juliangLogin登录失败 appid："+appid+" js："+js);
				result.put("result", 6);
				this.postData(resp, result.toString());
			}
		}
	}
	
	
	/**
	 * 成功登录后
	 * @throws Exception 
	 */
	private String sucLogin(long userId, String userName, String passWord, String telephone, int tourist, int appId) throws Exception{
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject result = new JSONObject();
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null){
			if(tourist == 0){
				//4:账号未注册
				result.put("result", 4);
				return result.toString();
			}else{
				account = accountService.createAccount(0, userName, passWord, null, 1, appId);
			}
		}else{
			if(!account.getPassWord().trim().equals(passWord)){
				//5密码错误
				result.put("result", 5);
				return result.toString();
			}else{
				//如果从其他游戏包进来  重置appId
				if(account.getAppId() != appId){
					account.setAppId(appId);
					accountService.updateAccount(account);
				}
			}
		}
		
		//玩家已创角的服务器列表
		List<Integer> svrList = account.getServerList();
		
		//所有服务器列表
		List<BaseServerConfig> serverList = baseDataService.listServers();
		
		long time = System.currentTimeMillis();
		String key = CommonUtil.randomLoginKey(account.getUserName());
		
		result.put("result", 0);
		result.put("userId", account.getUserId());
		result.put("key", key);
		result.put("time", time);
		if(account.getTelephone() == null){
			result.put("telePhone", 0);
		}else{
			result.put("telePhone", account.getTelephone());
		}
		
		result.put("sign", MD5Service.encryptToUpperString(account.getUserId()+key+time+Config.WEB_LOGIN_KEY));
		
		long curTime = System.currentTimeMillis();
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(BaseServerConfig model : serverList){
			long openServerDate = DateService.getDateByString(model.getOpenServerDate()).getTime();
			if(time < openServerDate) continue;
			
			Integer serverNo = model.getServerNo();
			JSONObject json = new JSONObject();
			json.put("serverNo", model.getServerNo());
			json.put("serverName", model.getServerName());
			json.put("gameHost", model.getGameHost());
			json.put("gamePort", model.getGamePort());
			json.put("loginFlag", svrList.contains(serverNo)? 1 : 0);
			json.put("endStopTime", 0);
			json.put("severType", model.getSeverType());
			json.put("openServerDate", openServerDate);
			int serverState = model.getSeverState();
			if(model.getEndStopDate() != null){
				long endStopTime = DateService.getDateByString(model.getEndStopDate()).getTime();
				if(curTime < endStopTime){
					serverState = 4;
					json.put("endStopTime", endStopTime);
				}
			}
			json.put("severState", serverState);
			
			jsonList.add(json);
		}
		result.put("serverList", jsonList.toString());
		
		return result.toString();
	}
	
}
