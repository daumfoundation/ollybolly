package com.ollybolly;

/**
 * 
 * page info : ��Ʈ�� ������
 * xml ���� �Ľ��Ͽ� ������ ��� �־���
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
	
	//��� �Ŵ���
	DB mDBManager;
	Common mCommon;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	
	   
	    setContentView(R.layout.ollybolly);
	
	    //++SharedPreferences �� �������� ����
	    Common.langType  = PreferenceUtil.getPreference(this, Common.KEY_LANGUAGE);
	    //++SharedPreferences �� �������� ��
		
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
					 strMessage = "���ͳݿ���";
					 strMessageAlert = "���ͳݿ����� Ȯ���ϼ���." ;
					 strBtnOk = "�ݱ�";				
				}

			 
		   	 	AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
					alert_internet_status.setTitle( strMessage );          		
					alert_internet_status.setMessage( strMessageAlert );
				alert_internet_status.setPositiveButton( strBtnOk, new DialogInterface.OnClickListener() {
		 		public void onClick( DialogInterface dialog, int which) {
		    	   			         dialog.dismiss();   //�ݱ�
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
			
			//++�ǽð� ��Ʈ��ũ ����
			PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "0");
			
			pDialog = ProgressDialog.show(Ollybolly.this, "", "loading ",true,false);

			  			
			Thread thread = new Thread(new Runnable(){
				public void run(){
					

					//++������
			        mDBManager =DB.getInstance(mContext);
			        
			        //mDBManager.dropTable();
			                
			        int tableVersionCount = mDBManager.getTableCount(DB.T_NAME_VERSION);
			        int tableCountryCount = mDBManager.getTableCount(DB.T_NAME_COUNTRY);
			        	
			        
					//++xml �Ľ��ϱ�
			        XmlParsing xp = new XmlParsing(mContext);

					
					mCommon = new Common();
					
					//++ �ε��� ����	
					try{
						
						String[] xmlResult = xp.getXmlVersionResult();
		    			//++���� ����Ʈ �ҷ�����
			    		mCommon.xmlVersionList = mDBManager.getVersionList();
			    		
			    		if(tableVersionCount == 0){ //++���� ���̺� �ʱ� ������			    			
			    			mDBManager.insertVersion(xmlResult);
			    			//Log.e("version","first");
			    		}else{ //++���� ���� ���̺��� �ִٸ� ��¥ üũ �ؼ� �ֽ� ���� ������ �ٽ� ������ �ֱ�
			    			//Log.e("xmlResult[0]",xmlResult[0]+"=="+mCommon.xmlVersionList.get(0));
			    			//Log.e("xmlResult[2]",xmlResult[2]+"=="+mCommon.xmlVersionList.get(2));
				    		if(xmlResult[0].equals(mCommon.xmlVersionList.get(0)) && xmlResult[2].equals(mCommon.xmlVersionList.get(2))){//++pass
				    			//Log.e("version","pass");
				    			
				    			
				    		
				    		}else{//++�ֽ� ���� �ִٸ� �̹��� �ٽ� �����ϰ� ���� ī��Ʈ 0���� ����
				    			//Log.e("version","second");
				    			mDBManager.deleteTable(DB.T_NAME_VERSION);
				    			mDBManager.insertVersion(xmlResult);
				    			tableVersionCount = 0;
				    		}
			    		}
			    		
			    		//++���� ����Ʈ �ҷ�����
			    		mCommon.xmlVersionList = mDBManager.getVersionList();
			    		
			        	//++�� ���� ����Ʈ �ҷ�����
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
				        if( tableVersionCount == 0){ //++���� xml insert �ϱ� �̹����� ���ÿ� �� �����ϱ�		        	
				        	//++ ������ �����
				        	mDBManager.deleteTable(DB.T_NAME_COUNTRY);
				        	
				        	//++��� �־��ֱ�				            
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
				            
				            //++�̹��� ���ÿ� �ٿ�ε� �ϱ� ����
				            
				            //++Ŀ�� ���� ����
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
				            //++�̹��� ���ÿ� �ٿ�ε� �ϱ� ��
				            
				            crsCountry.close();
				            
				            //++���� ������ �̹��� �� ���� �̹��� �ٿ�ε� �ϱ�
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
				        }else{//++�̹� ������ �ִ� ���
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
					//++ �ε��� ��
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
		        	
		        	
		        	
					//++�������� �̵�
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



