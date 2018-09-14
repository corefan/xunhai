package com.core.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.CodeContext;
import com.service.ICodeService;
import com.util.LogUtil;

/**
 * @author barsk
 * 2014-5-12
 * 激活码	
 */
public class CodeServlet extends BaseServlet {

	private static final long serialVersionUID = 107002412946708515L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		initReqResp(req, resp);

		try {
			JSONObject jsonObject = dealMsg(req);
			
			if (jsonObject != null) {
				String result = useCode(jsonObject);
				if (result != null) {
					resp.getWriter().println(result);
					resp.getWriter().flush();
				}
			}
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}
	}

	/**
	 * 使用激活码
	 * */
	private String useCode(JSONObject jsonObject) throws Exception {
		
		ICodeService codeService = CodeContext.getInstance().getServiceCollection().getCodeService();
		
		String code = jsonObject.getString("code");
		String site = jsonObject.getString("site");
		int playerID = jsonObject.getInt("playerID");
		
		JSONObject resultJson = codeService.useCode(playerID, site, code);
		resultJson.put("playerID", playerID);
		
		return resultJson.toString();
	}
	
}
