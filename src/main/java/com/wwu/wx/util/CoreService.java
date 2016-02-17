package com.wwu.wx.util;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wwu.wx.constants.Cts;
import com.wwu.wx.model.UserInfo;
import com.wwu.wx.model.wechat.response.TextMessage;

public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			
			System.out.println("............微信core服务.....processRequest..开始...........");
			
			
			// 默认返回的文本消息内容
			String respContent = "";

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			String contentString = requestMap.get("Content");
			
			System.out.println("..........fromUserName....."+fromUserName);
			System.out.println("..........toUserName....."+toUserName);
			System.out.println("..........msgType....."+msgType);
			System.out.println("..........content....."+contentString);
			
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			 textMessage.setFuncFlag(0);  
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				System.out.println("=======客服？=======");
				// 回复文本消息
				if(contentString.equals("客服")){
					textMessage.setContent("你好");
					textMessage.setMsgType("transfer_customer_service");
				}else{
					textMessage.setContent("如果有疑问，请直接输入：【客服】-联系客服人员");
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent +=fromUserName;
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent +=fromUserName;
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent +=fromUserName;
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent +=fromUserName;
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent ="欢迎关注维书会.公众号目前正在拼命开发中...如果有疑问，请直接输入：【客服】-联系客服人员";
					textMessage.setContent(respContent);
					UserInfo userInfo=new UserInfo();
					if(requestMap.get("EventKey")!=null){
						//事件KEY值，qrscene_为前缀，后面为二维码的参数值
						String screnStr=requestMap.get("EventKey");
						try {
							System.out.println("扫描-->订阅"+screnStr);
							Integer pId=Integer.parseInt(screnStr.replace("qrscene_", ""));
							userInfo.setpId(pId);
						} catch (Exception e) {
							//NOOP
						}
					}
					userInfo.setOpenid(fromUserName);
//					userInfoService.insertSelective(userInfo);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					System.out.println("用户取消订阅");
					UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Cts.USER_IN_SESSION);
					System.out.println(userInfo.getOpenid());
					System.out.println(userInfo.getUserId());
					request.getSession().invalidate();

				}
//					如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
//					如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者。	
				else if(eventType.equals(MessageUtil.EVENT_TYPE_SCAN)){
					
//					事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id/scene_str
//					requestMap.get("EventKey");
					//事件KEY值，qrscene_为前缀，后面为二维码的参数值
					
					UserInfo userInfo=new UserInfo();
					String screnStr=requestMap.get("EventKey");
					Integer pId=null;
					try {
						pId=Integer.parseInt(screnStr);
						System.out.println("扫描事件:"+screnStr);
						userInfo.setpId(pId);
					} catch (Exception e) {
						//NOOP
					}
					
//					UserInfo bean=userInfoService.getUserInfoByOpenId(fromUserName);
					//过滤自己的扫描自己
//					if(bean!=null && pId!=null && bean.getUserId().intValue()!=pId.intValue()){
//						userInfo.setOpenid(fromUserName);
//						userInfoService.upateUserInfo(userInfo);
//					}
				//模板消息	
				}else if(eventType.equals("TEMPLATESENDJOBFINISH")){
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
				}
			}
			
			respMessage = MessageUtil.textMessageToXml(textMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}
}
