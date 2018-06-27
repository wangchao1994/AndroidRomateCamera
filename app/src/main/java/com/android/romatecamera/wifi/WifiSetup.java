
package com.android.romatecamera.wifi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Inet4Address;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiSetup {

	// 定义一个WifiManager对象
	private WifiManager mWifiManager;
	// 定义一个WifiInfo对象
	private WifiInfo mWifiInfo;

	Context mcontext = null;

	public WifiSetup(Context context) {
		// 取得WifiManager对象
		mcontext = context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public boolean closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			return mWifiManager.setWifiEnabled(false);
		}
		return false;
	}

	/**
	 * Gets the Wi-Fi enabled state.检查当前wifi状态
	 * 
	 * @return One of {@link WifiManager#WIFI_STATE_DISABLED},
	 *         {@link WifiManager#WIFI_STATE_DISABLING},
	 *         {@link WifiManager#WIFI_STATE_ENABLED},
	 *         {@link WifiManager#WIFI_STATE_ENABLING},
	 *         {@link WifiManager#WIFI_STATE_UNKNOWN}
	 */
	public int checkState() {
		return mWifiManager.getWifiState();
	}


	//判断wifi是否连接上
	/*
	 * 若使用下面方法有的手机会直接开启wifi，以进行检查，造成判断结果为正
	 * NetworkInfo mWiFiNetworkInfo = mConnectivityManager .getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
	 * 
	 */
	public boolean isWifiConnected() {  
		if (mcontext != null) {  
			ConnectivityManager mConnectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			//要先保证mNetworkInfo不为空，不然当没有网络连接时会造成程序崩溃
			if(mNetworkInfo != null && mNetworkInfo.isConnected()){
				//判断当前是否为有用的WIFI连接
				if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
					return true;  
				} 
			}
		}  
		return false;  
	}

	@SuppressLint("HardwareIds")
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	/**
	 * Return the basic service set identifier (BSSID) of the current access
	 * point. The BSSID may be {@code null} if there is no network currently
	 * connected.
	 * 
	 * @return the BSSID, in the form of a six-byte MAC address:
	 *         {@code XX:XX:XX:XX:XX:XX}
	 */
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * Each configured network has a unique small integer ID, used to identify
	 * the network when performing operations on the supplicant. This method
	 * returns the ID for the currently connected network.
	 * 
	 * @return the network ID, or -1 if there is no currently connected network
	 */
	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * Function: 得到wifiInfo的所有信息<br>
	 * @return<br>
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}	

	/**
	 * Function: 打开wifi功能<br>
	 * @return true:打开成功；false:打开失败<br>
	 */
	public boolean openWifi() {
		boolean bRet = true;

		if (!mWifiManager.isWifiEnabled()) {
			bRet = mWifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	/**
	 * Function: 将int类型的IP转换成字符串形式的IP
	 */
	public String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 读取linux文件
	 * 获取链接的设备IP
	 * */
	public static ArrayList<String> getConnectedIP() {// 获取连接在服务器上的所有设备
		ArrayList<String> connectedIP = new ArrayList<String>();
		// 读取Linux文件系统中的某个文件

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectedIP;
	}

}
