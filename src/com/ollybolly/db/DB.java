package com.ollybolly.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.Date;


public class DB {
	 protected static DB inADBManager=null;
	  
	 private SQLiteDatabase db;
	 private final static String DB_NAME="ollybolly.db";                        //DB name setting
	 private final static int DB_MODE=Context.MODE_PRIVATE;
	 
	 //++T_NAME_MYBOX 시작
	 public final static String T_NAME_VERSION="version";
	 public final static String T_NAME_COUNTRY="country";
	 public final static String T_NAME_ITEM="item";
	 public final static String T_NAME_MYBOX="mybox";	 
	//++T_NAME_MYBOX 끝	 

	 
	 //++T_NAME_VERSION 시작
	 public static final String COLUMN_VERSION = "version";  
	 public static final String COLUMN_DATE = "date";  
	 public static final String COLUMN_CONTENTS_UPDATES = "contents_update";
	 public static final String COLUMN_API_DOMAIN = "api_domain";
	 public static final String COLUMN_IMG_PATH = "img_path";
	 public static final String COLUMN_IMG_POSTFIX_MAINTITLE = "img_postfix_maintitle";
	 public static final String COLUMN_IMG_POSTFIX_MAINFLAG = "img_postfix_mainflag";
	 public static final String COLUMN_IMG_POSTFIX_TITLE = "img_postfix_title";
	 public static final String COLUMN_IMG_POSTFIX_TITLE_EN = "img_postfix_title_en";
	 public static final String COLUMN_IMG_POSTFIX_FLAG = "img_postfix_flag";
	 public static final String COLUMN_IMG_POSTFIX_BG = "img_postfix_bg";
	 public static final String COLUMN_IMG_POSTFIX_BG_EN = "img_postfix_bg_en";
	 public static final String COLUMN_IMG_POSTFIX_BT = "img_postfix_bt";
	 public static final String COLUMN_IMG_POSTFIX_BT_EN = "img_postfix_bt_en";
	 public static final String COLUMN_IMG_POSTFIX_BT_MOV = "img_postfix_bt_mov";
	 public static final String COLUMN_IMG_POSTFIX_BT_MOV_EN = "img_postfix_bt_mov_en";
	 //++T_NAME_VERSION 끝
	 

	 //++T_NAME_COUNTRY 시작
	 public static final String COLUMN_COUNTRY_CODE = "country_code";
	 public static final String COLUMN_COUNTRY_UPDATE = "country_update";
	 public static final String COLUMN_LANGLUAGE_EN = "language_en";
	 public static final String COLUMN_LANGLUAGE_KO = "language_ko";
	 public static final String COLUMN_LANGLUAGE_NL = "language_nl";
	 public static final String COLUMN_MOVIE_URL = "movie_url";
	//++T_NAME_COUNTRY 끝
	 
	//++T_NAME_ITEM 시작
	 public static final String COLUMN_GUID = "guid";
	 
	 public static final String COLUMN_TITLE_EN = "title_en";
	 public static final String COLUMN_TITLE_KO = "title_ko";
	 public static final String COLUMN_AUTHOR_EN = "author_en";
	 public static final String COLUMN_AUTHOR_KO = "author_ko";
	 
	 public static final String COLUMN_PUBDATE = "pubdate";
	 
	 public static final String COLUMN_DESCRIPTION_EN = "description_en";
	 public static final String COLUMN_DESCRIPTION_KO = "description_ko";
	 
	 
	 public static final String COLUMN_MEDIA_CONTENT_URL = "media_content_url";
	 
	 
	 public static final String COLUMN_MEDIA_CONTENT_TYPE = "media_content_type";
	 public static final String COLUMN_MEDIA_CONTENT_DURATION = "media_content_duration";
	 
	 
	 public static final String COLUMN_MEDIA_CONTENT_COUNT = "media_content_count";
	 
	 
	 public static final String COLUMN_MEDIA_LOCATION_EN = "media_location_en";
	 
