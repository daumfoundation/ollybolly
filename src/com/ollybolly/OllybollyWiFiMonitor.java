package com.ollybolly;

/**
 * 
 * page info : �ǽð����� ��Ʈ��ũ üũ
 * 3G�� ���� �� �������� ����
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

		// ��Ʈ���� ������ �Ͼ���� �߻��ϴ� �κ�
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (!wifiNetInfo.isConnected() && mobNetInfo.isConnected()) { // ++�������̿���
																			// 3G��
																			// ��ȯ

				// ++SharedPreferences �� �������� ����
				String strMovie = PreferenceUtil.getPreference(context,
						Common.KEY_MOVIE);
				// ++SharedPreferences �� �������� ��

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
