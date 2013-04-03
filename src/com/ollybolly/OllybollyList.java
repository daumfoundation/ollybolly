package com.ollybolly;

/**
 * 
 * page info : 동화 리스트 페이지
 * 선택 된 나라별 동화 리스트 페이지
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
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import android.widget.FrameLayout.LayoutParams;

public class OllybollyList extends Activity implements ViewSwitcher.ViewFactory {

	DB mDBManager;
	Common mCommon;

	private Context mContext;

	private RelativeLayout mSwitcher;

	private int mSelectedIndex = 0;

	private ImageAdapter mImageAdapter;
	private ImageView spcIv;
	private ImageView spcIv2;
	private ImageView comIv;
	private ImageView comIv2;
	private View spcVs;
	private View comVs;

	private ImageButton vsImage01 = null;
	private ImageButton vsImage02 = null;
	private ImageButton vsImage03 = null;
	private ImageButton vsImage04 = null;
	private ImageButton vsImage05 = null;
	private ImageButton vsImage06 = null;
	private ListView listview = null;

	private int[] storyBtn = new int[2];

	private boolean isSaveOk = false;

	private AdapterView<?> mParent = null;
	private View mView = null;
	private long mId = 0;

	// 뷰 크기 변수
	// private final int REF_WIDHT = 800;
	// private final int REF_HEIGHT = 600;
	private int screenWidth, screenHeight;
	private float RW, RH;
	private ScrollView popup_view;
	private String nationTitle;

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

		Intent intent = getIntent(); // 값을 가져오는 인텐트 객체생성
		// 가져온 값을 set해주는 부분
		mCommon.valNation = intent.getExtras().getString(Common.VAL_NATION)
				.toString();

		// ++SharedPreferences 값 가져오기 시작
		Common.langType = PreferenceUtil.getPreference(this,
				Common.KEY_LANGUAGE);
		// ++SharedPreferences 값 가져오기 끝

		try {
			// * 각 나라별 리스트 뽑아오기
			String mOrder = "";
			if (mCommon.valNation.equals("special")) {
				mOrder = " order by pubdate desc, " + DB.COLUMN_GUID + " desc ";
			}
			Cursor crsItem = mDBManager.getCountryItem(mOrder,
					" where media_location_en = '" + mCommon.valNation + "' ");

			// int crsItemCount = crsItem.getCount();

			// Log.e("crsItemCount","crsItemCount=="+crsItemCount);

			// ++국가별 타이틀 이미지 셋팅
			mCommon.cmItemGuid.add(0);
			mCommon.cmItemTitleImageList.add("");
			mCommon.cmItemUrlList.add("");
			mCommon.cmItemCountList.add("");
			mCommon.cmItemDescription.add("");
			mCommon.cmItemTitle.add("");
			mCommon.cmItemWriter.add("");
			mCommon.cmItemIllustrator.add("");
			mCommon.cmItemNationEn.add("");
			if (Common.langType.equals("en")) {
				mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"
						+ mDBManager.getCountryImage(" where "
								+ DB.COLUMN_LANGLUAGE_EN + "='"
								+ mCommon.valNation + "' ")
						+ mCommon.xmlVersionList.get(11));
			} else {
				mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"
						+ mDBManager.getCountryImage(" where "
								+ DB.COLUMN_LANGLUAGE_EN + "='"
								+ mCommon.valNation + "' ")
						+ mCommon.xmlVersionList.get(10));
			}
			mCommon.cmItemThumbList.add("data/data/com.ollybolly/files/"
					+ mDBManager.getCountryImage(" where "
							+ DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation
							+ "' ") + mCommon.xmlVersionList.get(9));

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

				i++;
			}

			/*
			 * for(int m = 0; m < mCommon.cmItemThumbList.length ; m++){
			 * Log.e("mCommon.cmItemThumbList[m]",mCommon.cmItemImageList[m]); }
			 */
			crsItem.close();
		} catch (Exception e) {
			// handler.sendEmptyMessage(3);
			Log.e("error", "error" + e.toString());
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		} finally {
			// handler.sendEmptyMessage(2);

		}
		// ++ 로딩중 끝

		setContentView(R.layout.story_list);

		// ++레이아웃 각 해상도에 따른 셋팅

		ImageView ivHome = (ImageView) findViewById(R.id.home);
		ivHome.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {

				Intent intent = new Intent(OllybollyList.this,
						OllybollyMain.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); // 위의 intent작업들을 시작!!

			}
		});

		mSwitcher = (RelativeLayout) findViewById(R.id.switcher);
		// mSwitcher.setAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in));
		// mSwitcher.setAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_out));
		// mSwitcher.setBackgroundResource(mImageIds[0]);
		spcIv = (ImageView) findViewById(R.id.spcmaintitle);
		spcIv2 = (ImageView) findViewById(R.id.spclisttitle);
		comIv = (ImageView) findViewById(R.id.maintitle);
		comIv2 = (ImageView) findViewById(R.id.listtitle);
		spcVs = ((ViewStub) findViewById(R.id.spcviewstub_list)).inflate();
		comVs = ((ViewStub) findViewById(R.id.comviewstub_list)).inflate();

		// ++해상도 별 좌표 셋팅
		if (screenWidth == 1024) {
			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv.getLayoutParams());
			margin.setMargins(400, 485, 0, 0);
			spcIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcIv2.getLayoutParams());
			margin.setMargins(20, 380, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcVs.getLayoutParams());
			margin.setMargins(20, 480, 0, 0);
			spcVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(comIv.getLayoutParams());
			margin.setMargins(400, 485, 0, 0);
			comIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			margin.setMargins(20, 360, 0, 0);
			comIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(comVs.getLayoutParams());
			margin.setMargins(20, 460, 0, 0);
			comVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));
		}

		else if (screenWidth == 1280 && screenHeight == 720) {
			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv.getLayoutParams());
			margin.setMargins(400, 485, 0, 0);
			spcIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcIv2.getLayoutParams());
			margin.setMargins(20, 450, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcVs.getLayoutParams());
			margin.setMargins(20, 580, 0, 0);
			spcVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));

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
			 * 2. 나라화면 - 나라명칭 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv.getLayoutParams());
			margin.setMargins(10, 600, 0, 0);
			//System.out.println((comIv.getLayoutParams().width) * 2);
			//System.out.println((comIv.getLayoutParams().height) * 2);
			//margin.width = (comIv.getLayoutParams().width) * 5/3;
			//margin.height =  (comIv.getLayoutParams().height) * 5/3; 
			margin.width = 1000;
			margin.height = 100;
			comIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 3. 동화화면 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			if(Common.langType.equals("en")){
				margin.setMargins(20, 420, 0, 0);	
			}
			else {
				margin.setMargins(20, 450, 0, 0);
			}
			// margin.setMargins(300, 300, 0, 0);
			comIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 3. 동화화면 - 메뉴버튼 위치조정 - 메뉴버튼 3편보기버튼 추가 (추후)
			 * @author op
			 */

			android.widget.RelativeLayout.LayoutParams story_under_menu = new RelativeLayout.LayoutParams(
					750,
					150);
			story_under_menu.setMargins(10, 550, 0, 0);
			story_under_menu.addRule(RelativeLayout.ALIGN_BOTTOM);
			comVs.setLayoutParams(story_under_menu);

			margin = new ViewGroup.MarginLayoutParams(ivHome.getLayoutParams());
			android.widget.RelativeLayout.LayoutParams home_button_params = new RelativeLayout.LayoutParams(
					110, 110);
			home_button_params.leftMargin = 20;
			home_button_params.topMargin = 50;
			ivHome.setLayoutParams(home_button_params);

		}

		else if (screenWidth == 1280 && screenHeight == 800) {
			MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
					spcIv.getLayoutParams());
			margin.setMargins(400, 485, 0, 0);
			spcIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcIv2.getLayoutParams());
			margin.setMargins(20, 500, 0, 0);
			spcIv2.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			margin = new ViewGroup.MarginLayoutParams(spcVs.getLayoutParams());
			margin.setMargins(20, 650, 0, 0);
			spcVs.setLayoutParams(new RelativeLayout.LayoutParams(margin));

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
			 * 2. 나라화면 - 나라명칭 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv.getLayoutParams());
			margin.setMargins(10, 670, 0, 0);
			margin.width = 1000;
			margin.height = 100;
			comIv.setLayoutParams(new RelativeLayout.LayoutParams(margin));

			/**
			 * 3. 동화화면 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
			 * @author op
			 */

			margin = new ViewGroup.MarginLayoutParams(comIv2.getLayoutParams());
			if(Common.langType.equals("en")){
				margin.setMargins(20, 490, 0, 0);	
			}
			else {
				margin.setMargins(20, 520, 0, 0);
			}
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

			margin = new ViewGroup.MarginLayoutParams(ivHome.getLayoutParams());
			android.widget.RelativeLayout.LayoutParams home_button_params = new RelativeLayout.LayoutParams(
					110, 110);
			home_button_params.leftMargin = 20;
			home_button_params.topMargin = 50;
			ivHome.setLayoutParams(home_button_params);

		}

		// ++선택시 view 셋팅 시작
		if (mCommon.valNation.equals("special")) {
			try {

				comIv.setVisibility(View.INVISIBLE);
				comIv2.setVisibility(View.INVISIBLE);
				comVs.setVisibility(View.INVISIBLE);

				if (mSelectedIndex == 0) {
					spcVs.setVisibility(View.INVISIBLE);
					spcIv2.setVisibility(View.INVISIBLE);
					spcIv.setVisibility(View.VISIBLE);

					String mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov.png";

					if (Common.langType.equals("en")) {
						mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov_en.png";
					}

					Bitmap mainTitle = BitmapFactory.decodeFile(mainTitleImage);
					BitmapDrawable drawableMainTitle = new BitmapDrawable(
							mainTitle);
					spcIv.setBackgroundDrawable(drawableMainTitle);

					/**
					 * 6. 스페셜 에디션 동화화면 - 제작영상 메뉴버튼 위치조정
					 * @author op
					 */

					if (screenWidth == 1280 && screenHeight == 720) {
						android.widget.RelativeLayout.LayoutParams special_movie_menu_button = new RelativeLayout.LayoutParams(
								513, 97);
						special_movie_menu_button.leftMargin = 380;
						special_movie_menu_button.topMargin = 580;
						spcIv.setLayoutParams(special_movie_menu_button);
					}

					else if (screenWidth == 1280 && screenHeight == 800) {
						android.widget.RelativeLayout.LayoutParams special_movie_menu_button = new RelativeLayout.LayoutParams(
								513, 97);
						special_movie_menu_button.leftMargin = 380;
						special_movie_menu_button.topMargin = 650;
						spcIv.setLayoutParams(special_movie_menu_button);
					}

				} else {

					spcIv.setVisibility(View.INVISIBLE);
					spcIv2.setVisibility(View.VISIBLE);
					spcVs.setVisibility(View.VISIBLE);
					
					System.out.println("special image : " + mCommon.cmItemTitleImageList.get(0));

					Bitmap mainTitle = BitmapFactory
							.decodeFile(mCommon.cmItemTitleImageList.get(0));
					BitmapDrawable drawableMainTitle = new BitmapDrawable(
							mainTitle);
					spcIv2.setBackgroundDrawable(drawableMainTitle);

					// mNation = (ImageView) findViewById(R.id.nation);
				}

			} catch (Exception e) {

				// TODO: handle exception
				e.printStackTrace();
			}

			spcIv.setOnClickListener(new View.OnClickListener() {
				// @Override
				public void onClick(View v) {

					Intent intent = new Intent(OllybollyList.this,
							OllybollyStoryPlay.class);
					intent.putExtra(Common.VAL_URL,
							"http://apis.ollybolly.org/video/special.mp4"); // 클릭했을때
					startActivity(intent); // 위의 intent작업들을 시작!!

				}

			});
		} else {

			spcIv.setVisibility(View.INVISIBLE);
			spcIv2.setVisibility(View.INVISIBLE);
			spcVs.setVisibility(View.INVISIBLE);

			try {

				if (mSelectedIndex == 0) {
					comVs.setVisibility(View.INVISIBLE);
					comIv2.setVisibility(View.INVISIBLE);
					comIv.setVisibility(View.VISIBLE);
					Bitmap mainTitle = null;
					
					nationTitle = intent.getExtras().getString(Common.VAL_NATION).toString();
					
					if(screenWidth == 1280 && screenHeight == 720) {
						if(nationTitle.equals("mongolia")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title3x);
							}
						}
						else if(nationTitle.equals("palestine")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title3x);
							}
						}
						else if(nationTitle.equals("iran")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title3x);
							}
						}
						else if(nationTitle.equals("lebanon")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title3x);
							}
						}
						else if(nationTitle.equals("vietnam")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title3x);
							}
						}
						else if(nationTitle.equals("uzbekistan")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title3x);
							}
						}
						else if(nationTitle.equals("thailand")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title3x);
							}
						}
						else if(nationTitle.equals("philippines")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title3x);
							}
						}
					}
					else if(screenWidth == 1280 && screenHeight == 800) {
						if(nationTitle.equals("mongolia")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title3x);
							}
						}
						else if(nationTitle.equals("palestine")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title3x);
							}
						}
						else if(nationTitle.equals("iran")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title3x);
							}
						}
						else if(nationTitle.equals("lebanon")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title3x);
							}
						}
						else if(nationTitle.equals("vietnam")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title3x);
							}
						}
						else if(nationTitle.equals("uzbekistan")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title3x);
							}
						}
						else if(nationTitle.equals("thailand")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title3x);
							}
						}
						else if(nationTitle.equals("philippines")){
							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title_en3x);
							} else {
								mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title3x);
							}
						}
					}
					else {
						if (Common.langType.equals("en")) {
							mainTitle = BitmapFactory
									.decodeFile("data/data/com.ollybolly/files/"
											+ mDBManager.getCountryImage(" where "
													+ DB.COLUMN_LANGLUAGE_EN + "='"
													+ mCommon.valNation + "' ")
											+ "_title_en.png");
						} else {
							mainTitle = BitmapFactory
									.decodeFile("data/data/com.ollybolly/files/"
											+ mDBManager.getCountryImage(" where "
													+ DB.COLUMN_LANGLUAGE_EN + "='"
													+ mCommon.valNation + "' ")
											+ "_title.png");
						}
					}

					BitmapDrawable drawableMainTitle = new BitmapDrawable(
							mainTitle);

					comIv.setBackgroundDrawable(drawableMainTitle);

				} else {

					comIv.setVisibility(View.INVISIBLE);
					comIv2.setVisibility(View.VISIBLE);
					comVs.setVisibility(View.VISIBLE);

					String fileName;

					fileName = mCommon.cmItemTitleImageList.get(0);

					Log.e("fileName", "==" + fileName);

					Bitmap mainTitle = BitmapFactory.decodeFile(fileName);
					//mainTitle = imgResize(mainTitle, mainTitle.getWidth(),
					//		mainTitle.getHeight(), true, 2);
					BitmapDrawable drawableMainTitle = new BitmapDrawable(
							mainTitle);

					comIv2.setBackgroundDrawable(drawableMainTitle);

					// mNation = (ImageView) findViewById(R.id.nation);
				}

			} catch (Exception e) {

				// TODO: handle exception
				e.printStackTrace();
			}

		}
		// ++선택시 view 셋팅 끝

		try {

			Bitmap itemImage = BitmapFactory.decodeFile(mCommon.cmItemImageList
					.get(0));
			BitmapDrawable d = new BitmapDrawable(itemImage);
			mSwitcher.setBackgroundDrawable(d);

		} catch (Exception e) {

			// TODO: handle exception
			e.printStackTrace();
		}

		listview = (ListView) findViewById(R.id.listview);
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
		// int test = listview.getScrollY();
		// int test = listview.getVerticalScrollbarWidth();
		// Log.e("test",test+"");
		// listview.setScroll

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedIndex = position;
				mParent = parent;
				mView = view;
				mId = id;

				Log.e("id", id + "==");
				// Log.e("onItemClick","position="+position);
				// Log.e("parent.getCount()","parent.getCount()="+parent.getCount());
				LinearLayout linear = (LinearLayout) view
						.findViewById(R.id.story_main);
				linear.setBackgroundResource(R.drawable.bg_yellow);

				// Toast.makeText(Scratch.this, "Clicked _id="+id,
				// Toast.LENGTH_SHORT).show();
				// mSwitcher.setBackgroundResource(mImageIds[position]);

				// ++해당 guid가 보관함에 저장되어 있는지 체크
				boolean isGuid = mDBManager.getGuidMybox(mCommon.cmItemGuid
						.get(mSelectedIndex));

				// ++선택시 view 셋팅 시작
				if (mCommon.valNation.equals("special")) { // ++스페셜일때
					try {
						comIv.setVisibility(View.INVISIBLE);
						comIv2.setVisibility(View.INVISIBLE);
						comVs.setVisibility(View.INVISIBLE);
						if (mSelectedIndex == 0) {
							spcVs.setVisibility(View.INVISIBLE);
							spcIv2.setVisibility(View.INVISIBLE);
							spcIv.setVisibility(View.VISIBLE);

							String mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov.png";

							if (Common.langType.equals("en")) {
								mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov_en.png";
							}
							Bitmap mainTitle = BitmapFactory
									.decodeFile(mainTitleImage);
							BitmapDrawable drawableMainTitle = new BitmapDrawable(
									mainTitle);
							spcIv.setBackgroundDrawable(drawableMainTitle);

						} else {

							spcIv.setVisibility(View.INVISIBLE);
							spcIv2.setVisibility(View.VISIBLE);
							spcVs.setVisibility(View.VISIBLE);

							vsImage01 = (ImageButton) findViewById(R.id.Button_01);
							vsImage02 = (ImageButton) findViewById(R.id.Button_02);
							vsImage03 = (ImageButton) findViewById(R.id.Button_03);
							vsImage04 = (ImageButton) findViewById(R.id.Button_04);

							if (Common.langType.equals("en")) {
								// vsImage01.setBackgroundResource(R.drawable.storyview_bt1_en);
								// vsImage02.setBackgroundResource(R.drawable.storyview_bt2_en);
								vsImage03
										.setBackgroundResource(R.drawable.bt_view_description_en);
								vsImage04
										.setBackgroundResource(R.drawable.bt_save_en);
							}

							// ++각 이미지 버튼 셋팅하기

							String mItemUrl = mCommon.cmItemUrlList
									.get(position);

							storyBtn = mCommon.getStoryBtin(Common.langType,
									mItemUrl,
									mCommon.cmItemCountList.get(position));

							vsImage01.setBackgroundResource(storyBtn[0]);

							if (storyBtn[1] != 0) {
								vsImage02.setBackgroundResource(storyBtn[1]);
							}

							if (isGuid) {
								Drawable alpha = vsImage04.getBackground();
								alpha.setAlpha(100);

								vsImage04.setEnabled(false);
							} else {
								Drawable alpha = vsImage04.getBackground();
								alpha.setAlpha(255);
								vsImage04.setEnabled(true);
							}

							/**
							 * 5. 스페셜 에디션 화면 - 메뉴위치조정
							 * @author op
							 */

							if (screenWidth == 1280 && screenHeight == 720) {
								LinearLayout.LayoutParams lout01 = new LinearLayout.LayoutParams(
										209, 82);
								vsImage01.setLayoutParams(lout01);

								LinearLayout.LayoutParams lout02 = new LinearLayout.LayoutParams(
										209, 82);
								lout02.leftMargin = 10;
								vsImage02.setLayoutParams(lout02);

								LinearLayout.LayoutParams lout03 = new LinearLayout.LayoutParams(
										209, 82);
								lout03.leftMargin = 10;
								vsImage03.setLayoutParams(lout03);

								LinearLayout.LayoutParams lout04 = new LinearLayout.LayoutParams(
										209, 82);
								lout04.leftMargin = 10;
								vsImage04.setLayoutParams(lout04);
							} else if (screenWidth == 1280
									&& screenHeight == 800) {
								LinearLayout.LayoutParams lout01 = new LinearLayout.LayoutParams(
										209, 82);
								vsImage01.setLayoutParams(lout01);

								LinearLayout.LayoutParams lout02 = new LinearLayout.LayoutParams(
										209, 82);
								lout02.leftMargin = 10;
								vsImage02.setLayoutParams(lout02);

								LinearLayout.LayoutParams lout03 = new LinearLayout.LayoutParams(
										209, 82);
								lout03.leftMargin = 10;
								vsImage03.setLayoutParams(lout03);

								LinearLayout.LayoutParams lout04 = new LinearLayout.LayoutParams(
										209, 82);
								lout04.leftMargin = 10;
								vsImage04.setLayoutParams(lout04);
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
													OllybollyList.this,
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
													OllybollyList.this,
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

							Bitmap mainTitle = null;
							BitmapDrawable drawableMainTitle;

							String fileName;

							fileName = mCommon.cmItemTitleImageList
									.get(position);

							Log.e("special fileName ", "==" + fileName);
							
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
									mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar1_t_en);
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
								else if(fileNames.equals("/dilbar2_t_en.png")){
									mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar2_t_en);
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
								else {
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
							}
							else if (screenWidth == 1280 && screenHeight == 800){
								if(fileNames.equals("/dilbar1_t_en.png")){
									mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar1_t_en_v);
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
								else if(fileNames.equals("/dilbar2_t_en.png")){
									mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.dilbar2_t_en_v);
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
								else {
									mainTitle = BitmapFactory.decodeFile(fileName);
									mainTitle = imgResize(mainTitle,
											mainTitle.getWidth(),
											mainTitle.getHeight(), true, 2);
								}
							}
							else {
								mainTitle = BitmapFactory.decodeFile(fileName);
								//mainTitle = imgResize(mainTitle,
								//		mainTitle.getWidth(),
								//		mainTitle.getHeight(), true, 2);
							}
							
							drawableMainTitle = new BitmapDrawable(mainTitle);

							spcIv2.setBackgroundDrawable(drawableMainTitle);
						}

					} catch (Exception e) {

						// TODO: handle exception
						e.printStackTrace();
					}

					spcIv.setOnClickListener(new View.OnClickListener() {
						// @Override
						public void onClick(View v) {

							Intent intent = new Intent(OllybollyList.this,
									OllybollyStoryPlay.class);
							intent.putExtra(Common.VAL_URL,
									"http://apis.ollybolly.org/video/special.mp4"); // 클릭했을때
							startActivity(intent); // 위의 intent작업들을 시작!!

						}

					});
				} else {

					spcIv.setVisibility(View.INVISIBLE);
					spcIv2.setVisibility(View.INVISIBLE);
					spcVs.setVisibility(View.INVISIBLE);

					try {

						if (mSelectedIndex == 0) {
							comVs.setVisibility(View.INVISIBLE);
							comIv2.setVisibility(View.INVISIBLE);
							comIv.setVisibility(View.VISIBLE);

							Bitmap mainTitle = null;
							
							if(screenWidth == 1280 && screenHeight == 720) {
								if(nationTitle.equals("mongolia")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title3x);
									}
								}
								else if(nationTitle.equals("palestine")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title3x);
									}
								}
								else if(nationTitle.equals("iran")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title3x);
									}
								}
								else if(nationTitle.equals("lebanon")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title3x);
									}
								}
								else if(nationTitle.equals("vietnam")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title3x);
									}
								}
								else if(nationTitle.equals("uzbekistan")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title3x);
									}
								}
								else if(nationTitle.equals("thailand")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title3x);
									}
								}
								else if(nationTitle.equals("philippines")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title3x);
									}
								}
							}
							else if(screenWidth == 1280 && screenHeight == 800) {
								if(nationTitle.equals("mongolia")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.mong_title3x);
									}
								}
								else if(nationTitle.equals("palestine")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.palestine_title3x);
									}
								}
								else if(nationTitle.equals("iran")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.iran_title3x);
									}
								}
								else if(nationTitle.equals("lebanon")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.lebanon_title3x);
									}
								}
								else if(nationTitle.equals("vietnam")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.vietnam_title3x);
									}
								}
								else if(nationTitle.equals("uzbekistan")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.uzbek_title3x);
									}
								}
								else if(nationTitle.equals("thailand")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.thai_title3x);
									}
								}
								else if(nationTitle.equals("philippines")){
									if (Common.langType.equals("en")) {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title_en3x);
									} else {
										mainTitle = BitmapFactory.decodeResource(getResources(), R.drawable.philippines_title3x);
									}
								}
							}
							else if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN + "='"
												+ mCommon.valNation + "' ")
										+ "_title_en.png");
							} else {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN + "='"
												+ mCommon.valNation + "' ")
										+ "_title.png");
							}
							BitmapDrawable drawableMainTitle = new BitmapDrawable(
									mainTitle);

							comIv.setBackgroundDrawable(drawableMainTitle);

						} else {

							comIv.setVisibility(View.INVISIBLE);
							comIv2.setVisibility(View.VISIBLE);
							comVs.setVisibility(View.VISIBLE);

							vsImage01 = (ImageButton) findViewById(R.id.ComButton_01);
							vsImage02 = (ImageButton) findViewById(R.id.ComButton_02);
							vsImage03 = (ImageButton) findViewById(R.id.ComButton_03);
							vsImage04 = (ImageButton) findViewById(R.id.ComButton_04);
							vsImage05 = (ImageButton) findViewById(R.id.ComButton_05);
							vsImage06 = (ImageButton) findViewById(R.id.ComButton_06);

							if (mCommon.cmItemImageList.get(mSelectedIndex)
									.indexOf("1") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue);
								}

								vsImage06.setVisibility(View.VISIBLE);

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
							} else if (mCommon.cmItemImageList.get(
									mSelectedIndex).indexOf("2") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before);
								}
								vsImage06.setVisibility(View.VISIBLE);

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
							} else if (mCommon.cmItemImageList.get(mSelectedIndex)
									.indexOf("3") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_continue);
								}

								vsImage06.setVisibility(View.VISIBLE);

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
							} else if (mCommon.cmItemImageList.get(
									mSelectedIndex).indexOf("4") > -1) {
								if (Common.langType.equals("en")) {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before_en);
								} else {
									vsImage06
											.setBackgroundResource(R.drawable.bt_goto_before);
								}
								vsImage06.setVisibility(View.VISIBLE);

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
										.setBackgroundResource(R.drawable.bt_save_en);
								// vsImage06.setBackgroundResource(R.drawable.bt_goto_continue_en);
							}

							if (isGuid) {
								Drawable alpha = vsImage05.getBackground();
								alpha.setAlpha(100);
								vsImage05.setEnabled(false);
							} else {
								Drawable alpha = vsImage05.getBackground();
								alpha.setAlpha(255);
								vsImage05.setEnabled(true);
							}

							Bitmap mainTitle = null;

							if (Common.langType.equals("en")) {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN + "='"
												+ mCommon.valNation + "' ")
										+ "_bt_en.png");
							} else {
								mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"
										+ mDBManager.getCountryImage(" where "
												+ DB.COLUMN_LANGLUAGE_EN + "='"
												+ mCommon.valNation + "' ")
										+ "_bt.png");
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
													OllybollyList.this,
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
													OllybollyList.this,
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
													OllybollyList.this,
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

							// mNation = (ImageView) findViewById(R.id.nation);
						}

					} catch (Exception e) {

						// TODO: handle exception
						e.printStackTrace();
					}

				}
				// ++선택시 view 셋팅 끝

				try {

					Bitmap itemImage = BitmapFactory
							.decodeFile(mCommon.cmItemImageList.get(position));
					BitmapDrawable d = new BitmapDrawable(itemImage);
					mSwitcher.setBackgroundDrawable(d);

				} catch (Exception e) {

					// TODO: handle exception
					e.printStackTrace();
				}

				// ++리스트 갱신
				mImageAdapter.notifyDataSetChanged();
				listview.setSelection(mSelectedIndex);
			}

		});

	}

	/*
	 * 3. 동화화면 : image size 2배로 확대 - 동화제목(한글) 크기 및 위치조정 - 동화제목(영문) 짤림방지 및 위치조정
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

			// inflator객체를 이용하여 \res\layout\row.xml 파싱
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

				Bitmap itemImage = BitmapFactory
						.decodeFile(mCommon.cmItemThumbList.get(position));
				BitmapDrawable d = new BitmapDrawable(itemImage);

				storyList.setScaleType(ImageView.ScaleType.FIT_XY);
				storyList.setBackgroundDrawable(d);

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

			isSaveOk = false;
			isSaveOk = mDBManager.insertMybox(mCommon.cmItemGuid
					.get(mSelectedIndex));

			String strMessage = "";
			String strMessageAlert = "";
			String strBtn = "";
			if (isSaveOk) {
				if (Common.langType.equals("en")) {
					strMessage = "Add Favorite";
					strMessageAlert = "The story adds successfully";
					strBtn = "close";
				} else {
					strMessage = "즐겨찾기";
					strMessageAlert = "동화가 추가되었습니다.";
					strBtn = "닫기";
				}
			} else {
				strMessage = "";
			}

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setTitle(strMessage);
			alt_bld.setMessage(strMessageAlert);
			alt_bld.setPositiveButton(strBtn,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 닫기

							if (mCommon.valNation.equals("special")) { // ++스페셜일때
								Drawable alpha = vsImage04.getBackground();
								alpha.setAlpha(100);
								vsImage04.setEnabled(false);
							} else {
								Drawable alpha = vsImage05.getBackground();
								alpha.setAlpha(100);
								vsImage05.setEnabled(false);
							}

						}
					});

			AlertDialog alert = alt_bld.create();

			// Icon for AlertDialog
			// alert.setIcon(R.drawable.title_download);

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
					OllybollyList.this);
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
			popup_view = (ScrollView) dialog
					.findViewById(R.id.sview);
			LinearLayout dot_line = (LinearLayout)dialog.findViewById(R.id.dialog_dotted_line);

			android.widget.LinearLayout.LayoutParams rout = new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.FILL_PARENT, 380);
			android.widget.LinearLayout.LayoutParams dout = new LinearLayout.LayoutParams(
					1200, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			
			dot_line.setLayoutParams(dout);

			if (screenWidth == 1280 && screenHeight == 720) {
				popup_view.setLayoutParams(rout);
				/*
				popup_view.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_UP)
							popup_view.requestDisallowInterceptTouchEvent(false);
				        else 
				        	popup_view.requestDisallowInterceptTouchEvent(false);
						return false;
					}
				});
				*/
				//popup_view.setBackgroundResource(R.drawable.inner_dialog_border);
				title.setTextSize(30);
				writer.setTextSize(25);
				illustrator.setTextSize(25);
				message.setTextSize(25);

				// lp.x=100;lp.y=100;
				lp.width = 1250;
				lp.height = 700;// lp.gravity=Gravity.TOP | Gravity.LEFT;
				lp.dimAmount = 0;
				lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
				dialog.getWindow().setAttributes(lp);
			}

			else if (screenWidth == 1280 && screenHeight == 800) {
				popup_view.setLayoutParams(rout);
				//popup_view.setBackgroundResource(R.drawable.inner_dialog_border);
				title.setTextSize(30);
				writer.setTextSize(25);
				illustrator.setTextSize(25);
				message.setTextSize(20);

				// lp.x=100;lp.y=100;
				lp.width = 1250;
				lp.height = 750;// lp.gravity=Gravity.TOP | Gravity.LEFT;
				lp.dimAmount = 0;
				lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
				dialog.getWindow().setAttributes(lp);
			}
			
	        popup_view.setOnClickListener(null);
	        popup_view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});

			dialog.show();
			break;
		default:
			break;
		}

	}

	// 레이아웃 설정
	LayoutParams calLayout(LayoutParams params) {
		params.width = (int) Math.ceil(params.width * RW);
		params.height = (int) Math.ceil(params.height * RH);

		params.setMargins((int) Math.ceil(params.leftMargin * RW),
				(int) Math.ceil(params.topMargin * RH),
				(int) Math.ceil((params.leftMargin + params.width) * RW),
				(int) Math.ceil((params.topMargin + params.height) * RH));

		return params;
	}

	// ++담아두기 다이얼로그 끝
	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 시작--------------------------------------------
	public void onBackPressed() {
		clickBack();
		return;
	}

	private void clickBack() {

		Intent intent = new Intent(OllybollyList.this, OllybollyMain.class); // 이것은
																				// 현재페이지에서
																				// 현재페이지
																				// 클래스로..
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); // 위의 intent작업들을 시작!!

	}
	// ---------------------------------------------------뒤로가기 버튼 오버라이드 구현
	// 끝--------------------------------------------
}
