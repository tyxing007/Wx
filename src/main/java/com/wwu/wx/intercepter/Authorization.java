package com.wwu.wx.intercepter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wwu.wx.constants.Cts;
import com.wwu.wx.model.UserInfo;
import com.wwu.wx.model.wechat.WxJsConfig;
import com.wwu.wx.util.TokenProxy;
import com.wwu.wx.util.WeixinUtil;
import com.wwu.wx.wxpay.AccessToken;
import com.wwu.wx.wxpay.HttpUtil;
import com.wwu.wx.wxpay.WxConfig;

public class Authorization implements HandlerInterceptor  {

	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("================拦截器=【结束】====================");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse arg1,
			Object arg2, ModelAndView mv) throws Exception {
		if(mv!=null){
			mv.addObject("ctx",request.getContextPath());
		}else{
			System.out.println("=============MV ==null==================");
		}
		
	/*	System.out.println("postHandel==【"+request.getRequestURL()+"】");
		System.out.println("request.getQueryString():"+request.getQueryString());*/
		String param = null;
		String basePath=null;
		if(request.getQueryString()==null){
			 basePath = request.getRequestURL().toString();
		}else{
			System.out.println(request.getQueryString());
			param=request.getQueryString();
			basePath = request.getRequestURL().toString()+"?"+param;
		}
		
		System.out.println("组装后完整路径：==:"+basePath);
		
		WxJsConfig wc = WeixinUtil.getJsSdkConfig(basePath);
		if(null != wc && null !=mv){
			mv.addObject("wxConfig",wc);
			
		}
		System.out.println("=================拦截器【渲染之前】=======================");
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {

		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		
		System.out.println(".......................拦截器--【开始】......................................");
		String ua = request.getHeader("user-agent").toLowerCase();
		System.out.println("servletPath=="+request.getServletPath());
		System.out.println("ua=="+ua);
		
		//从微信服务器发起的动作，会先调用一次这个方法，判断通不通。通了之后才会走下面的。
		if (request.getParameter("signature") != null && request.getParameter("timestamp") != null 	&& request.getParameter("nonce") != null) {
			if(request.getMethod().equalsIgnoreCase("GET")){
				System.out.println("GET---->confirmation");
			}
			System.out.println(request.getMethod());
			return true;
		}
		
		
		//上线这里要改
		if (ua.toLowerCase().indexOf("micromessenger") == -1 ) {
			return true;
		}
		
		
		UserInfo userSession = (UserInfo) request.getSession().getAttribute(Cts.USER_IN_SESSION);
		if (userSession != null) {
			System.out.println("==userSession===不为空==========");
			System.out.println(userSession.getUserId()+"【=====】"+userSession.getOpenid());
			return true;
		}
		
		
		UserInfo userInfo = auth(request,response,request.getSession().getId());
		if(userInfo!=null){
			System.out.println("授权 获取用户信息不为空.......");
		}
		
		return true;
	}

	
	
	
	
	private UserInfo auth(HttpServletRequest request,HttpServletResponse response,String key)
			throws ClientProtocolException, IOException {
		
		String code = request.getParameter("_c_o_d_e_");
		System.out.println(".....code... 【"+code+"】 ....................");
		if(code==null){
			System.out.println("======为空不需要授权===同时也说明已经受过权======");
			return null;
		}
		
		UserInfo user =null;
		AccessToken token = null;
		System.out.println("=============获取授权 tocken=====开始====================");
		token = TokenProxy.getOuthToken(code,key);
		System.out.println("====token====【"+token+"】");
		System.out.println("=============获取授权 tocken=====结束====================");
		
		if (token != null && token.getOpenid()!=null) {
		
			String url=WxConfig.USERINFO_URL.replace("ACCESS_TOKEN", token.getToken()).replace("OPENID",
					token.getOpenid());
			JSONObject userInfo = HttpUtil.doGet(url);
			System.out.println("============================================================");
			System.out.println("【"+userInfo+"】");
			System.out.println("============================================================");
			
			if (userInfo != null) {
				user=new UserInfo();
				if (userInfo.get("city") != null) {
					user.setCity(userInfo.getString("city"));
				}
				if (userInfo.get("province") != null) {
					user.setProvince(userInfo.getString("province"));
				}
				if (userInfo.get("country") != null) {
					user.setCountry(userInfo.getString("country"));
				}
				if (userInfo.get("sex") != null) {
					// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
					user.setGender(userInfo.getInt("sex"));
				}
				if (userInfo.get("headimgurl") != null) {
					user.setHeadPic(userInfo.getString("headimgurl"));
				}
				if (userInfo.get("openid") != null) {
					user.setOpenid(userInfo.getString("openid"));
				}
				if (userInfo.get("unionid") != null) {
					user.setUnionid(userInfo.getString("unionid"));
				}
				if (userInfo.get("nickname") != null) {
					user.setNickname(userInfo.getString("nickname"));
				}
			}
		}
		return user;
	}
}

