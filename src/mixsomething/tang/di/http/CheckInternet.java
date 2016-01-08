package mixsomething.tang.di.http;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


/**
 * 检测网络类
 * @author tangdi
 *
 *
  * 测试ConnectivityManager 
  * ConnectivityManager主要管理和网络连接相关的操作 
  * 相关的TelephonyManager则管理和手机、运营商等的相关信息；WifiManager则管理和wifi相关的信息。 
  * NetworkInfo类包含了对wifi和mobile两种网络模式连接的详细描述,通过其getState()方法获取的State对象则代表着 
  * 连接成功与否等状态。 
  *  
  */  

public class CheckInternet {
	
	private static final String TAG = "===CheckIntent===";
	
	private Activity activity;
	
	private ConnectivityManager mConnectivityManager;
	
	private NetworkInfo mNetworkInfo;
	
	private static CheckInternet checkInternet = null;
	
	private State state;
	
	/**
	 * ============================网络连接状态=========================================
	 */
	private boolean WIFI_CONNECT = false;
	
	private boolean MOBLE_CONNECT = false;
	
	/**
	 * ==============================3G网络连接状态=======================================
	 */
	
	private static final String NETWORKTYPE_INVALID = "disconnect";
	
	private static final String NETWORKTYPE_WAP = "1G";
	
	private static final String NETWORKTYPE_2G = "2G";
	
	private static final String NETWORKTYPE_3G = "3G";
	
	private String mNetWorkType;
	
	/**
	 * 网络是否连接标志
	 */
	private static boolean connect_flag = false;
	
	private CheckInternet(Activity activity){
		this.activity = activity;
		mConnectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		Log.d(TAG, "实例化CheckInternet");
	}
	
	/**
	 * 单例加载类
	 * @param activity
	 * @return
	 */
	public CheckInternet getCheckInternet(Activity activity){
		if(checkInternet == null){
			return new CheckInternet(activity);
		}
		return checkInternet;
	}
	
	/**
	 * 判断网络是否联通
	 * 将返回true和false
	 */
	public boolean isCheck(){
		if(mNetworkInfo.isAvailable()){
			connect_flag = true;
		}else{
			connect_flag = false;
		}
		return connect_flag;
	}
	
	
	/**
	 * 判断网络连接类型
	 * 参数内容为联网状态，参数可选
	 * 传入参数为true，说明已经确定网络连接可用
	 * 没有传入参数则会根据类中的connect_flag自行判断
	 */
	public void connectType(boolean...val){
		
		if(val != null){
			if(val.length != 0){
				if(val[0]){
					state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
					if(state == State.CONNECTED){
						MOBLE_CONNECT = true;
						Log.d(TAG, "====GPRS===连接");
					}
					state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
					if(state == State.CONNECTED){
						WIFI_CONNECT = true;
						Log.d(TAG, "====WIFI===连接");
					}
				}
			}else{
				if(isCheck()){
					state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
					if(state == State.CONNECTED){
						MOBLE_CONNECT = true;
						Log.d(TAG, "====GPRS===连接");
					}
					state = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
					if(state == State.CONNECTED){
						WIFI_CONNECT = true;
						Log.d(TAG, "====WIFI===连接");
					}
				}
			}
		}
		
	}
	
