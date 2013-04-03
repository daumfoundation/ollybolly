package com.ollybolly;

/**
 * 
 * page info : 실시간으로 네트워크 체크
 * 3G로 변경 시 메인으로 보냄
 * @author sangsangdigital
 *
 */
import com.ollybolly.util.PreferenceUtil;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OllybollyWiFiMonitor extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		// 네트웍에 변경이 일어났을때 발생하는 부분
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (!wifiNetInfo.isConnected() && mobNetInfo.isConnected()) { // ++와이파이에서
																			// 3G로
																			// 전환

				// ++SharedPreferences 값 가져오기 시작
				String strMovie = PreferenceUtil.getPreference(context,
						Common.KEY_MOVIE);
				// ++SharedPreferences 값 가져오기 끝

				if (strMovie.equals("1")) {
					Intent notify = new Intent(context, OllybollyMain.class);
					notify.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					PendingIntent sender = PendingIntent.getActivity(context,
							0, notify, 0);
					try {
						sender.send();
					} catch (Exception ex) {

					}
				}
			}
		}
	}

}
