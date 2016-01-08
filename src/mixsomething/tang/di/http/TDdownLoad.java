package mixsomething.tang.di.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * getUrlBytes
 * 获取下载地址资源的字节数组流
 * 
 * getUrl
 * 获取下载地址资源的字符串
 * @author tangdi
 *
 */
public class TDdownLoad {
	
	private static final String TAG = "====TDdownLoad====";
	
	public byte[] getUrlBytes(String urlSpec) throws IOException{
		
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();
			if(connection .getResponseCode() != HttpURLConnection.HTTP_OK){
				return null;
			}
			int byteRead = 0;
			byte[] buffer = new byte[1024];
			while((byteRead = in.read(buffer)) > 0){
				out.write(buffer, 0, byteRead);
			}
			out.close();
			Log.d(TAG, "字节数组流成功转换");
			return out.toByteArray();
		}finally{
			connection.disconnect();
			Log.d(TAG, "关闭网络连接");
		}
		
	}
	
	public String getUrl(String urlSpec) throws IOException{
		
		Log.d(TAG, "字节数组流======>字符串");
		return new String(getUrlBytes(urlSpec));
		
	}

}
