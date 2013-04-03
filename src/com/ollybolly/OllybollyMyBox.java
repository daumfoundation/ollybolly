package com.ollybolly;

/**
 * 
 * page info : 즐겨찾기 페이지
 * @author sangsangdigital
 *
 */

import com.ollybolly.R;
import com.ollybolly.db.DB;
import com.ollybolly.util.PreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class OllybollyMyBox extends Activity implements
		ViewSwitcher.ViewFactory {

	DB mDBManager;
	Common mCommon;

	private Context mContext;

	private RelativeLayout mSwitcher;

	private int mSelectedIndex = 0;

	private AdapterView<?> mParent = null;
	private View mView = null;
	private long mId = 0;

	private ImageAdapter mImageAdapter;

	private ImageView title = null;
	private ImageView spcIv2 = null;
	private ImageView comIv2 = null;
	private View spcVs = null;
	private View comVs = null;

	private ImageButton vsImage01 = null;
	private ImageButton vsImage02 = null;
	private ImageButton vsImage03 = null;
	private ImageButton vsImage04 = null;
	private ImageButton vsImage05 = null;
	private ImageButton vsImage06 = null;

	private int[] storyBtn = new int[2];

	// 뷰 크기 변수
	private int screenWidth, screenHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

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

		mCommon = new Common();
		mContext = this;

		// ++디비생성
		mDBManager = DB.getInstance(this);
		mCommon.xmlVersionList = mDBManager.getVersionList();

		// ++SharedPreferences 값 가져오기 시작
		Common.langType = PreferenceUtil.getPreference(this,
				Common.KEY_LANGUAGE);
		// ++SharedPreferences 값 가져오기 끝

		try {

			/*
			 * Cursor clsMyItem = mDBManager.getMyBoxItem();
			 * clsMyItem.moveToFirst();
			 * 
			 * int clsMyItemCount = clsMyItem.getCount();
			 * 
			 * Log.e("testCount","=="+clsMyItemCount);
			 */
			// * 각 나라별 리스트 뽑아오기

			Cursor crsItem = mDBManager.getMyBoxItem();

			// int crsItemCount = crsItem.getCount();

			// Log.e("crsItemCount","crsItemCount=="+crsItemCount);

			// ++국가별 타이틀 이미지 셋팅
			mCommon.cmItemGuid.add(0);
			mCommon.cmItemImageList.add("");
			mCommon.cmItemThumbList.add("");
			mCommon.cmItemTitleImageList.add("");
			mCommon.cmItemUrlList.add("");
			mCommon.cmItemCountList.add("");
			mCommon.cmItemDescription.add("");
			mCommon.cmItemTitle.add("");
			mCommon.cmItemWriter.add("");
			mCommon.cmItemIllustrator.add("");
			mCommon.cmItemNationEn.add("");

			int i = 1;

			while (crsItem.moveToNext()) {

				mCommon.cmItemGuid.add(crsItem.getInt(0));
				mCommon.cmItemThumbList.add("data/data/com.ollybolly/files/"
						+ mCommon.getReplaceFileThumbName(crsItem.getString(3),
								"F"));

				// Log.e("mCommon.cmItemThumbList[i]",mCommon.cmItemThumbList[i]);
				mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"
						+ mCommon.getReplaceFileOriginName(
								crsItem.getString(3), "F"));

				if (Common.langType.equals("en")) {
					mCommon.cmItemTitleImageList
							.add("data/data/com.ollybolly/files/"
									+ mCommon.getReplaceFileTitleName(
											crsItem.getString(3), "F",
											Common.langType));
					mCommon.cmItemDescription.add(crsItem.getString(6));
					mCommon.cmItemTitle.add(crsItem.getString(8));
					mCommon.cmItemWriter.add(crsItem.getString(10));
					mCommon.cmItemIllustrator.add(crsItem.getString(12));

				} else {
					mCommon.cmItemTitleImageList
							.add("data/data/com.ollybolly/files/"
									+ mCommon.getReplaceFileTitleName(
											crsItem.getString(3), "F",
											Common.langType));
					mCommon.cmItemDescription.add(crsItem.getString(7));
					mCommon.cmItemTitle.add(crsItem.getString(9));
					mCommon.cmItemWriter.add(crsItem.getString(11));
					mCommon.cmItemIllustrator.add(crsItem.getString(13));
				}

				mCommon.cmItemUrlList.add(crsItem.getString(4));
				mCommon.cmItemCountList.add(crsItem.getString(5));
				mCommon.cmItemNationEn.add(crsItem.getString(2));

				i++;
			}

			crsItem.close();
		} catch (Exception e) {
			// handler.sendEmptyMessage(3);
			Log.e("error", "error" + e.toString());
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		} finally {
			// handler.sendEmptyMessage(2);

		}
		// ++ 로딩중 끝

		setContentView(R.layout.story_list_mybox);

		title = (ImageView) findViewById(R.id.title_mybox);

		if (Common.langType.equals("en")) {
			title.setBackgroundResource(R.drawable.title_archive_en);
		}

		ImageView ivHome = (ImageView) findViewById(R.id.home_mybox);
		ivHome.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {

				Intent intent = new Intent(OllybollyMyBox.this,
						OllybollyMain.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); // 위의 intent작업들을 시작!!

			}
		});

		mSwitcher = (RelativeLayout) findViewById(R.id.switcher_mybox);
		// mSwitcher.setAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in));
		// mSwitcher.setAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_out));
		// mSwitcher.setBackgroundResource(mImageIds[0]);
		spcIv2 = (ImageView) findViewById(R.id.spclisttitle_mybox);
		comIv2 = (ImageView) findViewById(R.id.listtitle_mybox);
		spcVs = ((ViewStub) findViewById(R.id.spcviewstub_list_mybox))
				.inflate();
		comVs = ((ViewStub) findViewById(R.id.comviewstub_list_mybox))
				.inflate();

		// ++해상도 별 좌표 셋팅
		if (screenWidth == 1024) {

			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv2.getLayoutParams());
			margin.setMargins(20, 380, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcVs.getLayoutParams());
			margin.setMargins(20, 480, 0, 0);
			spcVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			margin.setMargins(20, 360, 0, 0);
			comIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(comVs.getLayoutParams());
			margin.setMargins(20, 460, 0, 0);
			comVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));
		}

		else if (screenWidth == 1280 && screenHeight == 720) {

			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv2.getLayoutParams());
			margin.setMargins(20, 450, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 5. 스페셜 에디션 화면 - 메뉴버튼 비율 및 위치조정
			 * @author op
			 */

			android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
					880,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			special_under_menu.setMargins(10, 580, 0, 0);
			special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
			spcVs.setLayoutParams(special_under_menu);

			/**
			 * 3. 동화화면 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			margin.setMargins(20, 450, 0, 0);
			// margin.setMargins(300, 300, 0, 0);
			comIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 3. 동화화면 - 메뉴버튼 위치조정 - 메뉴버튼 3편보기버튼 추가 (추후)
			 * @author op
			 */

			android.widget.RelativeLayout.LayoutParams story_under_menu = new RelativeLayout.LayoutParams(
					765,
					150);
			story_under_menu.setMargins(10, 550, 0, 0);
			story_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
			comVs.setLayoutParams(story_under_menu);

			/**
			 * 7. 즐겨찾기화면 - “즐겨찾기” 텍스트 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(title.getLayoutParams());
			margin.setMargins(450, 50, 0, 0);
			title.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(ivHome.getLayoutParams());
			android.widget.RelativeLayout.LayoutParams home_button_params = new RelativeLayout.LayoutParams(
					110, 110);
			home_button_params.leftMargin = 20;
			home_button_params.topMargin = 50;
			ivHome.setLayoutParams(home_button_params);
		}

		else if (screenWidth == 1280 && screenHeight == 800) {

			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv2.getLayoutParams());
			margin.setMargins(20, 500, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 5. 스페셜 에디션 화면 - 메뉴버튼 비율 및 위치조정
			 * @author op
			 */

			android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
					880,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			special_under_menu.setMargins(10, 650, 0, 0);
			special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
			spcVs.setLayoutParams(special_under_menu);

			/**
			 * 3. 동화화면 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			margin.setMargins(20, 520, 0, 0);
			comIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 3. 동화화면 - 메뉴버튼 위치조정 - 메뉴버튼 3편보기버튼 추가 (추후)
			 * @author op
			 */

			android.widget.RelativeLayout.LayoutParams story_under_menu = new RelativeLayout.LayoutParams(
					750,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			story_under_menu.setMargins(10, 620, 0, 0);
			story_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
			comVs.setLayoutParams(story_under_menu);

			/**
			 * 7. 즐겨찾기화면 - “즐겨찾기” 텍스트 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(title.getLayoutParams());
			margin.setMargins(450, 50, 0, 0);
			title.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(ivHome.getLayoutParams());
			android.widget.RelativeLayout.LayoutParams home_button_params = new RelativeLayout.LayoutParams(
					110, 110);
			home_button_params.leftMargin = 20;
			home_button_params.topMargin = 50;
			ivHome.setLayoutParams(home_button_params);

		}

		// ++선택시 view 셋팅 시작
		try {

			comIv2.setVisibility(View.INVISIBLE);
			comVs.setVisibility(View.INVISIBLE);

			if (mSelectedIndex == 0) {
				spcIv2.setVisibility(View.INVISIBLE);
				spcVs.setVisibility(View.INVISIBLE);

			} else {

				spcIv2.setVisibility(View.VISIBLE);
				spcVs.setVisibility(View.VISIBLE);

				Bitmap mainTitle = BitmapFactory
						.decodeFile(mCommon.cmItemTitleImageList.get(0));
				BitmapDrawable drawableMainTitle = new BitmapDrawable(mainTitle);
				spcIv2.setBackgroundDrawable(drawableMainTitle);

				// mNation = (ImageView) findViewById(R.id.nation);
			}

		} catch (Exception e) {

			// TODO: handle exception
			e.printStackTrace();
		}
		// ++선택시 view 셋팅 끝

		try {

			// Bitmap itemImage =
			// BitmapFactory.decodeFile(mCommon.cmItemImageList[0]);
			// BitmapDrawable d =new BitmapDrawable(itemImage);
			// mSwitcher.setBackgroundDrawable(d);
			mSwitcher.setBackgroundResource(R.drawable.bg_archive);

		} catch (Exception e) {

			// TODO: handle exception
			e.printStackTrace();
		}

		ListView listview = (ListView) findViewById(R.id.listview_mybox);
		// ++라인 없애기
		listview.setDivider(null);

		/**
		 * 2. 나라화면 - 썸네일 이미지 비율조정
		 * @author op
		 */

		if (screenWidth == 1280 && screenHeight == 720) {
			android.widget.RelativeLayout.LayoutParams right_thumnail_frame = new RelativeLayout.LayoutParams(
					280,
					android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
			right_thumnail_frame.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			listview.setLayoutParams(right_thumnail_frame);
		} else if (screenWidth == 1280 && screenHeight == 800) {
			android.widget.RelativeLayout.LayoutParams right_thumnail_frame = new RelativeLayout.LayoutParams(
					280,
					android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
			right_thumnail_frame.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			listview.setLayoutParams(right_thumnail_frame);
		}

		mImageAdapter = new ImageAdapter(this);
		listview.setAdapter(mImageAdapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedIndex = position;
				mParent = parent;
				mView = view;
				mId = id;
				// Log.e("onItemClick","position="+position);
				// Log.e("parent.getCount()","parent.getCount()="+parent.getCount());
				LinearLayout linear = (LinearLayout) view
						.findViewById(R.id.story_main);
				linear.setBackgroundResource(R.drawable.bg_yellow);

				// Toast.makeText(Scratch.this, "Clicked _id="+id,
				// Toast.LENGTH_SHORT).show();
				// mSwitcher.setBackgroundResource(mImageIds[position]);

				// ++선택시 view 셋팅 시작
				if (mCommon.cmItemNationEn.get(mSelectedIndex)
						.equals("special")) { // ++스페셜일때
					try {
						comIv2.setVisibility(View.INVISIBLE);
						comVs.setVisibility(View.INVISIBLE);
						if (mSelectedIndex == 0) {
							spcVs.setVisibility(View.INVISIBLE);
							spcIv2.setVisibility(View.INVISIBLE);
							title.setVisibility(View.VISIBLE);

							if (Common.langType.equals("en")) {
								title.setBackgroundResource(R.drawable.title_archive_en);
							} else {
								title.setBackgroundResource(R.drawable.title_archive);
							}

							/*
							 * String mainTitleImage =
							 * "data/data/com.ollybolly/files/spc_bt_mov.png";
							 * 
							 * if(Common.langType.equals("en")){ mainTitleImage
							 * =
							 * "data/data/com.ollybolly/files/spc_bt_mov_en.png"
							 * ; } //Bitmap mainTitle =
							 * BitmapFactory.decodeFile(mainTitleImage);
							 * //BitmapDrawable drawableMainTitle =new
							 * BitmapDrawable(mainTitle);
							 * //spcIv.setBackgroundDrawable(drawableMainTitle);
							 * spcIv
							 * .setBackgroundResource(R.drawable.title_archive);
							 */

						} else {

							spcIv2.setVisibility(View.VISIBLE);
							spcVs.setVisibility(View.VISIBLE);
							title.setVisibility(View.INVISIBLE);

							vsImage01 = (ImageButton) findViewById(R.id.Button_01);
							vsImage02 = (ImageButton) findViewById(R.id.Button_02);
							vsImage03 = (ImageButton) findViewById(R.id.Button_03);
							vsImage04 = (ImageButton) findViewById(R.id.Button_04);

							if (Common.langType.equals("en")) {
								vsImage03
										.setBackgroundResource(R.drawable.bt_view_description_en);
								vsImage04
										.setBackgroundResource(R.drawable.bt_delete_en);
							} else {
								vsImage03
										.setBackgroundResource(R.drawable.bt_view_description);
								vsImage04
										.setBackgroundResource(R.drawable.bt_delete);
							}

							// ++각 이미지 버튼 셋팅하기

							String mItemUrl = mCommon.cmItemUrlList
									.get(mSelectedIndex);
							storyBtn = mCommon.getStoryBtin(Common.langType,
									mItemUrl,
									mCommon.cmItemCountList.get(position));

							vsImage01.setBackgroundResource(storyBtn[0]);
							if (storyBtn[1] != 0) {
								vsImage02.setBackgroundResource(storyBtn[1]);
							}

							/**
							 * 5. 스페셜 에디션 화면 - 메뉴버튼 비율 및 위치조정
							 * 
							 * @author op
							 */

							if (screenWidth == 1280 && screenHeight == 720) {
								// 스페셜 에디션 하단 메뉴 버튼1
								LinearLayout.LayoutParams button01 = new LinearLayout.LayoutParams(
										209, 82);
								vsImage01.setLayoutParams(button01);

								// 스페셜 에디션 하단 메뉴 버튼2
								LinearLayout.LayoutParams button02 = new LinearLayout.LayoutParams(
										209, 82);
								button02.leftMargin = 10;
								vsImage02.setLayoutParams(button02);

								// 스페셜 에디션 하단 메뉴 버튼3
								LinearLayout.LayoutParams button03 = new LinearLayout.LayoutParams(
										209, 82);
								button03.leftMargin = 10;
								vsImage03.setLayoutParams(button03);

								// 스페셜 에디션 하단 메뉴 버튼4
								LinearLayout.LayoutParams button04 = new LinearLayout.LayoutParams(
										209, 82);
								button04.leftMargin = 10;
								vsImage04.setLayoutParams(button04);
							} else if (screenWidth == 1280
									&& screenHeight == 800) {
								// 스페셜 에디션 하단 메뉴 버튼1
								LinearLayout.LayoutParams button01 = new LinearLayout.LayoutParams(
										209, 82);
								vsImage01.setLayoutParams(button01);

								// 스페셜 에디션 하단 메뉴 버튼2
								LinearLayout.LayoutParams button02 = new LinearLayout.LayoutParams(
										209, 82);
								button02.leftMargin = 10;
								vsImage02.setLayoutParams(button02);

								// 스페셜 에디션 하단 메뉴 버튼3
								LinearLayout.LayoutParams button03 = new LinearLayout.LayoutParams(
										209, 82);
								button03.leftMargin = 10;
								vsImage03.setLayoutParams(button03);

								// 스페셜 에디션 하단 메뉴 버튼4
								LinearLayout.LayoutParams button04 = new LinearLayout.LayoutParams(
										209, 82);
								button04.leftMargin = 10;
								vsImage04.setLayoutParams(button04);
							}

							vsImage01
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											String movieUrl = mCommon
													.getMovieUrl(
															mCommon.cmItemUrlList
																	.get(mSelectedIndex),
															"1");
											// Log.e("movieUrl",movieUrl);

											Intent intent = new Intent(
													OllybollyMyBox.this,
													OllybollyStoryPlay.class);
											intent.putExtra(Common.VAL_URL,
													movieUrl); // 클릭했을때 해당 국가
																// 파라미터값 전달
											startActivity(intent); // 위의
																	// intent작업들을
																	// 시작!!

										}
									});

							vsImage02
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											String movieUrl = mCommon
													.getMovieUrl(
															mCommon.cmItemUrlList
																	.get(mSelectedIndex),
															"2");
											Intent intent = new Intent(
													OllybollyMyBox.this,
													OllybollyStoryPlay.class);
											intent.putExtra(Common.VAL_URL,
													movieUrl); // 클릭했을때 해당 국가
																// 파라미터값 전달
											startActivity(intent); // 위의
																	// intent작업들을
																	// 시작!!

										}
									});

							vsImage03
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											okDialog(0);

										}
									});

							vsImage04
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {
											okDialog(1);

										}
									});

							if (mCommon.cmItemCountList.get(position).equals(
									"1")) {
								vsImage02.setVisibility(View.GONE);
							} else {
								vsImage02.setVisibility(View.VISIBLE);
							}

							/**
							 * 5. 스페셜 에디션 화면 - 스페셜제목(한글) 크기 및 위치조정 - 스페셜제목(영문)
							 * 짤림방지 및 위치조정
							 * @author op
							 */

							Bitmap special_mainTitle;
							BitmapDrawable drawableMainTitle;

							String fileName;

							fileName = mCommon.cmItemTitleImageList
									.get(position);

							Log.e("special fileNames ", "==" + fileName);
							
							/**
							 * 5. 스페셜 에디션 화면 - 1편보기만 있는 것은 흰색 영역 맞춰서 처리
							 * @author op 
							 */
							
							String fileNames = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
							
							if(fileNames.equals("/cruz1_t_en.png") || fileNames.equals("/moonsr1_t_en.png")){
								if (screenWidth == 1280 && screenHeight == 720) {
									android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
											650,
											90);
									special_under_menu.setMargins(20, 580, 0, 0);	
									special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
									spcVs.setLayoutParams(special_under_menu);
								}
								else if (screenWidth == 1280 && screenHeight == 800) {
									android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
											650,
											90);
									special_under_menu.setMargins(20, 650, 0, 0);	
									special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
									spcVs.setLayoutParams(special_under_menu);
								}
							}
							else {
								if (screenWidth == 1280 && screenHeight == 720) {
									android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											90);
									special_under_menu.setMargins(20, 580, 0, 0);	
									special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
									spcVs.setLayoutParams(special_under_menu);
								}
								else if (screenWidth == 1280 && screenHeight == 800) {
									android.widget.RelativeLayout.LayoutParams special_under_menu = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											90);
									special_under_menu.setMargins(20, 650, 0, 0);	
									special_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
									spcVs.setLayoutParams(special_under_menu);
								}
							}

							if(screenWidth == 1280 && screenHeight == 720) {
								if(fileNames.equals("/dilbar1_t_en.png")){
									special_mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar1_t_en_v);
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
								else if(fileNames.equals("/dilbar2_t_en.png")){
									special_mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar2_t_en_v);
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
								else {
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
							}
							else if (screenWidth == 1280 && screenHeight == 800){
								if(fileNames.equals("/dilbar1_t_en.png")){
									special_mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar1_t_en_v);
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
								else if(fileNames.equals("/dilbar2_t_en.png")){
									special_mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar2_t_en_v);
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
								else {
									special_mainTitle = BitmapFactory.decodeFile(fileName);
									special_mainTitle = imgResize(special_mainTitle,
											special_mainTitle.getWidth(),
											special_mainTitle.getHeight(), true, 2);
								}
							}
							else {
								special_mainTitle = BitmapFactory.decodeFile(fileName);
								//special_mainTitle = imgResize(special_mainTitle,
								//		special_mainTitle.getWidth(),
								//		special_mainTitle.getHeight(), true, 2);
							}
							
							drawableMainTitle = new BitmapDrawable(special_mainTitle);

							spcIv2.setBackgroundDrawable(drawableMainTitle);
						}

					} catch (Exception e) {

						// TODO: handle exception
						e.printStackTrace();
					}

				} else {

					spcIv2.setVisibility(View.INVISIBLE);
					spcVs.setVisibility(View.INVISIBLE);

					try {

						if (mSelectedIndex == 0) {
							comVs.setVisibility(View.INVISIBLE);
							comIv2.setVisibility(View.INVISIBLE);
							title.setVisibility(View.VISIBLE);

							if (Common.langType.equals("en")) {
								title.setBackgroundResource(R.drawable.title_archive_en);
							} else {
								title.setBackgroundResource(R.drawable.title_archive);
							}
						} else {

							comIv2.setVisibility(View.VISIBLE);
							comVs.setVisibility(View.VISIBLE);
							title.setVisibility(View.INVISIBLE);

							vsImage01 = (ImageButton) findViewById(R.id.ComButton_01);
							vsImage02 = (ImageButton) findViewById(R.id.ComButton_02);
							vsImage03 = (ImageButton) findViewById(R.id.ComButton_03);
							vsImage04 = (ImageButton) findViewById(R.id.ComButton_04);
							vsImage05 = (ImageButton) findViewById(R.id.ComButton_05);
							vsImage06 = (ImageButton) findViewById(R.id.ComButton_06);
							//vsImage06.setVisibility(View.GONE);

							if (mCommon.cmItemImageList.get(mSelectedIndex)
									.indexOf("1") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue);
								}
								Drawable alpha = vsImage06.getBackground();
								alpha.setAlpha(100);

								vsImage06.setEnabled(false);
								
								/*
								vsImage06
										.setOnClickListener(new View.OnClickListener() {
											// @Override
											public void onClick(View v) {
												mSelectedIndex = mSelectedIndex + 1;
												// listview.setSelection(mSelectedIndex);
												// mImageAdapter.notifyDataSetChanged();

												onItemClick(mParent, mView,
														mSelectedIndex, mId);

											}
										});
								*/
							} else if (mCommon.cmItemImageList.get(
									mSelectedIndex).indexOf("2") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before);
								}
								Drawable alpha = vsImage06.getBackground();
								alpha.setAlpha(100);

								vsImage06.setEnabled(false);
								/*
								vsImage06
										.setOnClickListener(new View.OnClickListener() {
											// @Override
											public void onClick(View v) {

												mSelectedIndex = mSelectedIndex - 1;
												// listview.setSelection(mSelectedIndex);
												// mImageAdapter.notifyDataSetChanged();

												onItemClick(mParent, mView,
														mSelectedIndex, mId);

											}
										});
								*/
							} else {
								vsImage06.setVisibility(View.GONE);
							}

							if (Common.langType.equals("en")) {
								vsImage01
										.setBackgroundResource(R.drawable.bt_korean_en);
								vsImage02
										.setBackgroundResource(R.drawable.bt_english_en);
								vsImage04
										.setBackgroundResource(R.drawable.bt_view_description_en);
								vsImage05
										.setBackgroundResource(R.drawable.bt_delete_en);
								//vsImage06.setBackgroundResource(R.drawable.bt_goto_continue_en);
							} else {
								vsImage01
										.setBackgroundResource(R.drawable.bt_korean);
								vsImage02
										.setBackgroundResource(R.drawable.bt_english);
								vsImage04
										.setBackgroundResource(R.drawable.bt_view_description);
								vsImage05
										.setBackgroundResource(R.drawable.bt_delete);
								//vsImage06.setBackgroundResource(R.drawable.bt_goto_continue);
							}

							Bitmap mainTitle = null;

							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN
												+ "='"
												+ mCommon.cmItemNationEn
														.get(mSelectedIndex)
												+ "' ") + "_bt_en.png");
							} else {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN
												+ "='"
												+ mCommon.cmItemNationEn
														.get(mSelectedIndex)
												+ "' ") + "_bt.png");
							}
							BitmapDrawable drawableMainTitle = new BitmapDrawable(
									mainTitle);

							vsImage03.setBackgroundDrawable(drawableMainTitle);

							vsImage01
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											String movieUrl = mCommon
													.getMovieUrl(
															mCommon.cmItemUrlList
																	.get(mSelectedIndex),
															"1");
											// Log.e("movieUrl",movieUrl);

											Intent intent = new Intent(
													OllybollyMyBox.this,
													OllybollyStoryPlay.class);
											intent.putExtra(Common.VAL_URL,
													movieUrl); // 클릭했을때 해당 국가
																// 파라미터값 전달
											startActivity(intent); // 위의
																	// intent작업들을
																	// 시작!!

										}
									});

							vsImage02
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											String movieUrl = mCommon
													.getMovieUrl(
															mCommon.cmItemUrlList
																	.get(mSelectedIndex),
															"3");
											// Log.e("movieUrl",movieUrl);

											Intent intent = new Intent(
													OllybollyMyBox.this,
													OllybollyStoryPlay.class);
											intent.putExtra(Common.VAL_URL,
													movieUrl); // 클릭했을때 해당 국가
																// 파라미터값 전달
											startActivity(intent); // 위의
																	// intent작업들을
																	// 시작!!
										}
									});

							vsImage03
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											String movieUrl = mCommon
													.getMovieUrl(
															mCommon.cmItemUrlList
																	.get(mSelectedIndex),
															"2");
											// Log.e("movieUrl",movieUrl);

											Intent intent = new Intent(
													OllybollyMyBox.this,
													OllybollyStoryPlay.class);
											intent.putExtra(Common.VAL_URL,
													movieUrl); // 클릭했을때 해당 국가
																// 파라미터값 전달
											startActivity(intent); // 위의
																	// intent작업들을
																	// 시작!!

										}
									});

							vsImage04
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {

											okDialog(0);

										}
									});

							vsImage05
									.setOnClickListener(new View.OnClickListener() {
										// @Override
										public void onClick(View v) {
											okDialog(1);

										}
									});

							String fileName;

							fileName = mCommon.cmItemTitleImageList
									.get(position);

							Log.e("fileName", "==" + fileName);

							mainTitle = BitmapFactory.decodeFile(fileName);

							/*
							 * 실제 서버에 있는 url을 요청하여 가장 큰 이미지를 로드해보는 테스트 진행
							 * 
							 * @author op
							 */

							// URL url = null;

							// String imageUrl =
							// "http://apis.ollybolly.org/appimg/picture/erkhii1_t@2x.png";

							// url = new URL(imageUrl);
							// Bitmap bitmap =
							// BitmapFactory.decodeStream(url.openConnection().getInputStream());
							// drawableMainTitle =new
							// BitmapDrawable(imgResize(mainTitle));
							
							if(screenWidth == 1280 && screenHeight == 720){
								mainTitle = imgResize(mainTitle,
										mainTitle.getWidth(),
										mainTitle.getHeight(), true, 2);
							}
							else if(screenWidth == 1280 && screenHeight == 800){
								mainTitle = imgResize(mainTitle,
										mainTitle.getWidth(),
										mainTitle.getHeight(), true, 2);
							}
							
							drawableMainTitle = new BitmapDrawable(mainTitle);

							comIv2.setBackgroundDrawable(drawableMainTitle);

							// mainTitle =
							// BitmapFactory.decodeFile(mCommon.cmItemTitleImageList.get(position));
							// drawableMainTitle =new BitmapDrawable(mainTitle);
							// comIv2.setBackgroundDrawable(drawableMainTitle);

							// mNation = (ImageView) findViewById(R.id.nation);
						}

					} catch (Exception e) {

						// TODO: handle exception
						e.printStackTrace();
					}

				}
				// ++선택시 view 셋팅 끝

				try {
					if (position == 0) {
						mSwitcher.setBackgroundResource(R.drawable.bg_archive);
					} else {
						Bitmap itemImage = BitmapFactory
								.decodeFile(mCommon.cmItemImageList
										.get(position));
						BitmapDrawable d = new BitmapDrawable(itemImage);
						mSwitcher.setBackgroundDrawable(d);
					}

				} catch (Exception e) {

					// TODO: handle exception
					e.printStackTrace();
				}

				// ++리스트 갱신
				mImageAdapter.notifyDataSetChanged();

			}

		});

	}

	/*
	 * 3. 동화화면 : image size 2배로 확대 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
	 * 
	 * @author op
	 */

	public Bitmap imgResize(Bitmap bitmap, int width, int height,
			boolean filter, int zoom) {
		bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * zoom,
				bitmap.getHeight() * zoom, true);
		return bitmap;
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);

		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);

		// Log.e("makeView","makeView");
		return i;
	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mCommon.cmItemGuid.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			// Log.e("getView","getView"+position);

			// LayoutInflater의 객체 inflater를 현재 context와 연결된 inflater로 초기화.
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

			// inflator객체를 이용하여 \res\laout\row.xml 파싱
			View row = (View) inflater.inflate(R.layout.row, null);

			// ++LinearLayout 배경 변경 하기

			if (position == mSelectedIndex) {
				LinearLayout linear = (LinearLayout) row
						.findViewById(R.id.story_main);
				linear.setBackgroundResource(R.drawable.bg_yellow);

				/**
				 * 2. 나라화면 - 썸네일 이미지 비율조정
				 * @author op
				 */

				if (screenWidth == 1280 && screenHeight == 720) {
					android.widget.LinearLayout.LayoutParams right_thumnail_bg_frame = new LinearLayout.LayoutParams(
							280, 180);
					linear.setLayoutParams(right_thumnail_bg_frame);
				}

				else if (screenWidth == 1280 && screenHeight == 800) {
					android.widget.LinearLayout.LayoutParams right_thumnail_bg_frame = new LinearLayout.LayoutParams(
							280, 200);
					linear.setLayoutParams(right_thumnail_bg_frame);
				}
			} else {
				// Log.e("here","here");
				LinearLayout linear = (LinearLayout) row
						.findViewById(R.id.story_main);
				linear.setBackgroundResource(R.drawable.bg_white);

				/**
				 * 2. 나라화면 - 썸네일 이미지 비율조정
				 * @author op
				 */

				if (screenWidth == 1280 && screenHeight == 720) {
					android.widget.LinearLayout.LayoutParams right_thumnail_bg_frame = new LinearLayout.LayoutParams(
							280, 180);
					linear.setLayoutParams(right_thumnail_bg_frame);
				}

				else if (screenWidth == 1280 && screenHeight == 800) {
					android.widget.LinearLayout.LayoutParams right_thumnail_bg_frame = new LinearLayout.LayoutParams(
							280, 200);
					linear.setLayoutParams(right_thumnail_bg_frame);
				}
			}

			// 아이디를 담은 공간 생성
			ImageView storyList = (ImageView) row.findViewById(R.id.story_list);
			// storyList.setBackgroundResource(mImageThumbList[position]);

			/**
			 * 2. 나라화면 - 썸네일 이미지 비율조정
			 * @author op
			 */

			if (screenWidth == 1280 && screenHeight == 720) {
				android.widget.LinearLayout.LayoutParams right_thumnail_image_frame = new LinearLayout.LayoutParams(
						270, 170);
				right_thumnail_image_frame.setMargins(5, 5, 0, 0);
				storyList.setLayoutParams(right_thumnail_image_frame);
			}

			else if (screenWidth == 1280 && screenHeight == 800) {
				android.widget.LinearLayout.LayoutParams right_thumnail_image_frame = new LinearLayout.LayoutParams(
						270, 190);
				right_thumnail_image_frame.setMargins(5, 5, 0, 0);
				storyList.setLayoutParams(right_thumnail_image_frame);
			}

			try {

				if (position == 0) {
					storyList.setBackgroundResource(R.drawable.icon_archive2);
				} else {
					Bitmap itemImage = BitmapFactory
							.decodeFile(mCommon.cmItemThumbList.get(position));
					BitmapDrawable d = new BitmapDrawable(itemImage);

					storyList.setScaleType(ImageView.ScaleType.FIT_XY);
					storyList.setBackgroundDrawable(d);
				}

			} catch (Exception e) {

				// TODO: handle exception
				e.printStackTrace();
			} finally {
			}

			// 커스터마이징 된 View 리턴.
			return row;

		}

	}

	// ++담아두기 다이얼로그 시작
	private void okDialog(int dialogId) {
		Dialog dialog = null;
		switch (dialogId) {
		case 1:

			String strMessage = "";
			String strMessageAlert = "";
			String strBtnOk = "";
			String strBtnCancel = "";
			if (Common.langType.equals("en")) {
				strMessage = "Delete";
				strMessageAlert = "Do you want to delete?";
				strBtnOk = "Delete";
				strBtnCancel = "Cancel";
			} else {
				strMessage = "동화삭제";
				strMessageAlert = "보관함에서 이 동화를 삭제 할까요?";
				strBtnOk = "삭제";
				strBtnCancel = "취소";
			}

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setTitle(strMessage);
			alt_bld.setMessage(strMessageAlert);
			alt_bld.setPositiveButton(strBtnOk,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 닫기

							// ++삭제 처리 하기
							mDBManager.deleteTable(
									DB.T_NAME_MYBOX,
									" WHERE guid = "
											+ mCommon.cmItemGuid
													.get(mSelectedIndex));

							Intent intent = new Intent(OllybollyMyBox.this,
									OllybollyMyBox.class); // 이것은 현재페이지에서 현재페이지
															// 클래스로..
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent); // 위의 intent작업들을 시작!!

						}
					});
			alt_bld.setNegativeButton(strBtnCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							// dialog.cancel();
						}
					});
			AlertDialog alert = alt_bld.create();

			alert.show();
			break;
		case 0:

			String strWriter = "글쓴이 | ";
			String strIllustrator = "그린이 | ";

			if (Common.langType.equals("en")) {
				strWriter = "Author | ";
				strIllustrator = "Illustrator | ";
			}

			OllybollyDialog.Builder customBuilder = new OllybollyDialog.Builder(
					OllybollyMyBox.this);
			customBuilder
					.setTitle(mCommon.cmItemTitle.get(mSelectedIndex))
					.setMessage(mCommon.cmItemDescription.get(mSelectedIndex))
					.setWriter(
							strWriter
									+ mCommon.cmItemWriter.get(mSelectedIndex))
					.setIllustrator(
							strIllustrator
									+ mCommon.cmItemIllustrator
											.get(mSelectedIndex))

			// .setNegativeButton("Cancel", new
			// DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// OllybollyList.this
			// .dismissDialog(0);
			// }
			// })
			// .setPositiveButton("Confirm", new
			// DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// })
			;
			dialog = customBuilder.create();

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			// lp.width = WindowManager.LayoutParams.FILL_PARENT;
			// lp.height = WindowManager.LayoutParams.FILL_PARENT;

			/**
			 * 4. 동화내용보기 화면 - 팝업창(한글) “글쓴이” 텍스트 띄어쓰기적용 - 팝업창(영문) “그린이”
			 * “illustraotr” 단어적용 - 팝업창(내용보기) 80% 비율조정
			 * @author op
			 */

			TextView title = (TextView) dialog.findViewById(R.id.title);
			TextView writer = (TextView) dialog.findViewById(R.id.writer);
			TextView illustrator = (TextView) dialog
					.findViewById(R.id.illustrator);
			TextView message = (TextView) dialog.findViewById(R.id.message);
			ScrollView popup_view = (ScrollView) dialog
					.findViewById(R.id.sview);
			LinearLayout dot_line = (LinearLayout)dialog.findViewById(R.id.dialog_dotted_line);

			android.widget.LinearLayout.LayoutParams popup_frame = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.FILL_PARENT, 380);
			android.widget.LinearLayout.LayoutParams dout = new LinearLayout.LayoutParams(
					1200, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			
			dot_line.setLayoutParams(dout);

			if (screenWidth == 1280 && screenHeight == 720) {
				popup_view.setLayoutParams(popup_frame);
				title.setTextSize(30);
				writer.setTextSize(25);
				illustrator.setTextSize(25);
				message.setTextSize(25);

				lp.width = 1250;
				lp.height = 700;
				lp.dimAmount = 0;
				lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
				dialog.getWindow().setAttributes(lp);
			}

			else if (screenWidth == 1280 && screenHeight == 800) {
				popup_view.setLayoutParams(popup_frame);
				title.setTextSize(30);
				writer.setTextSize(25);
				illustrator.setTextSize(25);
				message.setTextSize(20);

				lp.width = 1250;
				lp.height = 750;
				lp.dimAmount = 0;
				lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
				dialog.getWindow().setAttributes(lp);
			}

			dialog.show();
			break;
		default:
			break;
		}

	}

	// ++담아두기 다이얼로그 끝

	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 시작--------------------------------------------
	public void onBackPressed() {
		clickBack();
		return;
	}

	private void clickBack() {

		Intent intent = new Intent(OllybollyMyBox.this, OllybollyMain.class); // 이것은
																				// 현재페이지에서
																				// 현재페이지
																				// 클래스로..
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); // 위의 intent작업들을 시작!!

	}
	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 끝--------------------------------------------

}
