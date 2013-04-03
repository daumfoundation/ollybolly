package com.ollybolly;

/**
 * 
 * page info : 언어선택 페이지
 * 한국어/영어 선택 페이지
 * @author sangsangdigital
 *
 */

import com.ollybolly.util.PreferenceUtil;

import android.app.Activity; 
import android.net.Uri; 
import android.os.Bundle; 
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView; 
import android.content.Intent;
import android.graphics.Color;

public class OllybollySetting extends Activity {

  ImageView imgView01 = null;
  ImageView imgView02 = null;
  ImageView imgView03 = null;
  ImageView imgView04 = null;
  ImageView imgView05 = null;
  ImageView imgView06 = null;
  ImageView imgView07 = null;
  ImageView imgView08 = null;
  ImageView imgView09 = null;
  ImageView imgView10 = null;
  ImageView imgView11 = null;
  
  TextView textView01 = null;
  TextView textView02 = null;
  TextView textView03 = null;
  
  String[] strList = new String[3];  
  
  String[] strUrl = new String[3];
  
     @Override
  public void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);

   setContentView(R.layout.setting);
   
   //++SharedPreferences 값 가져오기 시작
   Common.langType  = PreferenceUtil.getPreference(this, Common.KEY_LANGUAGE);

   if(Common.langType == null){
   	Common.langType = "ko";
   }
  //++SharedPreferences 값 가져오기 끝
	
	//++실시간 네트워크 셋팅
	PreferenceUtil.putPreference(this, Common.KEY_MOVIE, "0");
	   
   imgView01 = (ImageView) findViewById(R.id.st_title_setting);
   
   imgView02 = (ImageView) findViewById(R.id.st_lang_sel);
   imgView03 = (ImageView) findViewById(R.id.st_korean);
   imgView04 = (ImageView) findViewById(R.id.st_english);
   
   imgView05 = (ImageView) findViewById(R.id.st_ollybolly_text);
   imgView06 = (ImageView) findViewById(R.id.st_goto_ollybolly);
   
   imgView07 = (ImageView) findViewById(R.id.st_daum_text);
   imgView08 = (ImageView) findViewById(R.id.st_goto_daum);
   
   imgView09 = (ImageView) findViewById(R.id.st_daumkids_text);
   imgView10 = (ImageView) findViewById(R.id.st_goto_daumkids);
   
   imgView11 = (ImageView) findViewById(R.id.st_back);   
      
   textView01 = (TextView) findViewById(R.id.st_ollybolly_info);
   textView01.setTextColor(Color.BLACK);
   textView02 = (TextView) findViewById(R.id.st_daum_info);
   textView02.setTextColor(Color.BLACK);
   textView03 = (TextView) findViewById(R.id.st_daumkids_info);
   textView03.setTextColor(Color.BLACK);
   
   strUrl[0] = "http://www.ollybolly.org";
   strUrl[1] = "http://www.daumfoundation.org";
   strUrl[2] = "http://kids.daum.net";


		   
   if(Common.langType.equals("en")){
	   
	   strUrl[0] = "http://ollybolly.org/?mid=home&language=en";
	   strUrl[1] = "http://daumfoundation.org/daumfn/mission_en";
	   strUrl[2] = "http://kids.daum.net";
	   
	   strList[0] = "The Ollybolly Online Picture Book gives kids a chance to read books from different cultures that my not be farmiliar to them! Ollybolly helps us think about our differences as a source of creativity, not a source of discrimination or exclusion.For the more Ollybolly stories, please visit Ollybolly website (http://www.ollybolly.org/english ) or Daum kids zzang website (http://kids,daum.net).";
	   strList[1] = "Established in September 2001, the Daum Foundation is a non-profit organization that benefits from he help of Daum Communications Corp shareholders, executives and emplyees. Daum Foundation aims to provide resources for the next generation of individuals to promote creative and diverse ways of life by utilizing media and communication tools. To realize this mission, the Foundation carries out a whole range of programs including YouthVoice, ITcanus and Ollybolly.";
	   strList[2] = "Daum Communications (http://www.daum.net) is dedicated to creating new value through the internet and bringing joyful changes to the world. We provide convenient services to make online lives more joyful through our email, online cafes, and media that connect individuals and communities, 24 hours a day, 365 days a year. Help us spread hope through Hope Donations (http://hope.daum.net) View OllyBolly stories at Daum KidsZzang (http://kids.daum.net)";
	  
	   imgView01.setBackgroundResource(R.drawable.title_setting_en);
	   imgView02.setBackgroundResource(R.drawable.title_language_select_en);
	   imgView03.setBackgroundResource(R.drawable.icon_setting_korean_off);
	   imgView04.setBackgroundResource(R.drawable.icon_setting_english_on);
	   imgView05.setBackgroundResource(R.drawable.logo_ollybolly_text_en);
	   imgView06.setBackgroundResource(R.drawable.bt_goto_ollybolly_en);
	   imgView07.setBackgroundResource(R.drawable.logo_daumfoundation_text_en);
	   imgView08.setBackgroundResource(R.drawable.bt_goto_daumfoundation_en);
	   imgView09.setBackgroundResource(R.drawable.icon_daum_text);
	   imgView10.setBackgroundResource(R.drawable.bt_goto_daum_en);
	   
	   textView01.setText(strList[0]);
	   textView02.setText(strList[1]);
	   textView03.setText(strList[2]);
	   
	   
   }else{
	   imgView03.setBackgroundResource(R.drawable.icon_setting_korean_on);
	   imgView04.setBackgroundResource(R.drawable.icon_setting_english_off);
   }
      
   imgView03.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {
			
			//++ SharedPreference 셋팅하기 시작
			PreferenceUtil.putPreference(OllybollySetting.this, Common.KEY_LANGUAGE, "ko");
			//++ SharedPreference 셋팅하기 끝
			   
			Intent intent = new Intent(OllybollySetting.this, OllybollySetting.class); 
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    		startActivity(intent);  // 위의 intent작업들을 시작!!
		}
	});
   
   
   imgView04.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {
			
			//++ SharedPreference 셋팅하기 시작
			PreferenceUtil.putPreference(OllybollySetting.this, Common.KEY_LANGUAGE, "en");
			//++ SharedPreference 셋팅하기 끝
			   
			Intent intent = new Intent(OllybollySetting.this, OllybollySetting.class); 
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
   		startActivity(intent);  // 위의 intent작업들을 시작!!
		}
	});   
   
   imgView06.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {

			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl[0]))); 
		}
	}); 
   
   imgView08.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl[1]))); 
		}
	});  
   
   imgView10.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl[2]))); 
		}
	}); 
   
   imgView11.setOnClickListener(new View.OnClickListener() {			
		//@Override
		public void onClick(View v) {
			clickBack();
		}
	});    
   
   
  }
     
 	//---------------------------------------------------뒤로가기 버튼 오버라이드 구현 시작--------------------------------------------    
     public void onBackPressed(){
     	clickBack();
     	return;
     }
     
     private void clickBack(){  

 			Intent intent = new Intent(OllybollySetting.this, OllybollyMain.class); //이것은 현재페이지에서 현재페이지 클래스로.. 
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    			startActivity(intent);  // 위의 intent작업들을 시작!!    	     

     	 } 
   //---------------------------------------------------뒤로가기 버튼 오버라이드 구현 끝--------------------------------------------      
}