	 public static final String COLUMN_MEDIA_LOCATION_KO = "media_location_ko";
	 public static final String COLUMN_MEDIA_TITLE = "media_title";
	 public static final String COLUMN_CREDIT_OWNER_EN = "media_credit_owner_en";
	 public static final String COLUMN_CREDIT_WRITER_EN = "media_credit_writer_en";
	 public static final String COLUMN_CREDIT_ILLUSTRATOR_EN = "media_credit_illustrator_en";
	 public static final String COLUMN_CREDIT_OWNER_KO = "media_credit_owner_ko";
	 public static final String COLUMN_CREDIT_WRITER_KO = "media_credit_writer_ko";
	 public static final String COLUMN_CREDIT_ILLUSTRATOR_KO = "media_credit_illustrator_ko";	 
	 
	 public static final String COLUMN_THUMBNAIL = "media_thumbnail";  
		//++T_NAME_ITEM 끝	 

	 
	 private final static String DBG_TAG="[db_ollybolly]";
	 private Context _context=null;

	 
	
	public DB(Context context)
	{
		
		if(context==null)
			throw new SQLException("fail to open storemanager: context parameter is null");
		
		_context = context;
		
		openDB();
		createTable();
		//dropTable();
	}
	
	
	/**
	 * @return
	 */
	public static DB getInstance(Context context) {
		if(inADBManager!=null)
			return inADBManager;
		
		inADBManager = new DB(context);
		return inADBManager;
	}
	
	//DB open
    public void openDB(){
     db = _context.openOrCreateDatabase(DB_NAME, DB_MODE, null);
     //String path = db.getPath();
     
    }
    
    public void close(){
    	db.close();
    }
    
    //Table Create
    public void createTable() {
    	
    	//++버전 테이블 생성
	     String sql = "create table if not exists ";
	     sql += T_NAME_VERSION;
	     sql += "("+COLUMN_VERSION+" text,";
	     sql += COLUMN_DATE+" text,";
	     sql += COLUMN_CONTENTS_UPDATES+" text,";
	     sql += COLUMN_API_DOMAIN+" text,";
	     sql += COLUMN_IMG_PATH+" text,";
	     sql += COLUMN_IMG_POSTFIX_MAINTITLE+" text,";
	     sql += COLUMN_IMG_POSTFIX_MAINFLAG+" text,";
	     sql += COLUMN_IMG_POSTFIX_TITLE+" text,";
	     sql += COLUMN_IMG_POSTFIX_TITLE_EN+" text,";
	     sql += COLUMN_IMG_POSTFIX_FLAG+" text,";
	     sql += COLUMN_IMG_POSTFIX_BG+" text,";
	     sql += COLUMN_IMG_POSTFIX_BG_EN+" text,";
	     sql += COLUMN_IMG_POSTFIX_BT+" text,";
	     sql += COLUMN_IMG_POSTFIX_BT_EN+" text,";
	     sql += COLUMN_IMG_POSTFIX_BT_MOV+" text,";
	     sql += COLUMN_IMG_POSTFIX_BT_MOV_EN+" text";
	     sql += ")";
		 
		 
	     try{
	      db.execSQL(sql);
	     }catch (SQLException se) {
	           Log.e("T_NAME_VERSION createTable(): ",se.toString());
	           throw new SQLException("fail to T_NAME_VERSION create table: version:"+se.toString());
	     }finally{
	    	 //Log.e("table create","table create");
	     }
		 
	     //++국가별 정보
	     sql = "create table if not exists ";
	     sql += T_NAME_COUNTRY;
	     sql += "( idx integer primary key autoincrement,";
	     sql += COLUMN_COUNTRY_CODE+" text,";
	     sql += COLUMN_COUNTRY_UPDATE+" text,";
	     sql += COLUMN_LANGLUAGE_EN+" text,";
	     sql += COLUMN_LANGLUAGE_KO+" text,";
	     sql += COLUMN_LANGLUAGE_NL+" text,";
	     sql += COLUMN_MOVIE_URL+" text";
	     sql += ")";
		 
		 
	     try{
	      db.execSQL(sql);
	     }catch (SQLException se) {
	           Log.e("T_NAME_COUNTRY createTable(): ",se.toString());
	           throw new SQLException("fail to T_NAME_COUNTRY create table: country:"+se.toString());
	     }finally{
	    	 //Log.e("table create","table create");
	     }
		 
	     //++동화 리스트
	     sql = "create table if not exists ";
	     sql += T_NAME_ITEM;
	     sql += "( "+COLUMN_GUID+" integer ,";
	     
	     sql += COLUMN_TITLE_EN+" text,";
	     sql += COLUMN_TITLE_KO+" text,";
	     sql += COLUMN_AUTHOR_EN+" text,";
	     sql += COLUMN_AUTHOR_KO+" text,";
	     
	     sql += COLUMN_PUBDATE+" text,";
	     
	     sql += COLUMN_DESCRIPTION_EN+" text,";	     
	     sql += COLUMN_DESCRIPTION_KO+" text,";	    
	     
	     sql += COLUMN_MEDIA_CONTENT_URL+" text,";
	     
	     sql += COLUMN_MEDIA_CONTENT_TYPE+" text,";
	     sql += COLUMN_MEDIA_CONTENT_DURATION+" text,";
	     
	     sql += COLUMN_MEDIA_CONTENT_COUNT+" text,";	     
	     sql += COLUMN_MEDIA_LOCATION_EN+" text,";
	     
	     sql += COLUMN_MEDIA_LOCATION_KO+" text,";
	     sql += COLUMN_MEDIA_TITLE+" text,";	     
	     sql += COLUMN_CREDIT_OWNER_EN+" text,";
	     sql += COLUMN_CREDIT_WRITER_EN+" text,";
	     sql += COLUMN_CREDIT_ILLUSTRATOR_EN+" text,";
	     sql += COLUMN_CREDIT_OWNER_KO+" text,";
	     sql += COLUMN_CREDIT_WRITER_KO+" text,";
	     sql += COLUMN_CREDIT_ILLUSTRATOR_KO+" text,";	    
	    
	     sql += COLUMN_THUMBNAIL+" text";
	     sql += ")";
		 
		 
	     try{
	      db.execSQL(sql);
	     }catch (SQLException se) {
	           Log.e("T_NAME_ITEM createTable(): ",se.toString());
	           throw new SQLException("fail to T_NAME_ITEM create table: item:"+se.toString());
	     }finally{
	    	 //Log.e("table create","table create");
	     }
	     
	     //++보관함
	     sql = "create table if not exists ";
	     sql += T_NAME_MYBOX;
	     sql += "( ";
	     sql += COLUMN_GUID+" integer";     
	     sql += ")";
		 
		 
	     try{
	      db.execSQL(sql);
	     }catch (SQLException se) {
	           Log.e("T_NAME_MYBOX createTable(): ",se.toString());
	           throw new SQLException("fail to T_NAME_MYBOX create table: item:"+se.toString());
	     }finally{
	    	 //Log.e("table create","table create");
	     }
	     
	     
    }
    
