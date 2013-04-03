package com.ollybolly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ollybolly.R;
import com.ollybolly.R.drawable;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Common {
	//++올리볼리 공통 변수 시작

	public ArrayList<String> xmlVersionList = new ArrayList<String>();
	
	public ArrayList<String> xmlCountryEnglish = new ArrayList<String>();
	
	public ArrayList<Integer> cmItemGuid = new ArrayList<Integer>();
	public ArrayList<String> cmItemImageList = new ArrayList<String>();
	public ArrayList<String> cmItemThumbList = new ArrayList<String>();
	public ArrayList<String> cmItemTitleImageList = new ArrayList<String>();
	public ArrayList<String> cmItemUrlList = new ArrayList<String>();
	public ArrayList<String> cmItemCountList = new ArrayList<String>();
	public ArrayList<String> cmItemDescription = new ArrayList<String>();
	public ArrayList<String> cmItemTitle = new ArrayList<String>();
	public ArrayList<String> cmItemWriter = new ArrayList<String>();
	public ArrayList<String> cmItemIllustrator = new ArrayList<String>();
	public ArrayList<String> cmItemNationEn = new ArrayList<String>();
		
	public static String langType = "ko";
	
	public static final String KEY_LANGUAGE = "language";
	public static final String KEY_MOVIE = "movie";
	
	public static SharedPreferences pref = null;
	
    // 클릭했을때 클릭한 나라 파라미터 전달
    public static final String VAL_NATION = "nation";
    public static final String VAL_URL = "url";
    public static final String VAL_DATE = "date";
    
    public String valNation = "";
	

	public static void setLangType(String type){
		langType = type;
	}
	
	public static String getLangType(){
		return langType;
	}
	
	//++파일명 @2x값 붙여서 리턴하기
	public String getReplaceFileName(String fileName){

		String tmpFile = "";
		
		String tmpArr[]=fileName.split("\\.");
		
		if(tmpArr.length == 2){
			tmpFile = tmpArr[0]+"@2x."+ tmpArr[1];
		}else{
			tmpFile = fileName;
		}
		
		return tmpFile;

	}
	
	//++파일명 _b@2x값 붙여서 리턴하기
	public String getReplaceFileOriginName(String fileName, String type){

		String tmpFile = "";
		
		String tmpArr[]=fileName.split("\\/");
		String tmpArr2[]=null;
		
		if(tmpArr.length == 5){
			tmpArr2 = tmpArr[4].split("\\.");
			
			if(type.equals("F")){
				tmpFile = tmpArr2[0]+"_b."+ tmpArr2[1];
			}else{
				tmpFile = tmpArr[0]+"/"+tmpArr[1]+"/"+tmpArr[2]+"/"+tmpArr[3]+"/"+tmpArr2[0]+"_b@2x."+ tmpArr2[1];
			}
			
		}else{
			tmpFile = fileName;
		}
		//Log.e("tmpFile","=="+tmpFile);
		return tmpFile;
	}	
	
	//++파일명 _s@2x값 붙여서 리턴하기
	public String getReplaceFileThumbName(String fileName, String type){

		String tmpFile = "";
		
		String tmpArr[]=fileName.split("\\/");
		String tmpArr2[]=null;
		
		if(tmpArr.length == 5){
			tmpArr2 = tmpArr[4].split("\\.");
			
			if(type.equals("F")){
				tmpFile = tmpArr2[0]+"_s."+ tmpArr2[1];
			}else{
				tmpFile = tmpArr[0]+"/"+tmpArr[1]+"/"+tmpArr[2]+"/"+tmpArr[3]+"/"+tmpArr2[0]+"_s@2x."+ tmpArr2[1];
			}
			
		}else{
			tmpFile = fileName;
		}
		//Log.e("tmpFile","=="+tmpFile);
		return tmpFile;

	}	

	//++파일명 _s@2x값 붙여서 리턴하기
	public String getReplaceFileTitleName(String fileName, String type, String lang){

		String tmpFile = "";
		
		String tmpTail = "";
		
		if(lang.equals("en")){
			tmpTail = "_en";
		}
		
		String tmpArr[]=fileName.split("\\/");
		String tmpArr2[]=null;
		
		
		
		
		if(tmpArr.length == 5){
			tmpArr2 = tmpArr[4].split("\\.");
			
			if(type.equals("F")){
				tmpFile = tmpArr2[0]+"_t"+tmpTail+"."+ tmpArr2[1];
			}else{
				tmpFile = tmpArr[0]+"/"+tmpArr[1]+"/"+tmpArr[2]+"/"+tmpArr[3]+"/"+tmpArr2[0]+"_t"+tmpTail+"@2x."+ tmpArr2[1];
			}
			
		}else{
			tmpFile = fileName;
		}
		//Log.e("tmpFile","=="+tmpFile);
		return tmpFile;

	}
	
	// HTTP URL로부터 이미지 파일을 다운로드받아 로컬에 저장한다. 
	public boolean HttpDown(String Url, String FileName,Context mContext) { 
		//Log.e("Url",Url);
	    URL imageurl; 
	    int Read; 
	    try { 
		    imageurl = new URL(Url); 
		    
		    HttpURLConnection conn= (HttpURLConnection)imageurl.openConnection(); 
		    conn.connect(); 
		    
		    if(conn.getResponseCode() == 404 ){
		    	Log.d("HttpDown","404");
		    }else{
			    int len = conn.getContentLength(); 
			    byte[] raster = new byte[len]; 
			    InputStream is = conn.getInputStream(); 
			    FileOutputStream fos = mContext.openFileOutput(FileName, 0); 
			
			    for (;;) { 
			    Read = is.read(raster); 
			    if (Read <= 0) { 
			    break; 
			    } 
			    fos.write(raster,0, Read); 
			    } 
			
			    is.close(); 
			    fos.close(); 
		    }
	    } catch (Exception e) { 
	    	Log.e("HttpDown",e.toString());
	    return false; 
	    } 
	    return true; 
	}
	
	//++파일 유무 체크
	public boolean checkIfFileExists(String filePath) {
	    if (filePath != null) {
	        File file = new File (filePath);

	        return file.exists();
	    }else{
	    	return false;
	    }
	} 
	
	// 동영상 이미지 명 뽑아오기
	public String getMovieUrl(String Url, String count) { 
		String tmpUrl = "";
		
		String tmpArr[]=Url.split("\\/");
		String tmpArr2[]=null;
		
		
		
		if(tmpArr.length == 5){
			tmpArr2 = tmpArr[4].split("\\.");
			
			if(!count.equals("")){
				tmpUrl = tmpArr[0]+"/"+tmpArr[1]+"/"+tmpArr[2]+"/"+tmpArr[3]+"/"+tmpArr2[0]+"_"+count+"."+ tmpArr2[1];
			}else{
				tmpUrl = tmpArr[0]+"/"+tmpArr[1]+"/"+tmpArr[2]+"/"+tmpArr[3]+"/"+tmpArr2[0]+"_1."+ tmpArr2[1];
			}
			
		}else{
			tmpUrl = Url;
		}
		
 
	    return tmpUrl; 
	}	
	
   /**  두 날짜 사이의 차이
   *
   *  @param startDate 시작 날짜
   *  @param endDate   종료 날짜
   *  @param format    날짜 형식
   *  @return long     날짜 차이
   */
    public long day2Day( String startDate, 
                                String endDate, 
                                String format    ) throws Exception{
        if (format == null) 
            format = "yyyy/MM/dd HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date sDate;
        Date eDate;
        long day2day = 0;
        try {
            sDate = sdf.parse(startDate);
            eDate = sdf.parse(endDate);
            day2day = (eDate.getTime() - sDate.getTime()) / (1000*60*60*24);
        } catch(Exception e) {
            throw new Exception("wrong format string");
        }

        return day2day;
    }

    public int[] getStoryBtin(String lang, String movie, String count){
    	int[] tmpStoryBtn = new int[2];
        if(Common.langType.equals("en")){
            if(movie.indexOf("cruz1") > -1){
            	tmpStoryBtn[0] = R.drawable.cruz1_bt1_en;
            	tmpStoryBtn[1] = 0;
            }else if(movie.indexOf("cruz2") > -1){
            	tmpStoryBtn[0] = R.drawable.cruz2_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.cruz2_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("dilbar1") > -1){
            	tmpStoryBtn[0] = R.drawable.dilbar1_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.dilbar1_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("dilbar2") > -1){
            	tmpStoryBtn[0] = R.drawable.dilbar2_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.dilbar2_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("lai1") > -1){
            	tmpStoryBtn[0] = R.drawable.lai1_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.lai1_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("lai2") > -1){
            	tmpStoryBtn[0] = R.drawable.lai2_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.lai2_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("moonsr1") > -1){
            	tmpStoryBtn[0] = R.drawable.moonsr1_bt1_en;
            }else if(movie.indexOf("parkjj1") > -1){
            	tmpStoryBtn[0] = R.drawable.parkjj1_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.parkjj1_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("yoonsh1") > -1){
            	tmpStoryBtn[0] = R.drawable.yoonsh1_bt1_en;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.yoonsh1_bt2_en;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }       		            	
        }else{
            if(movie.indexOf("cruz1") > -1){
            	tmpStoryBtn[0] = R.drawable.cruz1_bt1;
            }else if(movie.indexOf("cruz2") > -1){
            	tmpStoryBtn[0] = R.drawable.cruz2_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.cruz2_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("dilbar1") > -1){
            	tmpStoryBtn[0] = R.drawable.dilbar1_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.dilbar1_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("dilbar2") > -1){
            	tmpStoryBtn[0] = R.drawable.dilbar2_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.dilbar2_bt2;
            	}
            }else if(movie.indexOf("lai1") > -1){
            	tmpStoryBtn[0] = R.drawable.lai1_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.lai1_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("lai2") > -1){
            	tmpStoryBtn[0] = R.drawable.lai2_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.lai2_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("moonsr1") > -1){
            	tmpStoryBtn[0] = R.drawable.moonsr1_bt1;
            }else if(movie.indexOf("parkjj1") > -1){
            	tmpStoryBtn[0] = R.drawable.parkjj1_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.parkjj1_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }else if(movie.indexOf("yoonsh1") > -1){
            	tmpStoryBtn[0] = R.drawable.yoonsh1_bt1;
            	if(count.equals("2")){
            		tmpStoryBtn[1] = R.drawable.yoonsh1_bt2;
            	}else{
            		tmpStoryBtn[1] = 0;
            	}
            }       		            	
        }
        	
    	return tmpStoryBtn;
    }

	    
}
