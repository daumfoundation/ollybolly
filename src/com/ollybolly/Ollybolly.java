package com.ollybolly;

/**
 * 
 * page info : 인트로 페이지
 * xml 정보 파싱하여 데이터 디비에 넣어줌
 * @author sangsangdigital
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ollybolly.util.PreferenceUtil;
import com.ollybolly.xml.XmlParsing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle; 
import android.os.Message;
import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo; 
import android.os.Handler;
import android.util.Log;

import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Intent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;

import com.ollybolly.db.DB;


public class Ollybolly extends Activity {

	 private String strMessage = "";
	 private String strMessageAlert = "";
	 private String strBtnOk = "";
	
	private Context mContext;
	
	private ProgressDialog pDialog;
	
	//디비 매니저
	DB mDBManager;
	Common mCommon;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	
	   
	    setContentView(R.layout.ollybolly);
	
	    //++SharedPreferences 값 가져오기 시작
	    Common.langType  = PreferenceUtil.getPreference(this, Common.KEY_LANGUAGE);
	    //++SharedPreferences 값 가져오기 끝
		
	   if(Common.langType.equals("en")){
		   LinearLayout linear = (LinearLayout) findViewById(R.id.LinearLayout01);
		   linear.setBackgroundResource(R.drawable.default_en);
	   }
	   
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = ni.isConnected();
		
		if(isWifiConn==false && isMobileConn==false)
		{
				if(Common.langType.equals("en")){
					 strMessage = "Internet Connection";
					 strMessageAlert = "Please check your Internet connection.";
					 strBtnOk = "close";			
				}else{
					 strMessage = "인터넷연결";
					 strMessageAlert = "인터넷연결을 확인하세요." ;
					 strBtnOk = "닫기";				
				}

			 
		   	 	AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
					alert_internet_status.setTitle( strMessage );          		
					alert_internet_status.setMessage( strMessageAlert );
				alert_internet_status.setPositiveButton( strBtnOk, new DialogInterface.OnClickListener() {
		 		public void onClick( DialogInterface dialog, int which) {
		    	   			         dialog.dismiss();   //닫기
		    	   			         finish();	 
		 		}
		 	 });
			 alert_internet_status.show();		
		}else{
			mContext = this;
			createThreadAndDialog();		
		}
	  }

		void createThreadAndDialog()
		{

			if(Common.langType.equals("")){
				
				PreferenceUtil.putPreference(this, Common.KEY_LANGUAGE, "ko");
			}
			
			//++실시간 네트워크 셋팅
			PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "0");
			
			pDialog = ProgressDialog.show(Ollybolly.this, "", "loading ",true,false);

			  			
			Thread thread = new Thread(new Runnable(){
				public void run(){
					

					//++디비생성
			        mDBManager =DB.getInstance(mContext);
			        
			        //mDBManager.dropTable();
			                
			        int tableVersionCount = mDBManager.getTableCount(DB.T_NAME_VERSION);
			        int tableCountryCount = mDBManager.getTableCount(DB.T_NAME_COUNTRY);
			        	
			        
					//++xml 파싱하기
			        XmlParsing xp = new XmlParsing(mContext);

					
					mCommon = new Common();
					
					//++ 로딩중 시작	
					try{
						
						String[] xmlResult = xp.getXmlVersionResult();
		    			//++버전 리스트 불러오기
			    		mCommon.xmlVersionList = mDBManager.getVersionList();
			    		
			    		if(tableVersionCount == 0){ //++버전 테이블 초기 생성시			    			
			    			mDBManager.insertVersion(xmlResult);
			    			//Log.e("version","first");
			    		}else{ //++기존 버전 테이블이 있다면 날짜 체크 해서 최신 버전 있으면 다시 데이터 넣기
			    			//Log.e("xmlResult[0]",xmlResult[0]+"=="+mCommon.xmlVersionList.get(0));
			    			//Log.e("xmlResult[2]",xmlResult[2]+"=="+mCommon.xmlVersionList.get(2));
				    		if(xmlResult[0].equals(mCommon.xmlVersionList.get(0)) && xmlResult[2].equals(mCommon.xmlVersionList.get(2))){//++pass
				    			//Log.e("version","pass");
				    			
				    			
				    		
				    		}else{//++최신 버전 있다면 이미지 다시 저장하게 버전 카운트 0으로 셋팅
				    			//Log.e("version","second");
				    			mDBManager.deleteTable(DB.T_NAME_VERSION);
				    			mDBManager.insertVersion(xmlResult);
				    			tableVersionCount = 0;
				    		}
			    		}
			    		
			    		//++버전 리스트 불러오기
			    		mCommon.xmlVersionList = mDBManager.getVersionList();
			    		
			        	//++각 국가 리스트 불러오기
			        	HashMap<Integer, ArrayList<Object>> inCountryList = new HashMap<Integer, ArrayList<Object>>();
			        	inCountryList = xp.getXmlCountryResult();
			        	
			        	if(tableCountryCount > 0){
			        		String titleImage = mDBManager.getCountryImage();
			        		
				        	String strImage= "data/data/com.ollybolly/files/"+titleImage+mCommon.xmlVersionList.get(6);
				        	boolean isImage = mCommon.checkIfFileExists(strImage);
				        	//Log.e("isImage",""+isImage);
				        	
				        	if(isImage){
				        		
				        	}else{
				        		tableCountryCount = 0;
				        		//Log.e("tableCountryCount",tableCountryCount+"");
				        	}
			        	}
			        	
			        	                                                                                         ;		        	                                               
				        if( tableVersionCount == 0){ //++버전 xml insert 하기 이미지도 로컬에 다 저장하기		        	
				        	//++ 데이터 지우기
				        	mDBManager.deleteTable(DB.T_NAME_COUNTRY);
				        	
				        	//++디비에 넣어주기				            
				            Iterator<Integer> iter = inCountryList.keySet().iterator();
				            	            
				            while(iter.hasNext())
				            {
				                int nCount    = iter.next();
				                
				               ArrayList<Object> tmpList = new ArrayList<Object>();
				                
				                tmpList = inCountryList.get(nCount);
				                
				                mDBManager.insertCountry(tmpList);
				                
				                /*
				                for (int i = 0; i < tmpList.size(); i++){
				                	 System.out.println("==nCount=="+nCount+"==i=="+i+"=="+tmpList.get(i));
				                }
				                */
				                
				           	 	//System.out.println ( " value )  "+inList2.get(nCount)  ) ;             
				               
				            }
				            
				            //++이미지 로컬에 다운로드 하기 시작
				            
				            //++커서 변수 선언
				            Cursor crsCountry = mDBManager.getCountryList();
				                    
				            while (crsCountry.moveToNext()) {
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(5)), crsCountry.getString(1)+mCommon.xmlVersionList.get(5),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(6)), crsCountry.getString(1)+mCommon.xmlVersionList.get(6),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(7)), crsCountry.getString(1)+mCommon.xmlVersionList.get(7),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(8)), crsCountry.getString(1)+mCommon.xmlVersionList.get(8),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(9)), crsCountry.getString(1)+mCommon.xmlVersionList.get(9),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(10)), crsCountry.getString(1)+mCommon.xmlVersionList.get(10),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(11)), crsCountry.getString(1)+mCommon.xmlVersionList.get(11),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(12)), crsCountry.getString(1)+mCommon.xmlVersionList.get(12),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(13)), crsCountry.getString(1)+mCommon.xmlVersionList.get(13),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(14)), crsCountry.getString(1)+mCommon.xmlVersionList.get(14),mContext);
				            	mCommon.HttpDown(mCommon.xmlVersionList.get(3)+mCommon.xmlVersionList.get(4)+crsCountry.getString(1)+mCommon.getReplaceFileName(mCommon.xmlVersionList.get(15)), crsCountry.getString(1)+mCommon.xmlVersionList.get(15),mContext);
				    		}
				            //++이미지 로컬에 다운로드 하기 끝
				            
				            crsCountry.close();
				            
				            //++나라별 섬네일 이미지 및 원본 이미지 다운로드 하기
				            HashMap<Integer, ArrayList<Object>> inStoryList = new HashMap<Integer, ArrayList<Object>>();
				            inStoryList = xp.getXmlStoryImageResult();
				            iter = inStoryList.keySet().iterator();
				            
				            
				            mDBManager.deleteTable(DB.T_NAME_ITEM);
				            
				            while(iter.hasNext())
				            {
				                int nCount    = iter.next();
				                
				               ArrayList<Object> tmpList = new ArrayList<Object>();
				                
				               tmpList = inStoryList.get(nCount);
				               				                
				               mDBManager.insertItem(nCount,tmpList);
				                
				               /*
				                for (int i = 0; i < tmpList.size(); i++){
				                	 System.out.println("==nCount=="+nCount+"==i=="+i+"=="+(String)tmpList.get(i));
				                }
				               */
				               
				                
				           	 	//System.out.println ( " value )  "+inList2.get(nCount)  ) ;             
				               				               
				            }
				            
							
				            
				    		//Log.d("xmlResult",xmlResult[14]);	        	
				        }else{//++이미 데이터 있는 경우
				        	mCommon.xmlVersionList = mDBManager.getVersionList();
				        	    	
				        }
						
						handler.sendEmptyMessage(1);  
					}catch(Exception e){
						handler.sendEmptyMessage(3);
						Log.e("error","error"+e.toString());
						Toast.makeText(mContext, e.getMessage(), 0).show();
					}finally{
						handler.sendEmptyMessage(2);
						
					}
					//++ 로딩중 끝
				}
				
				
			});
			thread.start();
			
			
			
		}
		


		
		
		private Handler handler = new Handler() {
		    public void handleMessage(Message msg) {
		        switch(msg.what)
		        {
		        case 0:
		        	pDialog.dismiss();
		        	/*
		        	mTxtResult.setText(R.string.safe_name);
					mTxtResult.setVisibility(View.VISIBLE);
					mBtnGo.setVisibility(View.GONE);
					*/
		        	break;
		        	
		        case 1:
		        	pDialog.dismiss();
		        	break;
		        case 2:
		        	
		        	
		        	
					//++메인으로 이동
					Handler mHandler = new Handler();
			        mHandler.postDelayed(new Runnable() {
			        	// Do Something
			        	
			        	public void run()
			        	{
			        		
			    			Intent intent = new Intent(Ollybolly.this,OllybollyMain.class);
			    			startActivity(intent);
			    			//overridePendingTransition (0,0); 
			    			pDialog.dismiss();
			    			finish();   
			    			     		
			        	}
			        	
			        }, 2000);
			        
			        	
			        
		        	break;
		        case 3:
		        	pDialog.dismiss();
		        	Toast.makeText(Ollybolly.this, "fail", 0).show();
		        	break;
		        	
		        }
		    }
		};


}



