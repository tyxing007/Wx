package com.wwu.wx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.wwu.wx.model.UserInfo;
import com.wwu.wx.util.CoreService;
import com.wwu.wx.util.RedisUtil;
import com.wwu.wx.util.SerializeUtil;
import com.wwu.wx.util.SignUtil;
import com.wwu.wx.util.TokenProxy;
import com.wwu.wx.wxpay.HttpUtil;


@Controller
@RequestMapping("/Wx")
public class WeiXinController extends BaseController {

	@RequestMapping(method = RequestMethod.GET)
	public void handshake(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		System.out.println("===============微信controller=====【handshake】==开始==========");
		
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	//接受消息。
	@RequestMapping(method = RequestMethod.POST)
	public void message(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		String respMessage = CoreService.processRequest(request);
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
	}
	
	@RequestMapping(value="/getUserFromWx",method=RequestMethod.POST)
	public String getUserFromWx(HttpServletRequest request,
			HttpServletResponse response, Model model) throws ClientProtocolException, IOException {
		return "redirect:"+request.getParameter("view");
	}
	
	
	@RequestMapping("/clearToken/{id}")
	public @ResponseBody Map<String,String> clearToken(@PathVariable String id){
		
		Map<String,String> map=new HashMap<String, String>();
		Jedis jedis=RedisUtil.getJedis();
		jedis.del(SerializeUtil.serialize(id));
		map.put("result", "successful");
		return map;
	}
	
	
	
	
	@RequestMapping("/loading/{targetUrl}")
	public String loadingPage(HttpServletRequest request,Model model,@PathVariable String targetUrl){
		
		//System.out.println("targetUrl======>"+targetUrl.replaceAll("_","/"));
		
		model.addAttribute("targetUrl", "/"+targetUrl.replaceAll("_","/"));
		model.addAttribute("ctx", request.getContextPath()+"/");
		return 	super.viewLoadPage(request, model);
	}
	
	
}
