package com.android.romatecamera.wifi;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import static android.os.Looper.getMainLooper;

public class HPSetup {

	private static final String SETUP_WIFIAP_METHOD = "setWifiApEnabled";
	Context context = null;
	WifiManager wifiManager = null;
	WifiConfiguration netConfig = new WifiConfiguration();
	
	static HPSetup hPaConnector = null;
	private Handler handler;


	public static HPSetup getInstance(Context context) {
		if (hPaConnector == null) {
			hPaConnector = new HPSetup();
			hPaConnector.context = context.getApplicationContext();
			hPaConnector.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		}
		return hPaConnector;
	}

	public void setupWifiAp(String name, String password,boolean status) throws Exception {
		
		if (name == null || "".equals(name)) {
			throw new Exception("the name of the wifiap is cannot be null");
		}
		handler = new Handler(getMainLooper());
		//若wifi打开，先关闭wifi
		if(wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(false);
		}
		
		Method setupMethod = wifiManager.getClass().getMethod(SETUP_WIFIAP_METHOD, WifiConfiguration.class, boolean.class);
		try {
		// 设置wifi热点名称
		netConfig.SSID = name;
		// 设置wifi热点密码
		netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		netConfig.preSharedKey = password;
		netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		if (status){
			netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}else{
			netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		}
		netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
			Log.d("wchao","wifi hot==="+status);
			//保存ap设置
			Method methodsave = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
			methodsave.invoke(wifiManager, netConfig);
			//获取ConnectivityManager对象，便于后面使用
			ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			Field field = connManager.getClass().getDeclaredField("TETHERING_WIFI");
			field.setAccessible(true);
			int mTETHERING_WIFI = (int)field.get(connManager);
			Field iConnMgrField = connManager.getClass().getDeclaredField("mService");
			iConnMgrField.setAccessible(true);
			Object iConnMgr = iConnMgrField.get(connManager);
			Class<?> iConnMgrClass = Class.forName(iConnMgr.getClass().getName());
			Method startTethering = iConnMgrClass.getMethod("startTethering", int.class, ResultReceiver.class, boolean.class);
			startTethering.invoke(iConnMgr, mTETHERING_WIFI, new ResultReceiver(handler) {
				@Override
				protected void onReceiveResult(int resultCode, Bundle resultData) {
					super.onReceiveResult(resultCode, resultData);
				}
			}, status);

		}else {
			Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			method.invoke(wifiManager, netConfig, status);
		}
	} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
		e.printStackTrace();
	}
		/*
		if (password != null) {
			if (password.length() < 8) {
				throw new Exception("the length of wifi password must be 8 or longer");
			}
			// 设置wifi热点密码
			netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			netConfig.preSharedKey = password;
			if (status){
				Toast.makeText(context,"hotname="+name+"\npassword="+password,Toast.LENGTH_LONG).show();
			}
		}*/
		//setupMethod.invoke(wifiManager, netConfig, status);
	}
	
	public int checkWifiState() {
		return wifiManager.getWifiState();
	}
	
}
