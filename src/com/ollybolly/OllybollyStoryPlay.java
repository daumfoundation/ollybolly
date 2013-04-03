package com.ollybolly;

/**
 * 
 * page info : 동화 실행 페이지
 * @author sangsangdigital
 *
 */

import com.ollybolly.util.PreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

public class OllybollyStoryPlay extends Activity {

	private ProgressDialog pDialog;
	private VideoView videoView = null;
	private MediaController mediaController = null;
	private Uri videoUri = null;

	private String strMessage = "";
	private String strMessageAlert = "";
	private String strBtnOk = "";
	private String strBtnCancel = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ++실시간 네트워크 셋팅
		PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "1");

		setContentView(R.layout.storyplay);
		checkIntenet();

	}

	public void checkIntenet() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = ni.isConnected();

		Intent intent = getIntent(); // 값을 가져오는 인텐트 객체생성
		// 가져온 값을 set해주는 부분
		String strMovieUrl = intent.getExtras().getString(Common.VAL_URL)
				.toString();

		videoView = (VideoView) findViewById(R.id.VideoView);
		mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				videoView.start();
			}

		});

		// Set video link (mp4 format )
		videoUri = Uri.parse(strMovieUrl);

		if (isWifiConn == false && isMobileConn == false) {
			if (Common.langType.equals("en")) {
				strMessage = "Internet Connection";
				strMessageAlert = "Please check your Internet connection.";
				strBtnOk = "close";
			} else {
				strMessage = "인터넷연결";
				strMessageAlert = "인터넷연결을 확인하세요.";
				strBtnOk = "닫기";
			}

			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					this);
			alert_internet_status.setTitle(strMessage);
			alert_internet_status.setMessage(strMessageAlert);
			alert_internet_status.setPositiveButton(strBtnOk,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 닫기
							finish();
						}
					});
			alert_internet_status.show();
		} else if (isWifiConn == false && isMobileConn == true) {
			if (Common.langType.equals("en")) {
				strMessage = "Alert";
				strMessageAlert = "You are connecting to 3G network. Some data fees can be charged.  Do you want to continue?";
				strBtnOk = "continue";
				strBtnCancel = "stop";
			} else {
				strMessage = "알림";
				strMessageAlert = "3G에서 사용시 데이터요금이 부과됩니다. 계속 진행하시겠습니까?";
				strBtnOk = "진행";
				strBtnCancel = "중지";
			}

			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					this);
			alert_internet_status.setTitle(strMessage);
			alert_internet_status.setMessage(strMessageAlert);
			alert_internet_status.setCancelable(false);
			alert_internet_status.setPositiveButton(strBtnOk,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 진행
							createThreadAndDialog();
						}
					});
			alert_internet_status.setNegativeButton(strBtnCancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 중지
							finish();
						}
					});

			alert_internet_status.show();
		} else {
			// Common.setLangType("en");
			createThreadAndDialog();
		}
	}

	void createThreadAndDialog() {

		// Log.e("mCommon.xmlCountryEnglish[i]",mCommon.xmlCountryEnglish[mSelectedPosition]);
		pDialog = ProgressDialog.show(OllybollyStoryPlay.this, "", "loading",
				true, false);

		Thread thread = new Thread(new Runnable() {
			public void run() {

				try {
					videoView.setMediaController(mediaController);
					videoView.setVideoURI(videoUri);
					videoView.requestFocus();
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
					Log.e("error", "error" + e.toString());
					// Toast.makeText(StoryPlay.this, e.getMessage(), 0).show();
				} finally {
					handler.sendEmptyMessage(2);

				}
				// ++ 로딩중 끝
			}
		});
		thread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				/*
				 * mTxtResult.setText(R.string.safe_name);
				 * mTxtResult.setVisibility(View.VISIBLE);
				 * mBtnGo.setVisibility(View.GONE);
				 */
				break;

			case 1:

				break;
			case 2:
				pDialog.dismiss();

				break;
			case 3:
				// Toast.makeText(StoryPlay.this, "fail", 0).show();
				break;

			}
		}
	};

	@Override
	public void onPause() {
		Log.d("shop", "onPause");
		super.onPause();
		videoView.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		// checkIntenet(mType);
	}

}
