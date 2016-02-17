package com.wwu.wx.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.wwu.wx.constants.Cts;
import com.wwu.wx.model.UserInfo;




@Controller
public class BaseController {

	// 共有参数注册。--分页-每次请求条数
	protected static final int pageCount = 10;
	protected static final  String MSG_API_KEY = "f504ef1d5a17091064e33e3a9a369037"; //短信接口apikey
	
	
/*	//Added by William start
	private HttpServletRequest request;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	@Resource
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}*/

	// 获取servlet上下文
	WebApplicationContext webApplicationContext = ContextLoader
			.getCurrentWebApplicationContext();
	
	//Added by William end
	/**
	 * 
	 * @Title getUser
	 * @author Wally
	 * @time 2016年1月12日下午1:52:35
	 * @Description 获取用户session对象信息。
	 * @param
	 * @return UserInfo
	 */
	protected  UserInfo getUser(HttpServletRequest request){
		if(request.getSession().getAttribute(Cts.USER_IN_SESSION)!=null){
			return (UserInfo)request.getSession().getAttribute(Cts.USER_IN_SESSION);
		}
		return null;
	}
	
	protected  void setUser(UserInfo userInfo,HttpServletRequest request){
		if(request!=null && userInfo!=null){
			request.getSession().setAttribute(Cts.USER_IN_SESSION, userInfo);
		}
	}
	
	
	protected int getWb(UserInfo userInfo){
		return 0; 
	}
	
	protected void outPrintFail(PrintWriter out) {
		if (out != null) {
			out.print("FAIL");
		}
	}
	
	protected void outPrintSuccess(PrintWriter out) {
		if (out != null) {
			out.print("SUCCESS");
		}
	}
	
	protected  final String getCode(HttpServletRequest request){
		return request.getParameter("code");
	}
	
	protected  final boolean isFromWx(HttpServletRequest request){
		String code = getCode(request);
		return code==null || "".equals(code.trim()) ? false :true;
	}
	
	
	
	protected String viewLoadPage(HttpServletRequest request, org.springframework.ui.Model model){
		String code = getCode(request);
		model.addAttribute("view",  request.getServletPath());
		model.addAttribute("code",code);
		return "/loading";
	}
	
}
