package com.ollybolly;

/**
 * 
 * page info : 메인 페이지
 * 설정 된 언어별 리스트 셋팅
 * @author sangsangdigital
 *
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ollybolly.db.DB;
import com.ollybolly.util.PreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

public class OllybollyMain extends Activity {
	/** Called when the activity is first created. */

	Common mCommon;
	private Context mContext;

	// 디비 매니저
	DB mDBManager;

	private ProgressDialog pDialog;

	private int mSelectedPosition;
	private View mCountryText;
	private ImageView mNation;

	private ImageView mImageBtn01 = null;
	private ImageView mImageBtn02 = null;
	private ImageView mLogo = null;

	private String[] mImageList = null;
	private String[] mImageListNames = null;

	private ImageAdapter coverImageAdapter = null;

	private boolean newBtn = false;

	private long diffDate;

	// 뷰 크기 변수
	int screenWidth, screenHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// 화면 해상도
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		// Log.e("Width", "Width:"+screenWidth+" Height:"+screenHeight);

		// ++실시간 네트워크 셋팅
		PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "0");

		setContentView(R.layout.main);

		// ++레이아웃 각 해상도에 따른 셋팅
		if (screenWidth == 1024) {
			LinearLayout linear01 = (LinearLayout) findViewById(R.id.Main_LinearLayout_01);
			linear01.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 150));

			LinearLayout linear02 = (LinearLayout) findViewById(R.id.Main_LinearLayout_02);
			linear02.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 320));

			LinearLayout linear03 = (LinearLayout) findViewById(R.id.Main_LinearLayout_03);
			linear03.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 100));
		}

		/**
		 * 1. 메인화면 : 갤럭시노트2, 갤럭시S3 (1280*720) - 레이아웃 위치조정
		 * @author op
		 */

		else if (screenWidth == 1280 && screenHeight == 720) {
			LinearLayout top = (LinearLayout) findViewById(R.id.Main_LinearLayout_01);
			top.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 230));

			LinearLayout bottom = (LinearLayout) findViewById(R.id.Main_LinearLayout_02);
			bottom.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 350));

			LinearLayout under = (LinearLayout) findViewById(R.id.Main_LinearLayout_03);
			under.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 120));
		}

		/**
		 * 1. 메인화면 : 갤럭시노트, 갤럭시 탭10인치 (1280*800) - 레이아웃 위치조정 원래는 screenWidth ==
		 * 1280 && screenHeight == 800으로 테스트 해야하지만 에뮬레이터상에서는 해상도가 이렇게 나온다..
		 * screenWidth == 1238 && screenHeight == 752
		 * @author op
		 */

		else if (screenWidth == 1280 && screenHeight == 800) {
			LinearLayout top = (LinearLayout) findViewById(R.id.Main_LinearLayout_01);
			top.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 250));

			LinearLayout bottom = (LinearLayout) findViewById(R.id.Main_LinearLayout_02);
			bottom.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 400));

			LinearLayout under = (LinearLayout) findViewById(R.id.Main_LinearLayout_03);
			under.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 100));
		}

		// ++SharedPreferences 값 가져오기 시작
		Common.langType = PreferenceUtil.getPreference(this,
				Common.KEY_LANGUAGE);
		// ++SharedPreferences 값 가져오기 끝

		// ++SharedPreferences 날짜값 가져오기 시작
		String myDate = PreferenceUtil.getPreference(this, Common.VAL_DATE);

		// Log.e("myDate",myDate);

		if (myDate.equals("")) {
			newBtn = true;
			SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd",
					Locale.KOREA);
			Date current = new Date();
			String time = formater.format(current);
			// ++ SharedPreference 셋팅하기 시작
			PreferenceUtil.putPreference(this, Common.VAL_DATE, time);
			// ++ SharedPreference 셋팅하기 끝
		} else {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd",
					Locale.KOREA);
			Date current = new Date();
			String time = formater.format(current);

			try {
				diffDate = mCommon.day2Day(myDate, time, "yyyy.MM.dd");
			} catch (Exception e) {
				Log.e("error", "error" + e.toString());
			}

			if (diffDate < 5) {
				newBtn = true;
			} else {
				newBtn = false;
			}
		}
		// ++SharedPreferences 값 가져오기 끝

		if (Common.langType.equals("en")) {
			ImageView iv1 = (ImageView) findViewById(R.id.Left_01);
			iv1.setBackgroundResource(R.drawable.icon_daumfn_en);

			ImageView iv2 = (ImageView) findViewById(R.id.Right_01);
			iv2.setBackgroundResource(R.drawable.icon_daum_en);
		}

		mImageBtn01 = (ImageView) findViewById(R.id.MainButton_01);

		mImageBtn01.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {

				Intent intent = new Intent(OllybollyMain.this,
						OllybollySetting.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); // 위의 intent작업들을 시작!!
			}
		});

		mImageBtn02 = (ImageView) findViewById(R.id.MainButton_02);

		mImageBtn02.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {

				Intent intent = new Intent(OllybollyMain.this,
						OllybollyMyBox.class);
				intent.putExtra(Common.VAL_NATION, "special"); // 클릭했을때 해당 국가
																// 파라미터값 전달
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); // 위의 intent작업들을 시작!!

			}
		});

		// ++디비생성
		mContext = this;
		mDBManager = DB.getInstance(mContext);

		int tableCoun2 = mDBManager.getTableCount(DB.T_NAME_COUNTRY);

		mImageList = new String[tableCoun2];
		mImageListNames = new String[tableCoun2];

		mCommon = new Common();
		mCommon.xmlVersionList = mDBManager.getVersionList();

		// int mCountyListCount = mDBManager.getCountryCount();

		// Log.e("mCountyListCount",""+mCountyListCount);

		Cursor crsCountry = mDBManager.getCountryList();

		int i = 0;
		while (crsCountry.moveToNext()) {
			mImageList[i] = "data/data/com.ollybolly/files/"
					+ crsCountry.getString(1) + mCommon.xmlVersionList.get(6);
			mImageListNames[i] = "data/data/com.ollybolly/files/"
					+ crsCountry.getString(1) + mCommon.xmlVersionList.get(5);
			mCommon.xmlCountryEnglish.add(crsCountry.getString(3));

			// Log.e("mImageList[i]",mImageList[i]);
			i++;
		}

		crsCountry.close();

		OllybollyMainFlow coverFlow = (OllybollyMainFlow) findViewById(R.id.cover_flow);
		coverFlow.setAdapter(new ImageAdapter(this));
		coverImageAdapter = new ImageAdapter(this);

		// set to show shadow images (we call it as reflected images)
		// this method doesn't work originally (maybe need to modify)
		coverImageAdapter.createReflectedImages(); // original method
		coverFlow.setAdapter(coverImageAdapter);

		// Sets the spacing between items(images) in a Gallery
		coverFlow.setSpacing(-90); // original value

		// Set Selected Images at starting Gallery (number is index of
		// selection)
		coverFlow.setSelection(mSelectedPosition, true); // original method

		// Set persistence time of animation
		coverFlow.setAnimationDuration(500); // original value

		// set Zoom level
		coverFlow.setMaxZoom(-120); // original value

		// set Rotation angle of images - 이미지 이동할때 효과
		// coverFlow.setMaxRotationAngle(180); // optimal value
		// coverFlow.setMaxRotationAngle(240); // test value (deprecated)

		coverFlow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Log.e("클릭","클릭"+position);
				mSelectedPosition = position;
				createThreadAndDialog();
			}
		});

		coverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedPosition = position;

				Bitmap mationImage = BitmapFactory
						.decodeFile(mImageListNames[mSelectedPosition]);
				mNation.setImageBitmap(mationImage);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mCountryText = ((ViewStub) findViewById(R.id.country_hidden)).inflate();
		mCountryText.setVisibility(View.VISIBLE);
		mNation = (ImageView) findViewById(R.id.nation);
		
		System.out.println(mImageListNames[mSelectedPosition]);

		Bitmap mationImage = BitmapFactory
				.decodeFile(mImageListNames[mSelectedPosition]);
		mNation.setImageBitmap(mationImage);

	}

	public class ImageAdapter extends BaseAdapter {

		int mGalleryItemBackground;

		private ImageView[] mImages;

		public ImageAdapter(Context c) {
			super();
			mContext = c;
			mImages = new ImageView[mImageList.length];
		}

		public boolean createReflectedImages() {
			// The gap we want between the reflection and the original image
			final int reflectionGap = 4;

			final int widthGap = 40;
			final int heightGap = 40;

			final int originGap = 20;

			Bitmap newIcon = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_new2);

			int index = 0;
			for (String imageId : mImageList) {
				// Bitmap originalImage =
				// BitmapFactory.decodeResource(getResources(), imageId);

				Bitmap originalImage = BitmapFactory.decodeFile(imageId);

				//int width = originalImage.getWidth();
				//int height = originalImage.getHeight();
				
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();
				
				// This will not scale but will flip on the Y axis
				Matrix matrix = new Matrix();
				matrix.preScale(1, -1); // original code

				// Create a Bitmap with the flip matrix applied to it.
				// We only want the bottom half of the image
				Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

				// Create a new bitmap with same width but taller to fit
				// reflection
				Bitmap bitmapWithReflection = Bitmap.createBitmap(width
						+ widthGap, (height + height / 2) + heightGap,
						Config.ARGB_8888);

				// Create a new Canvas with the bitmap that's big enough for
				// the image plus gap plus reflection
				Canvas canvas = new Canvas(bitmapWithReflection);

				// Draw in the original image
				canvas.drawBitmap(originalImage, originGap, originGap, null);
				// canvas.drawBitmap(originalImage, null, new Rect(10, 10,
				// 50+width, 50+height), null);

				// Bitmap bitmap =
				// BitmapFactory.decodeFile("data/data/com.ollybolly/files/androes.png");

				// ++테두리 선 그리기 시작
				Paint white = new Paint(Paint.ANTI_ALIAS_FLAG);
				white.setColor(getResources().getColor(R.color.white));

				// ++상단
				canvas.drawRect(0 + originGap, 0 + originGap,
						width + originGap, 5 + originGap, white);
				// ++하단
				canvas.drawRect(0 + originGap, height - 5 + originGap, width
						+ originGap, height + originGap, white);
				// ++왼쪽
				canvas.drawRect(0 + originGap, 0 + originGap, 5 + originGap,
						height + originGap, white);
				// ++오른쪽
				canvas.drawRect(width - 5 + originGap, 0 + originGap, width
						+ originGap, height + originGap, white);
				// ++테두리 선 그리기 끝

				// Draw in the gap
				Paint deafaultPaint = new Paint();
				canvas.drawRect(0 + originGap, height + originGap, width
						+ originGap, height + reflectionGap + originGap,
						deafaultPaint);
				// Draw in the reflection
				canvas.drawBitmap(reflectionImage, 0 + originGap, height
						+ reflectionGap + originGap, null);

				// ++new 아이콘 붙이기
				if (newBtn) {
					canvas.drawBitmap(newIcon, 0, 5, null);
				}

				// Create a shader that is a linear gradient that covers the
				// reflection
				Paint paint = new Paint();
				LinearGradient shader = new LinearGradient(0 + originGap,
						originalImage.getHeight() + originGap, 0 + originGap,
						bitmapWithReflection.getHeight() + reflectionGap
								+ originGap, 0x70ffffff, 0x00ffffff,
						TileMode.CLAMP);
				// Set the paint to use this shader (linear gradient)
				paint.setShader(shader);
				// Set the Transfer mode to be porter duff and destination in
				paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
				// Draw a rectangle using the paint with our linear gradient
				canvas.drawRect(0 + originGap, height + originGap, width
						+ originGap, bitmapWithReflection.getHeight()
						+ reflectionGap + originGap, paint);

				ImageView imageView = new ImageView(mContext);
				imageView.setImageBitmap(bitmapWithReflection);

				/**
				 * 1. 메인화면 - 국가이미지 비율조정
				 * @author op
				 */

				if (screenWidth == 1280 && screenHeight == 720) {
					imageView
							.setLayoutParams(new OllybollyMainFlow.LayoutParams(
									400, 400));
				} else if (screenWidth == 1280 && screenHeight == 800) {
					imageView
							.setLayoutParams(new OllybollyMainFlow.LayoutParams(
									400, 400));
				} else {
					// set size of Images
					imageView
							.setLayoutParams(new OllybollyMainFlow.LayoutParams(
									250, 250)); // original value
					// imageView.setLayoutParams(new CoverFlow.LayoutParams(180,
					// 180)); // custom value
					// imageView.setScaleType(ScaleType.MATRIX); // original
					// code (deprecated)
				}

				// set aspect of Images (this setting is needed in order to use
				// reflected images (!)
				imageView.setScaleType(ScaleType.CENTER_INSIDE); // custom code
				mImages[index++] = imageView;

			}
			return true;
		}

		public int getCount() {
			return mImageList.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			// Use this code if you want to load from resources
			/*
			 * ImageView i = new ImageView(mContext);
			 * i.setImageResource(mImageIds[position]);
			 * 
			 * // set size of Image // i.setLayoutParams(new
			 * CoverFlow.LayoutParams(130, 130)); // original value
			 * i.setLayoutParams(new CoverFlow.LayoutParams(120, 180)); //
			 * custom code i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); //
			 * original code
			 * 
			 * //Make sure we set anti-aliasing otherwise we get jaggies
			 * BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			 * drawable.setAntiAlias(true); return i;
			 */

			return mImages[position];
		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}

	void createThreadAndDialog() {

		// Log.e("mCommon.xmlCountryEnglish[i]",mCommon.xmlCountryEnglish[mSelectedPosition]);
		pDialog = ProgressDialog.show(OllybollyMain.this, "", "loading", true,
				false);

		Thread thread = new Thread(new Runnable() {
			public void run() {

				try {

					// * 각 나라별 리스트 뽑아오기

					Cursor crsItem = mDBManager.getCountryItem(
							"",
							" where media_location_en = '"
									+ mCommon.xmlCountryEnglish
											.get(mSelectedPosition) + "' ");
					// Cursor crsItem = mDBManager.getCountryItem("", "");

					String strFullName = "";
					String strFileName = "";

					int i = 0;
					while (crsItem.moveToNext()) {
						// Log.e("crsItem.getString(3)",crsItem.getString(3));
						String strImage = "";
						boolean isImage = false;

						// ++오리지널 이미지 다운 받기
						strFileName = mCommon.getReplaceFileOriginName(
								crsItem.getString(3), "F");

						strImage = "data/data/com.ollybolly/files/"
								+ strFileName;
						isImage = mCommon.checkIfFileExists(strImage);

						if (!isImage) {
							strFullName = mCommon.getReplaceFileOriginName(
									crsItem.getString(3), "");
							mCommon.HttpDown(strFullName, strFileName, mContext);
							// Log.e("isImage",""+isImage);
						} else {
							// Log.e("isImage",""+isImage);
						}

						// ++섬네일 이미지 다운 받기
						strFileName = mCommon.getReplaceFileThumbName(
								crsItem.getString(3), "F");

						strImage = "data/data/com.ollybolly/files/"
								+ strFileName;
						isImage = mCommon.checkIfFileExists(strImage);
						if (!isImage) {
							strFullName = mCommon.getReplaceFileThumbName(
									crsItem.getString(3), "");
							mCommon.HttpDown(strFullName, strFileName, mContext);
							// Log.e("isImage",""+isImage);
						} else {
							// Log.e("isImage",""+isImage);
						}

						// ++타이틀 이미지 다운 받기
						strFileName = mCommon.getReplaceFileTitleName(
								crsItem.getString(3), "F", Common.langType);

						strImage = "data/data/com.ollybolly/files/"
								+ strFileName;
						isImage = mCommon.checkIfFileExists(strImage);
						if (!isImage) {
							strFullName = mCommon.getReplaceFileTitleName(
									crsItem.getString(3), "", Common.langType);
							mCommon.HttpDown(strFullName, strFileName, mContext);
							// Log.e("isImage",""+isImage);
						} else {
							// Log.e("isImage",""+isImage);
						}

						// Log.e("crsItem",crsItem.getInt(0)+"=="+crsItem.getString(1)+"=="+crsItem.getString(2));
						i++;
					}

					crsItem.close();
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
					Log.e("error", "error" + e.toString());
					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
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

				Intent intent = new Intent(OllybollyMain.this,
						OllybollyList.class);
				intent.putExtra(Common.VAL_NATION,
						mCommon.xmlCountryEnglish.get(mSelectedPosition)); // 클릭했을때
																			// 해당
																			// 국가
																			// 파라미터값
																			// 전달
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); // 위의 intent작업들을 시작!!

				break;
			case 3:
				Toast.makeText(OllybollyMain.this, "fail", Toast.LENGTH_SHORT)
						.show();
				break;

			}
		}
	};

	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 시작--------------------------------------------
	public void onBackPressed() {
		clickBack();
		return;
	}

	private void clickBack() {

		String strMessage = "";
		String strMessageAlert = "";
		String strBtnOk = "";
		String strBtnCancel = "";

		if (Common.langType.equals("en")) {
			strMessage = "Quit";
			strMessageAlert = "Are you sure you want to quit?";
			strBtnOk = "ok";
			strBtnCancel = "cancel";
		} else {
			strMessage = "종료";
			strMessageAlert = "정말로 종료 하시겠습니까?";
			strBtnOk = "예";
			strBtnCancel = "아니오";
		}

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(strMessageAlert)
				.setCancelable(false)
				.setPositiveButton(strBtnOk,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								finish();

							}
						})
				.setNegativeButton(strBtnCancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'NO' Button
								dialog.cancel();
							}
						});

		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog

		alert.setTitle(strMessage);

		// Icon for AlertDialog

		alert.setIcon(R.drawable.alert_dialog_icon);

		alert.show();

	}
	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 끝--------------------------------------------

}