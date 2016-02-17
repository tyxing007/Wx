package com.wwu.wx.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wwu.wx.model.Bill;
import com.wwu.wx.model.Count;
import com.wwu.wx.util.MessageUtil;
import com.wwu.wx.util.TokenProxy;
import com.wwu.wx.wxpay.HttpUtil;
import com.wwu.wx.wxpay.Model;
import com.wwu.wx.wxpay.Utils;
import com.wwu.wx.wxpay.WxConfig;
import com.wwu.wx.wxpay.WxPay;


@Controller
@RequestMapping("/wxpay")
public class WeiXinPayController extends BaseController {


	// 商户系统和微信支付系统主要交互：
	// 1、商户server调用统一下单接口请求订单，api参见公共api【统一下单API】
	// 2、商户server接收支付通知，api参见公共api【支付结果通知API】
	// 3、商户server查询支付结果，api参见公共api【查询订单API】

	// get openId
	// 统一下单
	// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
	// H5发起支付

	// 支付结果通知
	// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7

	@RequestMapping("/payRequestSponsor")
	public ModelAndView payRequestSponsor(WxPay wxPay,
			HttpServletRequest request, HttpServletResponse response) {

		// validate parameters
		wxPay.setSpbillIp(request.getRemoteAddr());
		ModelAndView mv=null;
		if(wxPay.getViewName().equals("2")){
			mv= new ModelAndView("/user/openMember");	
		}else {
			mv = new ModelAndView("/pay/payForSelf");	
		}
		
		try {
			response.setCharacterEncoding("UTF-8");
			JSONObject json = Utils.payRequestSponsor(wxPay);
			System.out.println("payRequestSponsor" + json.toString());
			mv.addObject("WxPay", json);
			mv.addObject("flag", "invoke");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	//微信回调
	@RequestMapping("/notifyFromWx")
	public void notifyFromWx(HttpServletRequest request,
			HttpServletResponse response) {
		// 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
		// 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。
		// (通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒)
		// xml请求解析

		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			if (!isSuccessful(requestMap)) {
				outPrintFail(out);
				return;
			}
			// Send template message
			sendTemplateMsg(requestMap);
			String attach = requestMap.get("attach");
			
			System.out.println(".................{"+attach+"}............");
			
			if (attach == null || attach.indexOf("_") == -1) {
				outPrintFail(out);
				return;
			}


			String type = attach.split("_")[1];
			System.out.println(".................{"+type+"}............");
			int money = Integer.parseInt(requestMap.get("total_fee"));
			if (Model.product.toString().equals(WxConfig.MODEL)) {
				money = money / 100;
			}
			if (Model.development.toString().equals(WxConfig.MODEL)) {
				money = money * 100;
			}
			// 1=维币充值 2=微信充值-成为会员
			if ("1".equals(type)) {
				System.out.println(".................{"+"充值"+"}............");
			} else {
				//1=成功2=失败
			}
			outPrintSuccess(out);
		} catch (Exception e) {
			outPrintFail(out);
			e.printStackTrace();
		}

	}
	@RequestMapping("/downloadbill")
	public void downloadbill(HttpServletRequest request,
			HttpServletResponse response) {
		// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_6
		// 商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
		// 注意：
		// 1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致，bill_type为REVOKED；
		// 2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
		// 3、对账单中涉及金额的字段单位为“元”
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		try {
			parameters.add(new BasicNameValuePair("appid", WxConfig.APPID));
			parameters.add(new BasicNameValuePair("mch_id", WxConfig.MCH_ID));
			parameters.add(new BasicNameValuePair("nonce_str", Utils
					.genNonceStr()));
			parameters.add(new BasicNameValuePair("bill_date", ""));
			parameters.add(new BasicNameValuePair("bill_type", "ALL"));

			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("sign", Utils
					.genPackageSign(parameters)));
			params.addAll(parameters);
			doPost(WxConfig.DOWNLOADBILL, Utils.toXml(params));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	public void doPost(String uri, String parameters) {

		File file = null;
		// CloseableHttpResponse response = null;
		try {

			// HttpPost httpost = new HttpPost(uri);
			// if (parameters != null) {
			// httpost.setEntity(new StringEntity(parameters, "UTF-8"));
			// }
			// response = HttpUtil.createSSLClientDefault().execute(httpost);
			// InputStream input = response.getEntity().getContent();
			//
			// file = File.createTempFile(System.currentTimeMillis() + "",
			// ".txt");
			// System.out.println(file.getAbsolutePath());
			// FileOutputStream output = new FileOutputStream(file);
			// byte[] b = new byte[1024];
			// int l = 0;
			// while ((l = input.read(b)) != -1) {
			// output.write(b, 0, l);
			// }
			//
			// output.flush();
			// output.close();
			// response.close();

			file = new File(
					"C:/Users/Andy_Liu/AppData/Local/Temp/14534597130777397052421274388468.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			String line = reader.readLine();
			// 交易时间,公众账号ID
			boolean flag = false;
			List<Bill> bills = new ArrayList<Bill>();
			while (line != null) {
				line = reader.readLine();
				if (line != null) {
					if (Pattern.compile("[\u4e00-\u9fa5]+").matcher(line)
							.find()) {
						// 总交易单数,总交易额,总退款金额,总代金券或立减优惠退款金额,手续费总金额
						flag = true;
						continue;
					}
					if (flag) {
						System.out.println(new Count(line));
					} else {
						System.out.println(new Bill(line).toString());
						// payCoreService.insertBill(new Bill(line));
						bills.add(new Bill(line));
					}

				}
			}

			if (!bills.isEmpty()) {
				
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				// file.delete();
			}
		}

	}
	

	private boolean isSuccessful(Map<String, String> map)
			throws UnsupportedEncodingException {

		if ("SUCCESS".equals(map.get("return_code"))) {
			if ("SUCCESS".equals(map.get("result_code"))) {
				 return sign(map);
			}
			// 修改订单状态
			return false;
		}
		// 修改订单状态
		return false;
	}


	private boolean sign(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return false;
		}
		Iterator<String> iterator = map.keySet().iterator();
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			System.out.println("key:" + key + ",value=" + map.get(key));
			if ("sign".equals(key)) {
				continue;
			}
			params.add(new BasicNameValuePair(key, map.get(key)));
		}
		String sign="";
		try {
			sign = Utils.genPackageSign(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sign==null || StringUtils.isBlank(sign)){
			return false;
		}
		return sign.equals(map.get("sign"));
	}
	
	private String sendTemplateMsg(Map<String,String> map) throws ClientProtocolException, IOException{
		String url=WxConfig.TEMPLATE_SEND.replace("ACCESS_TOKEN", TokenProxy.getCommonToken());
		JSONObject data=new JSONObject();
        JSONObject body=new JSONObject();
        body.accumulate("touser", map.get("openid"));
		body.accumulate("template_id", "nb6ZJ0MEslmrDSEu6vZ9U0hlD8roBbG3k4PCmMwUtf0");
//		body.accumulate("url","http://weixin.qq.com/download");
		
		//First Data
		JSONObject firstData=new JSONObject();
			firstData.put("value", "恭喜你成为维书会的一员!");
			firstData.put("color", "#173177");
		data.accumulate("first",firstData);
		
		//orderMoneySum
		JSONObject orderMoneySum=new JSONObject();
		orderMoneySum.put("value",(new BigDecimal(map.get("cash_fee")).divide(new BigDecimal(100))));
		orderMoneySum.put("color", "#173177");
		data.accumulate("orderMoneySum",orderMoneySum);
				
		//orderProductName
		JSONObject orderProductName=new JSONObject();
		orderProductName.put("value","购买会员");
		orderProductName.put("color", "#173177");
		data.accumulate("orderProductName",orderProductName);
		
		//orderProductName
		JSONObject remark=new JSONObject();
		remark.put("value","如有问题请致电400-828-1878");
		remark.put("color", "#173177");
		data.accumulate("Remark",remark);
		
		body.accumulate("data", data);
		return HttpUtil.doPost(url,body.toString());
	}
}