    //Table Drop
    public void dropTable() {
     
	     try{
		     String sql = "drop table  if exists ";
		     sql += T_NAME_VERSION;
	    	 db.execSQL(sql);
	    	 
	    	 //Log.d(DBG_TAG, "success drop table");
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to drop table:"+se.toString());
	    	 throw new SQLException("fail to drop table:"+se.toString());
	     }
     
	     try{
		     String sql = "drop table  if exists ";
		     sql += T_NAME_COUNTRY;
	    	 db.execSQL(sql);
	    	 
	    	//Log.d(DBG_TAG, "success drop table"+T_NAME_COUNTRY);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to drop table:"+T_NAME_COUNTRY+se.toString());
	    	 throw new SQLException("fail to drop table:"+T_NAME_COUNTRY+se.toString());
	     }
	     
	     try{
		     String sql = "drop table  if exists ";
		     sql += T_NAME_ITEM;
	    	 db.execSQL(sql);
	    	 
	    	//Log.d(DBG_TAG, "success drop table"+T_NAME_ITEM);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to drop table:"+T_NAME_ITEM+se.toString());
	    	 throw new SQLException("fail to drop table:"+T_NAME_ITEM+se.toString());
	     }
	     
	     try{
		     String sql = "drop table  if exists ";
		     sql += T_NAME_MYBOX;
	    	 db.execSQL(sql);
	    	 
	    	//Log.d(DBG_TAG, "success drop table"+T_NAME_MYBOX);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to drop table:"+T_NAME_MYBOX+se.toString());
	    	 throw new SQLException("fail to drop table:"+T_NAME_MYBOX+se.toString());
	     }	     
	     
	     
    }
    
