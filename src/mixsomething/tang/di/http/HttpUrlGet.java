package mixsomething.tang.di.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Http获取信息
 * 使用android的httpClient
 * @author tangdi
 *
 */
public class HttpUrlGet {
	
	/**
	 * 请求头参数设置接口
	 */
	public interface HttpHeadInformation {
		
		public  JSONObject getHeadInformation();
		
	}
	
	private HttpHeadInformation httpInformation;
	
	/**
	 * 保存请求头参数
	 */
	private static JSONObject headInfor;

	private HttpClient httpClient;
	
	private HttpPost httpPost;
	
	private URL url;
	
	public HttpUrlGet(String url_path, HttpHeadInformation httpInformation){
		try {
			url = new URL(url_path);
		} catch (MalformedURLException e) {
			System.err.println("=== " + "URL不符合" + " ===");
			e.printStackTrace();
		}
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(url_path);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30 * 1000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
		httpPost.addHeader("Content-type", "application/json");
		httpPost.addHeader("text/plain", "charset=UTF-8");
		this.httpInformation = httpInformation;
		
		headInfor = httpInformation.getHeadInformation();
	}
	
	/**
	 * 参数获取通过接口获得，由业务决定
	 * 调用该方法确认是否已完成接口HttpInformation
	 */
	public String sendRequest(JSONObject object){
		String result = null;
		//请求头map
		JSONObject headMap = httpInformation.getHeadInformation();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("head", headMap);
			jsonObject.put("body", object);
		} catch (JSONException e) {
			System.err.println("=== " + "请求参数JSON化失败" + " ===");
			e.printStackTrace();
		}
		StringEntity entity = null;
		try {
			entity = new StringEntity(jsonObject.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		
		//返回结果
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			try {
				result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	

}
