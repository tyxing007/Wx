package com.wwu.wx.wxpay;

public class WxConfig {

	public static final String APPID = "wxf923d8669d60855f";
	public static final String APP_SECRET = "ffbd14cd8ff255c7ca9911ab1bc01d51";
	
	public static final String MCH_ID = "1309307501";
	public static final String APP_KEY = "LW2015wereadclub2015weshuhui2015";
	

	public static final String LIMIT_PAY = "no_credit";
	public static final String TRADE_TYPE = "JSAPI";
	public static final String DEVICE_INFO = "WEB";
	public static final String CHARSET = "utf-8";
	public static final String SIGNTYPE="MD5";
	
	public static final String NOTIFY_URL = "http://book.uedhome.cn/BookClub/wxpay/notifyFromWx";
	public static final String ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	public static final String DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	public static final String UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	
	public static final String SCOPE="snsapi_userinfo";
	public static final String USERINFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public static final String QRCODE_RUL="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	
	public static final String SHOWQRCODE_RUL="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	public static final String ACCESS_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	public static final String OAUTH_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public final static String COMMON_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	public final static String REFRESH_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	
	public final static String TEMPLATE_SEND="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	
	public final static String TOKEN="wxf923d8669d60855f";
	public final static String MODEL="development"; //product
	
	
	
	
	
	//-------------------jsapi_ticket----------JS-SDK使用权限签名算法----------------------
	public final static String JEDIS_KEY_JSAPI="JSSDKAPI"; //redis存储的key.
	public final static String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public final static String JSAPI_SHARE_FUNTION_LIST="'onMenuShareQZone','onMenuShareWeibo','onMenuShareQQ','onMenuShareAppMessage','onMenuShareTimeline'";
	
	
	
/*	public static void main(String[] args) {
		
	}*/
	
	
}