    //Table Drop
    public void dropTable(String tableName) {
     
	     try{
		     String sql = "drop table  if exists ";
		     sql += tableName;
	    	 db.execSQL(sql);
	    	 
	    	 //Log.d(DBG_TAG, "success drop table=="+tableName);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to drop "+tableName+":"+se.toString());
	    	 throw new SQLException("fail to drop "+tableName+":"+se.toString());
	     }
     
    }

    //Table Drop
    public void deleteTable(String tableName) {
     
	     try{
		     String sql = "delete from  ";
		     sql += tableName;
	    	 db.execSQL(sql);
	    	 
	    	 //Log.d(DBG_TAG, "success delete table=="+tableName);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to delete "+tableName+":"+se.toString());
	    	 throw new SQLException("fail to delete "+tableName+":"+se.toString());
	     }
     
    }
    
    //Table Drop
    public void deleteTable(String tableName,String nWhere) {
     
	     try{
		     String sql = "delete from  ";
		     sql += tableName;
		     sql += " "+ nWhere;
	    	 db.execSQL(sql);
	    	 
	    	 //Log.d(DBG_TAG, "success delete table=="+tableName);
	    	 
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to delete "+tableName+":"+se.toString());
	    	 throw new SQLException("fail to delete "+tableName+":"+se.toString());
	     }
     
    }
    