	/**
	 * 判断是否是3G网络
	 * 参数内容为是否手机联网状态，参数可选
	 * 传入参数为true，说明已经确定网络连接可用
	 * 没有传入参数则会根据类中的connect_flag自行判断
	 * ==========================================start======================================
	 */
	private boolean isFastMobileNetwork() {
		TelephonyManager telephonyManager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
		    switch (telephonyManager.getNetworkType()) {
		        case TelephonyManager.NETWORK_TYPE_1xRTT:
		            return false; // ~ 50-100 kbps
		        case TelephonyManager.NETWORK_TYPE_CDMA:
		            return false; // ~ 14-64 kbps
		        case TelephonyManager.NETWORK_TYPE_EDGE:
		            return false; // ~ 50-100 kbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_0:
		            return true; // ~ 400-1000 kbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_A:
		            return true; // ~ 600-1400 kbps
		        case TelephonyManager.NETWORK_TYPE_GPRS:
		            return false; // ~ 100 kbps
		        case TelephonyManager.NETWORK_TYPE_HSDPA:
		            return true; // ~ 2-14 Mbps
		        case TelephonyManager.NETWORK_TYPE_HSPA:
		            return true; // ~ 700-1700 kbps
		        case TelephonyManager.NETWORK_TYPE_HSUPA:
		            return true; // ~ 1-23 Mbps
		        case TelephonyManager.NETWORK_TYPE_UMTS:
		            return true; // ~ 400-7000 kbps
		        case TelephonyManager.NETWORK_TYPE_EHRPD:
		            return true; // ~ 1-2 Mbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_B:
		            return true; // ~ 5 Mbps
		        case TelephonyManager.NETWORK_TYPE_HSPAP:
		            return true; // ~ 10-20 Mbps
		        case TelephonyManager.NETWORK_TYPE_IDEN:
		            return false; // ~25 kbps
		        case TelephonyManager.NETWORK_TYPE_LTE:
		            return true; // ~ 10+ Mbps
		        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		            return false;
		        default:
		            return false;
		        }
		    }
	
	public String getNetWorkType(boolean...state) {
		
		if(state != null){
			if(state.length != 0){
				if(state[0]){
					ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
				    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
				    if (networkInfo != null && networkInfo.isConnected()) {
				        String type = networkInfo.getTypeName();
				        if (type.equalsIgnoreCase("MOBILE")) {
				            String proxyHost = android.net.Proxy.getDefaultHost();
				            mNetWorkType = TextUtils.isEmpty(proxyHost)
				                    ? (isFastMobileNetwork() ? NETWORKTYPE_3G : NETWORKTYPE_2G)
				                    : NETWORKTYPE_WAP;
				        }
				    } else {
				        mNetWorkType = NETWORKTYPE_INVALID;
				    }
				    return mNetWorkType;
				}
			}else{
				ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			    if (networkInfo != null && networkInfo.isConnected()) {
			        String type = networkInfo.getTypeName();
			        if (type.equalsIgnoreCase("MOBILE")) {
			            String proxyHost = android.net.Proxy.getDefaultHost();
			            mNetWorkType = TextUtils.isEmpty(proxyHost)
			                    ? (isFastMobileNetwork() ? NETWORKTYPE_3G : NETWORKTYPE_2G)
			                    : NETWORKTYPE_WAP;
			        }
			    } else {
			        mNetWorkType = NETWORKTYPE_INVALID;
			    }
			    return mNetWorkType;
			} 
		}else{
			System.err.print("========= " + TAG + "====== " + "getNetWorkType参数错误");
			mNetWorkType = "参数错误";
		}
		return mNetWorkType;
	}
	/**
	 * ===========================================end===================================================
	 */
		
	
	/**
	 * 获取终端IP
	 */
	public static String getLocalIpAddress() {
		try {
			// 取得枚举网络接口
			Enumeration<NetworkInterface> enumNetworkInterface = NetworkInterface.getNetworkInterfaces();
			// 遍历枚举网络接口
			while (enumNetworkInterface.hasMoreElements()) {
				// 取得网络接口
				NetworkInterface networkInterface = enumNetworkInterface.nextElement();
				// 取得枚举枚举inet地址
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
				// 遍历枚举枚举inet地址
				while (enumInetAddress.hasMoreElements()) {
					// 取得inet地址
					InetAddress inetAddress = enumInetAddress.nextElement();
					// inet地址不是环回地址并且是inet4地址实例
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
						// 返回终端IP
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回默认终端IP
		return "";
	}
	

}
