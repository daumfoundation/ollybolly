package com.ollybolly;

/**
 * 
 * page info : 동화 리스트 페이지
 * 선택 된 나라별 동화 리스트 페이지
 * @author sangsangdigital
 *
 */

import java.util.ArrayList;

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
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.FrameLayout.LayoutParams;



public class OllybollyList extends Activity implements
        ViewSwitcher.ViewFactory {
		
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
		private ImageButton vsImage03  = null;
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
		private final int REF_WIDHT = 800;
		private final int REF_HEIGHT = 600;
		private int screenWidth, screenHeight;
		private float RW, RH;
			
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
		((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth  = displayMetrics.widthPixels;
		//int screenHeight = displayMetrics.heightPixels;
		//Log.e("Width", "Width:"+screenWidth+" Height:"+screenHeight);
		
		//++실시간 네트워크 셋팅
		PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "0");
			   
		mCommon = new Common();    
		mContext = this;
		  
		//++디비생성
		mDBManager =DB.getInstance(this);
		mCommon.xmlVersionList = mDBManager.getVersionList();    
		            
		Intent intent = getIntent();  // 값을 가져오는 인텐트 객체생성
		// 가져온 값을 set해주는 부분
		mCommon.valNation = intent.getExtras().getString(Common.VAL_NATION).toString();
		 
		//++SharedPreferences 값 가져오기 시작
		Common.langType  = PreferenceUtil.getPreference(this, Common.KEY_LANGUAGE);
  	    //++SharedPreferences 값 가져오기 끝
		
		try{
		        //* 각 나라별 리스트 뽑아오기
				String mOrder = "";
				 if(mCommon.valNation.equals("special")){
					 mOrder = " order by pubdate desc, "+DB.COLUMN_GUID+" asc ";
				 }
				 Cursor crsItem = mDBManager.getCountryItem(mOrder, " where media_location_en = '"+mCommon.valNation+"' ");

				 Cursor crsAllItem = mDBManager.getCountryAllItem();
				 
			 
			 int crsAllItemCount = crsAllItem.getCount();
			 int crsItemCount = crsItem.getCount();
			 
			 //Log.e("crsItemCount","crsItemCount=="+crsItemCount);
			
			//++국가별 타이틀 이미지 셋팅
			mCommon.cmItemGuid.add(0);
			mCommon.cmItemTitleImageList.add("");
			mCommon.cmItemUrlList.add("");
			mCommon.cmItemCountList.add("");
			mCommon.cmItemDescription.add("");
			mCommon.cmItemTitle.add("");
			mCommon.cmItemWriter.add("");
			mCommon.cmItemIllustrator.add("");
			mCommon.cmItemNationEn.add("");			
			if(Common.langType.equals("en")){
				mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+mCommon.xmlVersionList.get(11));
			}else{
				mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+mCommon.xmlVersionList.get(10));
			} 
			mCommon.cmItemThumbList.add("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+mCommon.xmlVersionList.get(9));
			
			

			
		    	
			  int i = 1;
			  
			  while (crsItem.moveToNext()) {
				  
			 	  mCommon.cmItemGuid.add(crsItem.getInt(0));
				  mCommon.cmItemThumbList.add("data/data/com.ollybolly/files/"+mCommon.getReplaceFileThumbName(crsItem.getString(3), "F"));
				  
				  //Log.e("mCommon.cmItemThumbList[i]",mCommon.cmItemThumbList[i]);
				  mCommon.cmItemImageList.add("data/data/com.ollybolly/files/"+mCommon.getReplaceFileOriginName(crsItem.getString(3), "F"));
				  
				  if(Common.langType.equals("en")){
					mCommon.cmItemTitleImageList.add("data/data/com.ollybolly/files/"+mCommon.getReplaceFileTitleName(crsItem.getString(3), "F",Common.langType));
					mCommon.cmItemDescription.add(crsItem.getString(6));
					mCommon.cmItemTitle.add(crsItem.getString(8));
					mCommon.cmItemWriter.add(crsItem.getString(10));
					mCommon.cmItemIllustrator.add(crsItem.getString(12));
						
				  }else{
					mCommon.cmItemTitleImageList.add("data/data/com.ollybolly/files/"+mCommon.getReplaceFileTitleName(crsItem.getString(3), "F",Common.langType));
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
		       for(int m = 0; m < mCommon.cmItemThumbList.length ; m++){
		    	   Log.e("mCommon.cmItemThumbList[m]",mCommon.cmItemImageList[m]);
		       }
		       */
		       crsItem.close();
		}catch(Exception e){
			//handler.sendEmptyMessage(3);
			Log.e("error","error"+e.toString());
			Toast.makeText(mContext, e.getMessage(), 0).show();
		}finally{
			//handler.sendEmptyMessage(2);
			
		}
		//++ 로딩중 끝
			
		
			
		 
		
		setContentView(R.layout.story_list);
		
		//++레이아웃 각 해상도에 따른 셋팅
		
		
		
		ImageView ivHome = (ImageView) findViewById(R.id.home);
		ivHome.setOnClickListener(new View.OnClickListener() {			
								//@Override
							public void onClick(View v) {
								
				        		Intent intent = new Intent(OllybollyList.this, OllybollyMain.class); 
				        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
									
								}
				    		});
		
		mSwitcher = (RelativeLayout) findViewById(R.id.switcher);
		//mSwitcher.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		//mSwitcher.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		//mSwitcher.setBackgroundResource(mImageIds[0]);
		spcIv = (ImageView)  findViewById(R.id.spcmaintitle);
		spcIv2 = (ImageView) findViewById(R.id.spclisttitle);
		comIv = (ImageView)  findViewById(R.id.maintitle);
		comIv2 = (ImageView) findViewById(R.id.listtitle);      
		spcVs = ((ViewStub) findViewById(R.id.spcviewstub_list)).inflate();
		comVs = ((ViewStub) findViewById(R.id.comviewstub_list)).inflate();
		
		//++해상도 별 좌표 셋팅
		if(screenWidth == 1024){
		    MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(spcIv.getLayoutParams());
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
		
		//++선택시 view 셋팅 시작
		if(mCommon.valNation.equals("special")){
		try {
		    
			comIv.setVisibility(View.INVISIBLE);
			comIv2.setVisibility(View.INVISIBLE);
			comVs.setVisibility(View.INVISIBLE);
			
			if(mSelectedIndex == 0){
				spcVs.setVisibility(View.INVISIBLE);
				spcIv2.setVisibility(View.INVISIBLE);
				spcIv.setVisibility(View.VISIBLE);
				
				String mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov.png";
				
				if(Common.langType.equals("en")){
					mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov_en.png";
				}
		    	Bitmap mainTitle = BitmapFactory.decodeFile(mainTitleImage);
				BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
				spcIv.setBackgroundDrawable(drawableMainTitle);	        
				
		
		
				
			}else{
				
				
				spcIv.setVisibility(View.INVISIBLE);
				spcIv2.setVisibility(View.VISIBLE);
				spcVs.setVisibility(View.VISIBLE);
					
		    	Bitmap mainTitle = BitmapFactory.decodeFile(mCommon.cmItemTitleImageList.get(0));
				BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
				spcIv2.setBackgroundDrawable(drawableMainTitle);	
				
		     	//mNation = (ImageView) findViewById(R.id.nation);	        		
			}
		
			
		
		 	
			
		} catch (Exception e) {
		
			// TODO: handle exception
			e.printStackTrace();
		}
		
		spcIv.setOnClickListener(new View.OnClickListener() {			
			//@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class);
				intent.putExtra(Common.VAL_URL, "http://apis.ollybolly.org/video/special.mp4");  // 클릭했을때
				startActivity(intent);  // 위의 intent작업들을 시작!!					
					
				}
				
			});
		}else{       	
			
			spcIv.setVisibility(View.INVISIBLE);
			spcIv2.setVisibility(View.INVISIBLE);    
			spcVs.setVisibility(View.INVISIBLE);
			
		    try {
		        
		    	if(mSelectedIndex == 0){
		    		comVs.setVisibility(View.INVISIBLE);
		    		comIv2.setVisibility(View.INVISIBLE);
		    		comIv.setVisibility(View.VISIBLE);
		    		Bitmap mainTitle = null;
		    		if(Common.langType.equals("en")){
					mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_title_en.png");	
				}else{
					mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_title.png");
				}
		    	
				BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
				
				comIv.setBackgroundDrawable(drawableMainTitle);	        
				
		
		
				
			}else{
				
				
				comIv.setVisibility(View.INVISIBLE);
				comIv2.setVisibility(View.VISIBLE);
				comVs.setVisibility(View.VISIBLE);
				
		    	Bitmap mainTitle = BitmapFactory.decodeFile(mCommon.cmItemTitleImageList.get(0));
				BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
				comIv2.setBackgroundDrawable(drawableMainTitle);	
				
		     	//mNation = (ImageView) findViewById(R.id.nation);	        		
			}
		
			
		
		 	
			
		} catch (Exception e) {
		
			// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		//++선택시 view 셋팅 끝
		
		try {
			
			Bitmap itemImage = BitmapFactory.decodeFile(mCommon.cmItemImageList.get(0));
			BitmapDrawable d =new BitmapDrawable(itemImage);
			mSwitcher.setBackgroundDrawable(d);
		
		} catch (Exception e) {
		
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		listview = (ListView) findViewById(R.id.listview);
		//++라인 없애기
		listview.setDivider(null);
		
		
		mImageAdapter = new ImageAdapter(this);
		listview.setAdapter(mImageAdapter);
		//int test = listview.getScrollY();
		//int test = listview.getVerticalScrollbarWidth(); 
		//Log.e("test",test+"");
		//listview.setScroll
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
		    public void onItemClick( 
		        AdapterView<?> parent, View view, int position, long id) {             	
		    	mSelectedIndex = position;
		    	mParent = parent;
		    	mView = view;
		    	mId= id;
		    	
		    	Log.e("id",id+"==");
			//Log.e("onItemClick","position="+position);
			//Log.e("parent.getCount()","parent.getCount()="+parent.getCount());
			LinearLayout linear = (LinearLayout)view.findViewById(R.id.story_main);
			linear.setBackgroundResource(R.drawable.bg_yellow);
			
		    //Toast.makeText(Scratch.this, "Clicked _id="+id, Toast.LENGTH_SHORT).show(); 
			//mSwitcher.setBackgroundResource(mImageIds[position]);
			 
			//++해당 guid가 보관함에 저장되어 있는지 체크
			boolean isGuid = mDBManager.getGuidMybox(mCommon.cmItemGuid.get(mSelectedIndex)); 
			
		    //++선택시 view 셋팅 시작
		    if(mCommon.valNation.equals("special")){ //++스페셜일때
		        try {
		        	comIv.setVisibility(View.INVISIBLE);
		        	comIv2.setVisibility(View.INVISIBLE);    
		        	comVs.setVisibility(View.INVISIBLE);
		        	if(mSelectedIndex == 0){
		        		spcVs.setVisibility(View.INVISIBLE);
		        		spcIv2.setVisibility(View.INVISIBLE);
		        		spcIv.setVisibility(View.VISIBLE);
		        		String mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov.png";
		        		
		        		if(Common.langType.equals("en")){
		        			mainTitleImage = "data/data/com.ollybolly/files/spc_bt_mov_en.png";
		        		}
			        	Bitmap mainTitle = BitmapFactory.decodeFile(mainTitleImage);
			    		BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
			    		spcIv.setBackgroundDrawable(drawableMainTitle);	        
			    		
		
		
			    		
		        	}else{
		        		
		        		
		        		spcIv.setVisibility(View.INVISIBLE);
		        		spcIv2.setVisibility(View.VISIBLE);
			    		spcVs.setVisibility(View.VISIBLE);
			    		
			    		vsImage01 = (ImageButton) findViewById(R.id.Button_01);
			    		vsImage02 = (ImageButton) findViewById(R.id.Button_02);
			    		vsImage03 = (ImageButton) findViewById(R.id.Button_03);
			    		vsImage04 = (ImageButton) findViewById(R.id.Button_04);
			    		
			            if(Common.langType.equals("en")){
			            	//vsImage01.setBackgroundResource(R.drawable.storyview_bt1_en);
			            	//vsImage02.setBackgroundResource(R.drawable.storyview_bt2_en);
			            	vsImage03.setBackgroundResource(R.drawable.bt_view_description_en);
			            	vsImage04.setBackgroundResource(R.drawable.bt_save_en);
			            }	  
			            
			            //++각 이미지 버튼 셋팅하기
			            
			            String mItemUrl = mCommon.cmItemUrlList.get(position);
			            
			            
			            storyBtn = mCommon.getStoryBtin(Common.langType, mItemUrl, mCommon.cmItemCountList.get(position));
			            
		            	vsImage01.setBackgroundResource(storyBtn[0]);
		            	
		            	
		            	if(storyBtn[1] != 0){
		            		vsImage02.setBackgroundResource(storyBtn[1]);
		            	}
		            	

			            
			            if(isGuid){
		   					Drawable alpha = vsImage04.getBackground();
		   					alpha.setAlpha(100);
		   					
			            	vsImage04.setEnabled(false);
			            }else{
		   					Drawable alpha = vsImage04.getBackground();
		   					alpha.setAlpha(255);        		            	
			            	vsImage04.setEnabled(true);
			            }
			            
			    		vsImage01.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								String movieUrl = mCommon.getMovieUrl(mCommon.cmItemUrlList.get(mSelectedIndex), "1");
								//Log.e("movieUrl",movieUrl);
								
				        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
			 	        		intent.putExtra(Common.VAL_URL, movieUrl);  // 클릭했을때 해당 국가 파라미터값 전달
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
			 	        		
			 	        		
								
							}
			    		});
			    		
			    		vsImage02.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								String movieUrl = mCommon.getMovieUrl(mCommon.cmItemUrlList.get(mSelectedIndex), "2");
				        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
			 	        		intent.putExtra(Common.VAL_URL, movieUrl);  // 클릭했을때 해당 국가 파라미터값 전달
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
								
							}
			    		});        
			    		
			    		vsImage03.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								okDialog(0);
								
							}
			    		});   
			    		
							
			    		vsImage04.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								okDialog(1);
								
							}
			    		});  
			    		
			    		
			    		if(mCommon.cmItemCountList.get(position).equals("1")){
			    			vsImage02.setVisibility(View.GONE);
			    		}else{
			    			vsImage02.setVisibility(View.VISIBLE);
			    		}
			    		
				    		
			    		
			    		
			        	Bitmap mainTitle = BitmapFactory.decodeFile(mCommon.cmItemTitleImageList.get(position));
			    		BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
			    		spcIv2.setBackgroundDrawable(drawableMainTitle);	
			    		
			         	//mNation = (ImageView) findViewById(R.id.nation);	        		
		        	}
		
		    		
		
		         	
		    		
				} catch (Exception e) {
		
					// TODO: handle exception
					e.printStackTrace();
				}
				
				spcIv.setOnClickListener(new View.OnClickListener() {			
					//@Override
					public void onClick(View v) {
						
		        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
		        		intent.putExtra(Common.VAL_URL, "http://apis.ollybolly.org/video/special.mp4");  // 클릭했을때
		        		startActivity(intent);  // 위의 intent작업들을 시작!!					
						
					}
					
				});
		    }else{       	
		    	
		    	spcIv.setVisibility(View.INVISIBLE);
		    	spcIv2.setVisibility(View.INVISIBLE);     
		    	spcVs.setVisibility(View.INVISIBLE);
		    	
		        try {
		            
		        	if(mSelectedIndex == 0){
		        		comVs.setVisibility(View.INVISIBLE);
		        		comIv2.setVisibility(View.INVISIBLE);
		        		comIv.setVisibility(View.VISIBLE);
		        		
		
		        		Bitmap mainTitle = null;
		        		if(Common.langType.equals("en")){
		        			mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_title_en.png");	
		        		}else{
		        			mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_title.png");
		        		}        		
			    		BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
			    		
			    		comIv.setBackgroundDrawable(drawableMainTitle);	        
			    		
		
		
			    		
		        	}else{
		        		
		        		
		        		comIv.setVisibility(View.INVISIBLE);
		        		comIv2.setVisibility(View.VISIBLE);
			    		comVs.setVisibility(View.VISIBLE);
			    		
			    		vsImage01 = (ImageButton) findViewById(R.id.ComButton_01);
			    		vsImage02 = (ImageButton) findViewById(R.id.ComButton_02);
			    		vsImage03 = (ImageButton) findViewById(R.id.ComButton_03);
			    		vsImage04 = (ImageButton) findViewById(R.id.ComButton_04);
			    		vsImage05 = (ImageButton) findViewById(R.id.ComButton_05);
			    		vsImage06 = (ImageButton) findViewById(R.id.ComButton_06);       
			    		
			    		
			    		
			    		if(mCommon.cmItemImageList.get(mSelectedIndex).indexOf("1") > -1){
			    			if(Common.langType.equals("en")){
			    				vsImage06.setBackgroundResource(R.drawable.bt_goto_continue_en);
			    			}else{
			    				vsImage06.setBackgroundResource(R.drawable.bt_goto_continue);
			    			}
			    				
			    			vsImage06.setVisibility(View.VISIBLE);
			    			
			    			vsImage06.setOnClickListener(new View.OnClickListener() {			
								//@Override
								public void onClick(View v) {
									mSelectedIndex = mSelectedIndex+1;
									//listview.setSelection(mSelectedIndex);
									//mImageAdapter.notifyDataSetChanged();
		
									onItemClick( mParent, mView,mSelectedIndex, mId);
									
									
									
								}
				    		});        		    			
			    		}else if(mCommon.cmItemImageList.get(mSelectedIndex).indexOf("2") > -1){
			    			if(Common.langType.equals("en")){
			    				vsImage06.setBackgroundResource(R.drawable.bt_goto_before_en);
			    			}else{
			    				vsImage06.setBackgroundResource(R.drawable.bt_goto_before);
			    			}        		    			
			    			vsImage06.setVisibility(View.VISIBLE);
			    			
			    			vsImage06.setOnClickListener(new View.OnClickListener() {			
								//@Override
								public void onClick(View v) {
									
									mSelectedIndex = mSelectedIndex -1;
									//listview.setSelection(mSelectedIndex);
									//mImageAdapter.notifyDataSetChanged();
		    							
									onItemClick( mParent, mView,mSelectedIndex, mId);
				 	        		
									
								}
				    		});        		    			
			    		}else{
			    			vsImage06.setVisibility(View.GONE);
			    		}
			    		
			            if(Common.langType.equals("en")){
			            	vsImage01.setBackgroundResource(R.drawable.bt_korean_en);
			            	vsImage02.setBackgroundResource(R.drawable.bt_english_en);
			            	vsImage04.setBackgroundResource(R.drawable.bt_view_description_en);
			            	vsImage05.setBackgroundResource(R.drawable.bt_save_en);
			            	//vsImage06.setBackgroundResource(R.drawable.bt_goto_continue_en);
			            }     
			            
			            if(isGuid){
		   					Drawable alpha = vsImage05.getBackground();
		   					alpha.setAlpha(100);
			            	vsImage05.setEnabled(false);
			            }else{
		   					Drawable alpha = vsImage05.getBackground();
		   					alpha.setAlpha(255);        		            	
			            	vsImage05.setEnabled(true);
			            }
			            
			    		Bitmap mainTitle = null;
			    		
			    		if(Common.langType.equals("en")){
			    			mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_bt_en.png");
			    		}else{
			    			mainTitle = BitmapFactory.decodeFile("data/data/com.ollybolly/files/"+mDBManager.getCountryImage(" where " +  DB.COLUMN_LANGLUAGE_EN + "='" + mCommon.valNation + "' ")+"_bt.png");
			    		}
			    		BitmapDrawable drawableMainTitle =new BitmapDrawable(mainTitle);
			    		
			    		vsImage03.setBackgroundDrawable(drawableMainTitle);	
			    		
			    		
			    		vsImage01.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								String movieUrl = mCommon.getMovieUrl(mCommon.cmItemUrlList.get(mSelectedIndex), "1");
								//Log.e("movieUrl",movieUrl);
								
				        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
			 	        		intent.putExtra(Common.VAL_URL, movieUrl);  // 클릭했을때 해당 국가 파라미터값 전달
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
			 	        		
			 	        		
								
							}
			    		});
			    		
			    		vsImage02.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								String movieUrl = mCommon.getMovieUrl(mCommon.cmItemUrlList.get(mSelectedIndex), "3");
								//Log.e("movieUrl",movieUrl);
								
				        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
			 	        		intent.putExtra(Common.VAL_URL, movieUrl);  // 클릭했을때 해당 국가 파라미터값 전달
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
							}
			    		});
			    		        		    		
			    		vsImage03.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								String movieUrl = mCommon.getMovieUrl(mCommon.cmItemUrlList.get(mSelectedIndex), "2");
								//Log.e("movieUrl",movieUrl);
								
				        		Intent intent = new Intent(OllybollyList.this, OllybollyStoryPlay.class); 
			 	        		intent.putExtra(Common.VAL_URL, movieUrl);  // 클릭했을때 해당 국가 파라미터값 전달
			 	        		startActivity(intent);  // 위의 intent작업들을 시작!!
			 	        		
			 	        		
								
							}
			    		});
			    		        		
			    		
			    		vsImage04.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								
								okDialog(0);
								
							}
			    		});   
			    		
							
			    		vsImage05.setOnClickListener(new View.OnClickListener() {			
							//@Override
							public void onClick(View v) {
								okDialog(1);
								
							}
			    		});  
			    		
			    		
			    		mainTitle = BitmapFactory.decodeFile(mCommon.cmItemTitleImageList.get(position));
			    		drawableMainTitle =new BitmapDrawable(mainTitle);
			    		comIv2.setBackgroundDrawable(drawableMainTitle);	
			    		
			         	//mNation = (ImageView) findViewById(R.id.nation);	        		
		        	}
		
				} catch (Exception e) {
		
					// TODO: handle exception
					e.printStackTrace();
				}
				
		    }
		    //++선택시 view 셋팅 끝
		    
		    try {
		
				Bitmap itemImage = BitmapFactory.decodeFile(mCommon.cmItemImageList.get(position));
				BitmapDrawable d =new BitmapDrawable(itemImage);
				mSwitcher.setBackgroundDrawable(d);
		
			} catch (Exception e) {
		
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			//++리스트 갱신
		        	mImageAdapter.notifyDataSetChanged();
		        	listview.setSelection(mSelectedIndex);
		        }
		
		    }); 
		            
		}
		
		public View makeView() {
		    ImageView i = new ImageView(this);
		    i.setBackgroundColor(0xFF000000);
		   
		    i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,
		    		LayoutParams.FILL_PARENT));
		    i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    
		    //Log.e("makeView","makeView");
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
		    	
		    	//Log.e("getView","getView"+position);
		
		
		// LayoutInflater의 객체 inflater를 현재 context와 연결된 inflater로 초기화.
		LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
		
		// inflator객체를 이용하여 \res\laout\row.xml 파싱
		View row = (View)inflater.inflate(R.layout.row, null);
		
		
		//++LinearLayout 배경 변경 하기
		
		if(position == mSelectedIndex){           
			LinearLayout linear = (LinearLayout)row.findViewById(R.id.story_main);
			linear.setBackgroundResource(R.drawable.bg_yellow);
		}else{
			//Log.e("here","here");
			LinearLayout linear = (LinearLayout)row.findViewById(R.id.story_main);
			linear.setBackgroundResource(R.drawable.bg_white);
		}
		
		
		//아이디를 담은 공간 생성
		ImageView storyList = (ImageView)row.findViewById(R.id.story_list);
		//storyList.setBackgroundResource(mImageThumbList[position]);
		
		try {
		
			Bitmap itemImage = BitmapFactory.decodeFile(mCommon.cmItemThumbList.get(position));
			BitmapDrawable d =new BitmapDrawable(itemImage);
			
		   	 storyList.setScaleType(ImageView.ScaleType.FIT_XY);
			 storyList.setBackgroundDrawable(d);
		
		} catch (Exception e) {
		
			// TODO: handle exception
			e.printStackTrace();
		}finally{
		}
		
		
		// 커스터마이징 된 View 리턴.
		    return row;
			
		    
		}

        

    }
    
    
    //++담아두기 다이얼로그 시작
    private void okDialog(int dialogId){  
    	Dialog dialog = null;
    	switch (dialogId) {
    		case 1:
    			
    			 isSaveOk = false;
    			 isSaveOk = mDBManager.insertMybox(mCommon.cmItemGuid.get(mSelectedIndex));
    			 
    			 String strMessage = "";
    			 String strMessageAlert = "";
    			 String strBtn = "";
    			 if(isSaveOk){
    				 if(Common.langType.equals("en")){
    					 strMessage = "Add Favorite";
    					 strMessageAlert = "The story adds successfully";
    					 strBtn = "close";
    				 }else{
    					 strMessage = "즐겨찾기";
    					 strMessageAlert = "동화가 추가되었습니다.";
    					 strBtn = "닫기";
    				 }
    			 }else{
    				 strMessage = "";
    			 }
    			 
    			 
    			 
    		     AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);  
    		     alt_bld.setTitle( strMessage);          		
    		     alt_bld.setMessage(strMessageAlert );
    		     alt_bld.setPositiveButton(strBtn, new DialogInterface.OnClickListener() {
    	    		public void onClick( DialogInterface dialog, int which) {
    	           			dialog.dismiss();   //닫기
    	           					
    	           				if(mCommon.valNation.equals("special")){ //++스페셜일때
    	           					Drawable alpha = vsImage04.getBackground();
    	           					alpha.setAlpha(100);
	    	        				vsImage04.setEnabled(false);
	    	        			}else{
    	           					Drawable alpha = vsImage05.getBackground();
    	           					alpha.setAlpha(100);
	    	        				vsImage05.setEnabled(false);
	    	        			}
    	           		
    	        			
    	    		}
    	      		});

    		     AlertDialog alert = alt_bld.create();

    		     // Icon for AlertDialog  
    		     //alert.setIcon(R.drawable.title_download);  

    		     alert.show();      			
    			break;
    		case 0:
    			
    			String strWriter = "글쓴이 | ";
    			String strIllustrator = "그린이 |";
    			
    			if(Common.langType.equals("en")){
    				strWriter = "writer | ";
    				strIllustrator = "illustraotr | ";
    			}
    			
				OllybollyDialog.Builder customBuilder = new
				OllybollyDialog.Builder(OllybollyList.this);
				customBuilder.setTitle(mCommon.cmItemTitle.get(mSelectedIndex))
					.setMessage(mCommon.cmItemDescription.get(mSelectedIndex))
					.setWriter(strWriter + mCommon.cmItemWriter.get(mSelectedIndex))
					.setIllustrator(strIllustrator + mCommon.cmItemIllustrator.get(mSelectedIndex))
				
					//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					//	public void onClick(DialogInterface dialog, int which) {
					//		OllybollyList.this
					//		.dismissDialog(0);
					//	}
					//})
					//.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					//	public void onClick(DialogInterface dialog, int which) {
					//		dialog.dismiss();
					//	}
					//})
					;
	            dialog = customBuilder.create();
	            dialog.show();
    			break;
    		default :
    			break;    			
    	}


	 } 
    
    // 레이아웃 설정
    LayoutParams calLayout(LayoutParams params){
    	params.width = (int)Math.ceil(params.width*RW);
    	params.height = (int)Math.ceil(params.height*RH);
    	
    	params.setMargins((int)Math.ceil(params.leftMargin*RW),
    			(int)Math.ceil(params.topMargin*RH),
        		(int)Math.ceil((params.leftMargin+params.width)*RW),
        		(int)Math.ceil((params.topMargin+params.height)*RH));
      
    	return params;
    }
   	
    //++담아두기 다이얼로그 끝  
	//---------------------------------------------------뒤로가기 버튼 오버라이드 구현 시작--------------------------------------------    
    public void onBackPressed(){
    	clickBack();
    	return;
    }
    
    private void clickBack(){  

			Intent intent = new Intent(OllybollyList.this, OllybollyMain.class); //이것은 현재페이지에서 현재페이지 클래스로.. 
   			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
   			startActivity(intent);  // 위의 intent작업들을 시작!!    	     

    	 } 
  //---------------------------------------------------뒤로가기 버튼 오버라이드 구현 끝-------------------------------------------- 
}
