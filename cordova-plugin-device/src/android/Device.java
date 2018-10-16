/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */
package org.apache.cordova.device;

import java.util.TimeZone;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class Device extends CordovaPlugin {
	public static final String TAG = "Device";

	public static String platform; // Device OS
	public static String uuid; // Device UUID

	private static final String ANDROID_PLATFORM = "Android";
	private static final String AMAZON_PLATFORM = "amazon-fireos";
	private static final String AMAZON_DEVICE = "Amazon";
	private static final int READ_PHONE_STATE = 0;
	private CallbackContext callbackContext;

	/**
	 * Constructor.
	 */
	public Device() {
	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 *
	 * @param cordova
	 *            The context of the main Activity.
	 * @param webView
	 *            The CordovaWebView Cordova is running in.
	 */
	@Override
	public void onRequestPermissionResult(int requestCode,
			String[] permissions, int[] grantResults) throws JSONException {
		for (int r : grantResults) {
			if (r == PackageManager.PERMISSION_DENIED) {
				return;
			}
		}
		promptForDevice();
	}

	public void promptForDevice() throws JSONException {
		if (PermissionHelper.hasPermission(this,
				Manifest.permission.READ_PHONE_STATE)) {
			JSONObject r = new JSONObject();
			r.put("uuid", getUuid());
			r.put("version", this.getOSVersion());
			r.put("platform", this.getPlatform());
			r.put("model", this.getModel());
			r.put("manufacturer", this.getManufacturer());
			r.put("imei", this.getIMEI());
			callbackContext.success(r);

		} else {
			PermissionHelper.requestPermission(this, READ_PHONE_STATE,
					Manifest.permission.READ_PHONE_STATE);
		}
	}

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackContext
	 *            The callback id used when calling back into JavaScript.
	 * @return True if the action was valid, false if not.
	 */
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext=callbackContext;
		if (action.equals("getDeviceInfo")) {
			promptForDevice();
		} else {
			return false;
		}
		return true;
	}

	// --------------------------------------------------------------------------
	// LOCAL METHODS
	// --------------------------------------------------------------------------

	// 获取本地Imei号码
	private String getIMEI() {
		TelephonyManager systemService = (TelephonyManager) cordova
				.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = systemService.getDeviceId();
		return systemService.getDeviceId();
	}

	/**
	 * Get the OS name.
	 *
	 * @return
	 */
	public String getPlatform() {
		String platform;
		if (isAmazonDevice()) {
			platform = AMAZON_PLATFORM;
		} else {
			platform = ANDROID_PLATFORM;
		}
		return platform;
	}

	/**
	 * Get the device's Universally Unique Identifier (UUID).
	 *
	 * @return
	 */
	public String getUuid() {
		String uuid = Settings.Secure.getString(this.cordova.getActivity()
				.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		return uuid;
	}

	public String getModel() {
		String model = android.os.Build.MODEL;
		return model;
	}

	public String getProductName() {
		String productname = android.os.Build.PRODUCT;
		return productname;
	}

	public String getManufacturer() {
		String manufacturer = android.os.Build.MANUFACTURER;
		return manufacturer;
	}

	/**
	 * Get the OS version.
	 *
	 * @return
	 */
	public String getOSVersion() {
		String osversion = android.os.Build.VERSION.RELEASE;
		return osversion;
	}

	public String getSDKVersion() {
		@SuppressWarnings("deprecation")
		String sdkversion = android.os.Build.VERSION.SDK;
		return sdkversion;
	}

	public String getTimeZoneID() {
		TimeZone tz = TimeZone.getDefault();
		return (tz.getID());
	}

	/**
	 * Function to check if the device is manufactured by Amazon
	 *
	 * @return
	 */
	public boolean isAmazonDevice() {
		if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
			return true;
		}
		return false;
	}

}