    //++총 데이터 카운트 뽑아오기
	public int getTableCount(String TB_NAME){
		
		 String sql = "SELECT COUNT(*) FROM  ";
		 sql += TB_NAME ;
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     
	     int tableCount = 0;
	     int resultCount = 0;
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 tableCount = cursor.getCount();
	    	
	    	 if(tableCount > 0 ){
    			 
	    		 resultCount			= cursor.getInt(0);
	    		  
	    		 cursor.moveToNext();
	    		 
	    	 }
	    	 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to getTableCount:"+se.toString());
	    	 throw new SQLException("fail to getTableCount list:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get getTableCount list:"+se.toString());
	    	 throw new SQLException("fail to get getTableCount list:"+se.toString());
	     }finally{
	    	 cursor.close();
	     } 
	    return resultCount;
	}	
	
	//++version 테이블에 데이터 넣기
	public void insertVersion(String[] result) 
	throws SQLException {
		
		String sql = "INSERT INTO ";
		try{
			
		     sql += T_NAME_VERSION;
		     sql += "("+COLUMN_VERSION+","+COLUMN_DATE+","+COLUMN_CONTENTS_UPDATES+","+COLUMN_API_DOMAIN+","+COLUMN_IMG_PATH+","+COLUMN_IMG_POSTFIX_MAINTITLE+","+COLUMN_IMG_POSTFIX_MAINFLAG+",";
		     sql += COLUMN_IMG_POSTFIX_TITLE+","+COLUMN_IMG_POSTFIX_TITLE_EN+","+COLUMN_IMG_POSTFIX_FLAG+","+COLUMN_IMG_POSTFIX_BG+","+COLUMN_IMG_POSTFIX_BG_EN+","+COLUMN_IMG_POSTFIX_BT+",";
		     sql += COLUMN_IMG_POSTFIX_BT_EN+","+COLUMN_IMG_POSTFIX_BT_MOV+","+COLUMN_IMG_POSTFIX_BT_MOV_EN;
		     sql += ") VALUES ( '";
		     sql += result[0] +"','"+result[1] +"','"+result[2] +"','"+result[3] +"','"+result[4] +"','"+result[5] +"','"+result[6] +"','"+result[7] +"','"+result[8] +"','"+result[9] +"','"+result[10] +"','"+result[11] +"','"+result[12] +"','"+result[13] +"','"+result[14] +"','"+result[15]+"'";
		     sql += " )";
		     
		     //Log.d(DBG_TAG,sql);
		     
		     db.execSQL(sql);
		     //Log.e(DBG_TAG,"success to insert version:"+result[0].length());
	     
			}catch (SQLException se) {
				Log.e(DBG_TAG,"fail to insert version:"+se.toString());
				throw new SQLException("fail to insert version:"+se.toString());
			}
	}
	
	//++country 테이블에 데이터 넣기
	public void insertCountry(ArrayList<Object> arrList) 
	throws SQLException {
		//Log.e("tmpList","=="+"'"+ result[0] +"','"+result[1] +"','"+result[2] +"','"+result[3] +"','"+result[4] +"','"+result[5]+"'");
		
		/*
		for (int i = 0; i < arrList.size(); i++){
       	 System.out.println("i=="+i+"=="+arrList.get(i));
       }
	   */
		
		
		String sql = "INSERT INTO ";
		try{
			
			 if(arrList.size() == 5){
				 
			     sql += T_NAME_COUNTRY;
			     sql += "("+COLUMN_COUNTRY_CODE+","+COLUMN_COUNTRY_UPDATE+","+COLUMN_LANGLUAGE_EN+","+COLUMN_LANGLUAGE_KO+","+COLUMN_LANGLUAGE_NL;
			     sql += ") VALUES ( '";
			     sql += arrList.get(0) +"','"+arrList.get(1) +"','"+arrList.get(2) +"','"+arrList.get(3) +"','"+arrList.get(4) +"'";
			     sql += " )";
			     
			     //Log.d(DBG_TAG,sql);
			     
			     db.execSQL(sql);
			     //Log.e(DBG_TAG,"success to insert country:"+arrList.size());
			 }else if(arrList.size() == 6){
				 sql += T_NAME_COUNTRY;
			     sql += "("+COLUMN_COUNTRY_CODE+","+COLUMN_COUNTRY_UPDATE+","+COLUMN_LANGLUAGE_EN+","+COLUMN_LANGLUAGE_KO+","+COLUMN_LANGLUAGE_NL+","+COLUMN_MOVIE_URL;
			     sql += ") VALUES ( '";
			     sql += arrList.get(0) +"','"+arrList.get(1) +"','"+arrList.get(2) +"','"+arrList.get(3) +"','"+arrList.get(4) +"','"+arrList.get(5) +"'";
			     sql += " )";
			     
			     db.execSQL(sql);
			     //Log.e(DBG_TAG,"success to insert country:"+arrList.size());			     
			     
			 }

	     
			}catch (SQLException se) {
				Log.e(DBG_TAG,"fail to insert country:"+se.toString());
				throw new SQLException("fail to insert country:"+se.toString());
			}
			
			
	}
	
	//++item 테이블에 데이터 넣기
	public void insertItem(int iGuid, ArrayList<Object> arrList)
	throws SQLException {
		
		String nation = "";
		nation = (String) arrList.get(11);
		
		
		

		String sql = "INSERT INTO ";
		try{
			
		     sql += T_NAME_ITEM;
		     sql += "("+COLUMN_GUID+",";
		     
		    
		     sql += COLUMN_TITLE_EN+",";
		     sql += COLUMN_TITLE_KO+",";
		     sql += COLUMN_AUTHOR_EN+",";
		     sql += COLUMN_AUTHOR_KO+",";
		     
		     sql += COLUMN_PUBDATE+",";
		     
		     sql += COLUMN_DESCRIPTION_EN+",";
		     sql += COLUMN_DESCRIPTION_KO+",";
		     
		     
		     sql += COLUMN_MEDIA_CONTENT_URL+",";
		     
		     
		     sql += COLUMN_MEDIA_CONTENT_TYPE+",";
		     sql += COLUMN_MEDIA_CONTENT_DURATION+",";
		    
		     sql += COLUMN_MEDIA_CONTENT_COUNT+",";
		     sql += COLUMN_MEDIA_LOCATION_EN+",";
		     
		     sql += COLUMN_MEDIA_LOCATION_KO+",";
		     sql += COLUMN_MEDIA_TITLE+",";
		     sql += COLUMN_CREDIT_OWNER_EN+",";
		     sql += COLUMN_CREDIT_WRITER_EN+",";
		     sql += COLUMN_CREDIT_ILLUSTRATOR_EN+",";
		     sql += COLUMN_CREDIT_OWNER_KO+",";
		     sql += COLUMN_CREDIT_WRITER_KO+",";
		     sql += COLUMN_CREDIT_ILLUSTRATOR_KO+",";
		     
		     sql += COLUMN_THUMBNAIL;
		     sql += ") VALUES ( ";
		     sql +=  iGuid ;
		     sql +=  ",'"+ getWordReplace((String)arrList.get(0)) + "'";	     
		     sql +=  ",'"+ getWordReplace((String)arrList.get(1)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(2)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(3)) + "'";
		     sql +=  ",'"+ getStringDate((String)arrList.get(4)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(5)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(6)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(7)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(8)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(9)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(10)) + "'";
		     sql +=  ",'"+ nation.toLowerCase() + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(12)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(13)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(14)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(15)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(16)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(17)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(18)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(19)) + "'";
		     sql +=  ",'"+ getWordReplace((String)arrList.get(20)) + "'";
		     
		     sql += " )";
		     
		     //Log.d(DBG_TAG,sql);
		     
		     db.execSQL(sql);
		     //Log.e(DBG_TAG,"success to insert item:"+sql);
	     
			}catch (SQLException se) {
				Log.e(DBG_TAG,"fail to insert item:"+se.toString());
				throw new SQLException("fail to insert item:"+se.toString());
			}
	}
	
	// 현재 버전 리스트 가져 오기
	public ArrayList<String> getVersionList(){
		
		ArrayList<String> nowVersionList = new ArrayList<String>();
		
		 String sql = "SELECT * FROM  ";
		 sql += T_NAME_VERSION + " order by "+COLUMN_VERSION+" desc ";
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 int cnt = cursor.getCount();
	    	 
	    	 if(cnt > 0 ){
	    			 for(int i=0; i<16; i++){
	    				 nowVersionList.add(cursor.getString(i));
	    		 	 }	
	    			 cursor.moveToNext();
	    	 }	    	 
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get version:"+se.toString());
	    	 throw new SQLException("fail to get version list:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get version list:"+se.toString());
	    	 throw new SQLException("fail to get user version list:"+se.toString());
	     }finally{
	    	 cursor.close();
	     } 
	    return nowVersionList;
	}	
	
	// 현재 버전 리스트 가져 오기
	public String getCountryImage(){
		
		 String strImage = "";
		 String sql = "SELECT "+COLUMN_COUNTRY_CODE+" FROM  ";
		 sql += T_NAME_COUNTRY + " order by idx desc LIMIT 1 ";
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 int cnt = cursor.getCount();
	    	 
	    	 if(cnt > 0 ){
	    		 strImage =  cursor.getString(0);
	    		 	 
	    	 }	    	 
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get country:"+se.toString());
	    	 throw new SQLException("fail to get country list:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get country list:"+se.toString());
	    	 throw new SQLException("fail to get user country list:"+se.toString());
	     }finally{
	    	 cursor.close();
	     } 
	    return strImage;
	}	
	
	// 현재 국가 리스트 가져 오기
	public Cursor getCountryList(){
		
		
		 String sql = "SELECT * FROM  ";
		 sql += T_NAME_COUNTRY + "  order by idx asc ";
	     
	     //Log.d(DBG_TAG,sql);

		 Cursor nowCursorList = db.rawQuery(sql, null);
	    
	     return nowCursorList;
	}	
	
	// 현재 국가 리스트 가져 오기
	public String getCountryImage(String nWhere){
		

	     String tempImage = "";
	     
		 String sql = "SELECT * FROM  ";
		 sql += T_NAME_COUNTRY + nWhere + "  order by idx asc ";
	     
	     //Log.d(DBG_TAG,sql);
	     
	     Cursor cursor = db.rawQuery(sql, null);	     
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 int tmpCount = cursor.getCount();
	    	 
	    	if(tmpCount > 0){
	    		tempImage = cursor.getString(1);
	    		//Log.e("tempImage",tempImage);
	    	}
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get getCountryImage:"+se.toString());
	    	 throw new SQLException("fail to get getCountryImage list:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get MyGymItem list:"+se.toString());
	    	 throw new SQLException("fail to get user getCountryImage list:"+se.toString());
	     }finally{
	    	 cursor.close();
	     }
	     

	    
	     return tempImage;
	}	
	
	
	// 현재 국가 리스트 카운트 가져오기
	public int getCountryCount(){
		int tmpCount = 0;
		
		 String sql = "SELECT * FROM  ";
		 sql += T_NAME_COUNTRY;
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 tmpCount = cursor.getCount();
	    	 
	    	
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get getCountryCount:"+se.toString());
	    	 throw new SQLException("fail to get getCountryCount list:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get MyGymItem list:"+se.toString());
	    	 throw new SQLException("fail to get user getCountryCount list:"+se.toString());
	     }finally{
	    	 cursor.close();
	     }
	    
	     return tmpCount;
	}	
	// 현재 국가별 동화 리스트 가져 오기
		public Cursor getCountryAllItem(){
			
			
			String sql = "SELECT "+COLUMN_GUID+","+COLUMN_PUBDATE+","+COLUMN_MEDIA_LOCATION_EN+","+COLUMN_THUMBNAIL+","+COLUMN_MEDIA_CONTENT_URL+","+COLUMN_MEDIA_CONTENT_COUNT+","+COLUMN_DESCRIPTION_EN+","+COLUMN_DESCRIPTION_KO;
			 sql += ","+COLUMN_TITLE_EN + ","+ COLUMN_TITLE_KO + ","+ COLUMN_CREDIT_WRITER_EN + ","+ COLUMN_CREDIT_WRITER_KO + ","+ COLUMN_CREDIT_ILLUSTRATOR_EN + ","+ COLUMN_CREDIT_ILLUSTRATOR_KO;
			 sql += " FROM  ";
			 sql += T_NAME_ITEM;
		     
		     //Log.d(DBG_TAG,sql);

			 Cursor nowCursorList = db.rawQuery(sql, null);
		    
		     return nowCursorList;
		}	
	
	// 현재 국가별 동화 리스트 가져 오기
	public Cursor getCountryItem(String nOrder, String nWhere){
		
		/*
		Cursor test = db.rawQuery("SELECT datetime(pubdate,'localtime') FROM  item  where media_location_en = 'mongolia'    order by datetime(pubdate,'localtime') asc, guid asc ", null);
		test.moveToFirst();
  		
 		 int crsItemCount = test.getCount();
 		Log.e("crsItemCountdfdffdfdfd",""+crsItemCount);
		  while (test.moveToNext()) {
			  Log.e("test.getString(3)",test.getString(0));
		  }	
		  */
		
		
		if(nOrder.equals("")){
			nOrder = " order by pubdate desc, "+COLUMN_TITLE_KO+" asc ";
		}
		
	     
		 String sql = "SELECT "+COLUMN_GUID+","+COLUMN_PUBDATE+","+COLUMN_MEDIA_LOCATION_EN+","+COLUMN_THUMBNAIL+","+COLUMN_MEDIA_CONTENT_URL+","+COLUMN_MEDIA_CONTENT_COUNT+","+COLUMN_DESCRIPTION_EN+","+COLUMN_DESCRIPTION_KO;
		 sql += ","+COLUMN_TITLE_EN + ","+ COLUMN_TITLE_KO + ","+ COLUMN_CREDIT_WRITER_EN + ","+ COLUMN_CREDIT_WRITER_KO + ","+ COLUMN_CREDIT_ILLUSTRATOR_EN + ","+ COLUMN_CREDIT_ILLUSTRATOR_KO;
		 sql += " FROM  ";
		 sql += T_NAME_ITEM+ " " + nWhere + "  " + nOrder;
	     
	     //Log.d(DBG_TAG,sql);

		 Cursor nowCursorList = db.rawQuery(sql, null);
	    
	     return nowCursorList;
	}	
	
	public String getStringDate(String date){
		 String time2 = "";
		//++날짜 String에서 Date로 형변환 시작
		try{
			  String strDate = date;
			  
			  long time = Date.parse(strDate);
	
		      String fmt = "yyyy-MM-dd HH:mm:ss"; 
		      
		      SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		      time2 = sdf.format(new Date(time));
	
	
		       
			   //Log.e("SimpleDateFormat",""+time2);
		}catch(Exception e){
			Log.e("getStringDate","getStringDate"+e.toString());
		}finally{
			
		}
		//++날짜 String에서 Date로 형변환 끝		
		
		return time2;
	}

	public String getWordReplace(String str){
		String tmpStr = "";

		tmpStr = str.replace("\"", "”");
		tmpStr = tmpStr.replace("\'", "´");
		
		//tmpStr = str.replace("\"", "");
		//tmpStr = tmpStr.replace("\'", "");		
		
		return tmpStr;
	}
	
	//++mybox 테이블에 데이터 넣기
	public boolean insertMybox(int guid) 
	throws SQLException {
		boolean isOk = false;
		String sql = "";
		
		//++ 보관함에 이미 저장 되어 있는지 체크
	    int tmpCount = 0;
		
	     sql = "SELECT * FROM  ";
            sql += T_NAME_MYBOX;
	     sql += " WHERE "+COLUMN_GUID+" = " + guid;
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 tmpCount = cursor.getCount();
	    	 
	    	
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get getMyboxCount:"+se.toString());
	    	 throw new SQLException("fail to get getMyboxCount:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get MgetMyboxCount:"+se.toString());
	    	 throw new SQLException("fail to get user getMyboxCount:"+se.toString());
	     }finally{
	    	 cursor.close();
	     }		
		
	    if(tmpCount == 0 ){
			sql = "INSERT INTO ";
			try{
				
			     sql += T_NAME_MYBOX;
			     sql += "("+COLUMN_GUID+") VALUES ( ";
			     sql += guid;
			     sql += " )";
			     
			     //Log.d(DBG_TAG,sql);
			     
			     db.execSQL(sql);
		     
				}catch (SQLException se) {
					Log.e(DBG_TAG,"fail to insert mybox:"+se.toString());
					throw new SQLException("fail to insert mybox:"+se.toString());
				}finally{
					isOk = true;
				}
	    }
	    
	    return isOk;
	}
	
	//++mybox 테이블에 데이터 넣기
	public boolean getGuidMybox(int guid) 
	throws SQLException {
		boolean isOk = false;
		String sql = "";
		
		//++ 보관함에 이미 저장 되어 있는지 체크
	    int tmpCount = 0;
		
	     sql = "SELECT * FROM  ";
            sql += T_NAME_MYBOX;
	     sql += " WHERE "+COLUMN_GUID+" = " + guid;
	     
	     //Log.d(DBG_TAG,sql);

	     Cursor cursor = db.rawQuery(sql, null);
	     
	     
	     try{
	   			
	    	 cursor.moveToFirst();
	    	 tmpCount = cursor.getCount();
	    	 
	    	
	    		 
	     }catch (SQLException se) {
	    	 Log.e(DBG_TAG,"fail to get getMyboxCount:"+se.toString());
	    	 throw new SQLException("fail to get getMyboxCount:"+se.toString());
	     }catch (Exception se) {
	    	 Log.e(DBG_TAG,"fail to get MgetMyboxCount:"+se.toString());
	    	 throw new SQLException("fail to get user getMyboxCount:"+se.toString());
	     }finally{
	    	 cursor.close();
	     }		
		
	     if(tmpCount > 0){
	    	 isOk = true;
	     }
	    
	    return isOk;
	}	
	
	// 현재 보관함 리스트 가져오기
	public Cursor getMyBoxItem(){
		
		String tmpOrder = " order by pubdate desc, "+COLUMN_TITLE_KO+" asc ";
		
		 String sql = "SELECT A."+COLUMN_GUID+","+COLUMN_PUBDATE+","+COLUMN_MEDIA_LOCATION_EN+","+COLUMN_THUMBNAIL+","+COLUMN_MEDIA_CONTENT_URL+","+COLUMN_MEDIA_CONTENT_COUNT+","+COLUMN_DESCRIPTION_EN+","+COLUMN_DESCRIPTION_KO;
		 sql += ","+COLUMN_TITLE_EN + ","+ COLUMN_TITLE_KO + ","+ COLUMN_CREDIT_WRITER_EN + ","+ COLUMN_CREDIT_WRITER_KO + ","+ COLUMN_CREDIT_ILLUSTRATOR_EN + ","+ COLUMN_CREDIT_ILLUSTRATOR_KO;
		 sql += " FROM  ";
		 sql += T_NAME_ITEM + " A INNER JOIN " + T_NAME_MYBOX + " B ON A.guid = B.guid  " + tmpOrder;
	     
	     //Log.d(DBG_TAG,sql);

		 Cursor nowCursorList = db.rawQuery(sql, null);
	    
	     return nowCursorList;
	}	
	
}
