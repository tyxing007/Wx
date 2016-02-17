package com.wwu.wx.wxpay;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpUtil {

	
	
	public static CloseableHttpClient createSSLClientDefault(){
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
		         //信任所有
		         public boolean isTrusted(X509Certificate[] chain,
		                 String authType) throws CertificateException {
		           return true;
		         }
		       }).build();
		       SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		       return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		     } catch (KeyManagementException e) {
		       e.printStackTrace();
		     } catch (NoSuchAlgorithmException e) {
		       e.printStackTrace();
		     } catch (KeyStoreException e) {
		       e.printStackTrace();
		     }
		     return  HttpClients.createDefault();
		}
	

	public static String doPost(String uri,List<NameValuePair> parameters) 
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		try {
			HttpPost httpost = new HttpPost(uri);
			httpost.setEntity(new UrlEncodedFormEntity(parameters));
			response = createSSLClientDefault().execute(httpost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity,WxConfig.CHARSET);
		} finally {
			if (response != null) {
				response.close();
			}

		}

	}
	
	
	public static JSONObject httpPost(String uri,String parameters) 
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		try {
			HttpPost httpost = new HttpPost(uri);
			if(parameters!=null && !"".equals(parameters)){
				httpost.setEntity(new StringEntity(parameters,"UTF-8"));
			}
			response = createSSLClientDefault().execute(httpost);
			HttpEntity entity = response.getEntity();
			return JSONObject.fromObject(EntityUtils.toString(entity,WxConfig.CHARSET));
		} finally {
			if (response != null) {
				response.close();
			}

		}

	}
	
	public static String doPost(String uri,String parameters) 
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		try {
			HttpPost httpost = new HttpPost(uri);
			httpost.setEntity(new StringEntity(parameters,"UTF-8"));
			response = createSSLClientDefault().execute(httpost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, WxConfig.CHARSET);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
	
	public static JSONObject doGet(String uri) 
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet=new HttpGet(uri);
			response = createSSLClientDefault().execute(httpGet);
			HttpEntity entity = response.getEntity();
			return JSONObject.fromObject(EntityUtils.toString(entity,WxConfig.CHARSET));
		} finally {
			if (response != null) {
				response.close();
			}

		}
	}
	
}
