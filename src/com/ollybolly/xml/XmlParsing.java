package com.ollybolly.xml;

import java.io.InputStream;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;



import android.util.Log;
import android.widget.*;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import android.content.Context;

public final class XmlParsing
{
	Context mContext;
	
	String versionUrl = "올리볼리_버전_api";
	String indexUrl = "올리볼리_인덱스_api";
	
	public XmlParsing(Context mcontext){
		mContext = mcontext;
	}
	
	 private InputStream getInputStream(String para_url){
		  while(true){
			   try{
				    URL url = new URL(para_url);
				    URLConnection con= url.openConnection();
				    InputStream is = con.getInputStream();
				    return is;
			   } catch (Exception e) {
			    	//Log.d("mytag", e.getMessage());
			   }
		  }
	 }
	 
	//++버전 xml 파싱하기 시작
	public String[] getXmlVersionResult(){
		
    		boolean[] isContent = new boolean[16];     
    		String[] inContent = new String[16];     		
			try{
	        	
	        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
	        	XmlPullParser parser = parserCreator.newPullParser();
	        	InputStream stream = getInputStream(versionUrl);
	        	
	        	parser.setInput( stream, "utf-8" );
	        	int parserEvent = parser.getEventType();
	        	
	            String tag;
	        	
	        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
	        		  switch(parserEvent){
	        		
		        		case XmlPullParser.TEXT:
		        			
		        			tag = parser.getName() ;
		        			
		        			//++true일 때 배열에 저장
		        		    for(int i = 0; i < 16; i++){
		                        if (isContent[i]) {
		                        	inContent[i] =  parser.getText();                                     	
		                        }
		        		    }	                        
		        		break;
	                    case XmlPullParser.END_TAG:
	                        tag = parser.getName();
	                        if (tag.compareTo("version") == 0) {
	                        	isContent[0] = false;
	                        }
	                        
	                         if (tag.compareTo("date") == 0) {
	                        	 isContent[1] = false;
	                        }
	                         
	                         if (tag.compareTo("contents_update") == 0) {
	                        	 isContent[2] = false;
	                        }
	                         
	                         if (tag.compareTo("api_domain") == 0) {
	                        	 isContent[3] = false;
	                        }
	                         
	                         if (tag.compareTo("img_path") == 0) {
	                        	 isContent[4] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_maintitle") == 0) {
	                        	 isContent[5] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_mainflag") == 0) {
	                        	 isContent[6] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_title") == 0) {
	                        	 isContent[7] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_title_en") == 0) {
	                        	 isContent[8] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_flag") == 0) {
	                        	 isContent[9] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bg") == 0) {
	                        	 isContent[10] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bg_en") == 0) {
	                        	 isContent[11] = false;
	                        }
	                         
	                        if (tag.compareTo("img_postfix_bt") == 0) {
	                        	 isContent[12] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_en") == 0) {
	                        	 isContent[13] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_mov") == 0) {
	                        	 isContent[14] = false;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_mov_en") == 0) {
	                        	 isContent[15] = false;
	                        }                     
	                     break;	        		
	                    case XmlPullParser.START_TAG:
	                        tag = parser.getName();
	                        
	                        if (tag.compareTo("version") == 0) {
	                        	isContent[0] = true;
	                        }
	                        
	                         if (tag.compareTo("date") == 0) {
	                        	 isContent[1] = true;
	                        }
	                         
	                         if (tag.compareTo("contents_update") == 0) {
	                        	 isContent[2] = true;
	                        }
	                         
	                         if (tag.compareTo("api_domain") == 0) {
	                        	 isContent[3] = true;
	                        }
	                         
	                         if (tag.compareTo("img_path") == 0) {
	                        	 isContent[4] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_maintitle") == 0) {
	                        	 isContent[5] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_mainflag") == 0) {
	                        	 isContent[6] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_title") == 0) {
	                        	 isContent[7] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_title_en") == 0) {
	                        	 isContent[8] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_flag") == 0) {
	                        	 isContent[9] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bg") == 0) {
	                        	 isContent[10] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bg_en") == 0) {
	                        	 isContent[11] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt") == 0) {
	                        	 isContent[12] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_en") == 0) {
	                        	 isContent[13] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_mov") == 0) {
	                        	 isContent[14] = true;
	                        }
	                         
	                         if (tag.compareTo("img_postfix_bt_mov_en") == 0) {
	                        	 isContent[15] = true;
	                        }
	                         
		                        
	                    break;
	        		}
	        		parserEvent = parser.next();
	        	}
	               
	     	
	        }catch( Exception e ){
	        	showNotFoundXml() ;
	       	 	Log.e("dd", "Error in network call", e);
	        }    		
			
	        return inContent;
	       
		}
	//++버전 xml 파싱하기 끝
	
	
	//++국가 리스트 xml 파싱하기 시작
	public HashMap<Integer, ArrayList<Object>> getXmlCountryResult(){
		
		boolean[] isContent = new boolean[6];
		
		HashMap<Integer, ArrayList<Object>> inCountryList = new HashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> arrList = new ArrayList<Object>();
		
		//inList[0][0] = "111";

		try{
        	
        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();
        	
        	
        	InputStream stream = getInputStream(versionUrl);

        	
        	parser.setInput( stream, "utf-8" );
        	int parserEvent = parser.getEventType();
        	
        	
            String tag;
        	
            int i = -1;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		  switch(parserEvent){
        		
	        		case XmlPullParser.TEXT:
	        			tag = parser.getName() ;

	        			if(parser.getDepth() == 3){                   
		        			if(isContent[0]){//++country_code
		        				arrList.add(parser.getText());
		        			}else if(isContent[1]){//++update
		        				arrList.add(parser.getText());
		        			}else if(isContent[2]){//++en
		        				arrList.add(parser.getText());
		        			}else if(isContent[3]){//++ko
		        				arrList.add(parser.getText());
		        			}else if(isContent[4]){//++nl
		        				arrList.add(parser.getText());
		        			}else if(isContent[5]){//++movie_url
		        				arrList.add(parser.getText());
		        			}
	        			}
	        			//Log.e("2",parser.getText()+"=="+"2");
	        		break;
                    case XmlPullParser.END_TAG:
                    	 tag = parser.getName();                    	 
                    	 //Log.e("3",tag+"=="+"3");        
                    	 if(tag.compareTo("country") == 0){
                    		 //Log.e("country==","=="+i+"=="+tag.compareTo("country"));
                    		 //Log.e("inContent==",i+"=="+inContent[0]+"=="+inContent[1]+"=="+inContent[2]+"=="+inContent[3]+"=="+inContent[4]+"=="+inContent[5]);
                    		 inCountryList.put(i,arrList);
                    		 
                    		 arrList = null;
                    		 arrList = new ArrayList<Object>();
                    		 
                    		 
                    		 //Log.e("i==","=="+i+"=="+i);
                    	 }
                    	
                     	//++country 안에 엘리먼트 뽑아오기 시작
                     	if(parser.getDepth() == 3){ 
                     		
 	                        if (tag.compareTo("country_code") == 0) {
 	                        	isContent[0] = false;
 	                        }
                     		
                     		if (tag.compareTo("update") == 0) {
 	                        	isContent[1] = false;
 	                        }
                     		
                     		
                     		if (tag.compareTo("name") == 0) {
                        		isContent[2] = false;
                        		isContent[3] = false;
                        		isContent[4] = false;
 	                        }
                     		
                     		if (tag.compareTo("movie_url") == 0) {
 	                        	isContent[5] = false;
 	                        }                        	

                     	}
                     	//++country 안에 엘리먼트 뽑아오기 끝
                 		
                     	
                     break;	        		
                    case XmlPullParser.START_TAG:
                    	            
                    	tag = parser.getName();
                    	
                    	//Log.e("1",tag+"=="+"1");     
	        			if(parser.getDepth() == 2 && tag.compareTo("country") == 0){          
		        				i++;
	        			}                  	
                    	//++country 안에 엘리먼트 뽑아오기 시작
                    	if(parser.getDepth() == 3){ 
                    		
	                        if (tag.compareTo("country_code") == 0) {
	                        	isContent[0] = true;
	                        }
                    		
                    		if (tag.compareTo("update") == 0) {
	                        	isContent[1] = true;
	                        }
                    		
                    		
	                        if( parser.getAttributeCount() == 1){
	                        	if(parser.getAttributeValue(0).equals("en")){
	                        		isContent[2] = true;
	                        	}else if(parser.getAttributeValue(0).equals("ko")){
	                        		isContent[3] = true;
	                        	}else if(parser.getAttributeValue(0).equals("nl")){
	                        		isContent[4] = true;
	                        	}
	                        }
                    		
                    		if (tag.compareTo("movie_url") == 0) {
	                        	isContent[5] = true;
	                        }                        	

                    	}
                    	//++country 안에 엘리먼트 뽑아오기 끝
                    	
                    break;
        		}
        		parserEvent = parser.next();
        	}
               
     	
        }catch( Exception e ){
        	showNotFoundXml() ;
       	 	Log.e("dd", "Error in network call", e);
        }    		
		
        /*
        Log.e("inCountryList",""+inCountryList.size());
       
        Iterator<Integer> iter = inCountryList.keySet().iterator();
        
        
        while(iter.hasNext())
        {
            int nCount    = iter.next();
            
           ArrayList<Object> tmpList = new ArrayList<Object>();
            
            tmpList = inCountryList.get(nCount);
            
            for (int i = 0; i < tmpList.size(); i++){
            	 System.out.println("==nCount=="+nCount+"==i=="+i+"=="+tmpList.get(i));
            }
            
       	 	//System.out.println ( " value )  "+inList2.get(nCount)  ) ;             
           
        }
        */
        
        return inCountryList;
       
	}	
	//++국가 리스트 xml 파싱하기 끝
	
	//++동화 리스트 xml 파싱하기 시작
	public HashMap<Integer, ArrayList<Object>> getXmlStoryResult(){
		
		boolean[] isContent = new boolean[22];
		
		HashMap<Integer, ArrayList<Object>> inStoryList = new HashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> arrList = new ArrayList<Object>();
		
		//inList[0][0] = "111";

		try{
        	
        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();
        	
        	
        	InputStream stream = getInputStream(indexUrl);

        	
        	parser.setInput( stream, "utf-8" );
        	int parserEvent = parser.getEventType();
        	
        	
            String tag;
        	
            int i = -1;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		  switch(parserEvent){
        		
	        		case XmlPullParser.TEXT:
	        			tag = parser.getName() ;
	        			
	        				if(isContent[0]){//++guid
	        					arrList.add(parser.getText());
	        					//Log.e("guid",parser.getText()+"==");
		        			}else if(isContent[1]){//++title en
		        				arrList.add(parser.getText());
	        					//Log.e("title en",parser.getText()+"==");
		        			}else if(isContent[2]){//++title ko
		        				arrList.add(parser.getText());
	        					//Log.e("title ko",parser.getText()+"==");
		        			}else if(isContent[3]){//++author en
		        				arrList.add(parser.getText());
	        					//Log.e("author en",parser.getText()+"==");
		        			}else if(isContent[4]){//++author ko
		        				arrList.add(parser.getText());
	        					//Log.e("author ko",parser.getText()+"==");
		        			}else if(isContent[5]){//++pubDate
		        				arrList.add(parser.getText());
	        					//Log.e("pubDate",parser.getText()+"==");
		        			}else if(isContent[6]){//++description en
		        				arrList.add(parser.getText());
	        					//Log.e("description en",parser.getText()+"==");
		        			}else if(isContent[7]){//++description ko
		        				arrList.add(parser.getText());
	        					//Log.e("description ko",parser.getText()+"==");
		        			}else if(isContent[8]){//++media:content url
	        					//Log.e("media:content url ",parser.getText()+"==");
		        			}else if(isContent[9]){//++media:content type
	        					//Log.e("media:content type",parser.getText()+"==");
		        			}else if(isContent[10]){//++media:content duration
	        					//Log.e("media:content duration ",parser.getText()+"==");
		        			}else if(isContent[11]){//++media:content count
	        					//Log.e("media:content  count",parser.getText()+"==");
		        			}else if(isContent[12]){//++media:location en
	        					//Log.e("media:location en",parser.getText()+"==");
		        			}else if(isContent[13]){//++media:location ko
	        					//Log.e("media:location ko",parser.getText()+"==");
		        			}else if(isContent[14]){//++media:title
		        				arrList.add(parser.getText());
	        					//Log.e("media:title",parser.getText()+"==");
		        			}else if(isContent[15]){//++media:credit en owner
		        				arrList.add(parser.getText());
	        					//Log.e("media:credit en owner",parser.getText()+"==");
		        			}else if(isContent[16]){//++media:credit en writer
		        				arrList.add(parser.getText());
	        					//Log.e("media:credit en writer",parser.getText()+"==");
		        			}else if(isContent[17]){//++media:credit en illustrator
		        				arrList.add(parser.getText());
	        					//Log.e("media:credit en ollustrator",parser.getText()+"==");
		        			}else if(isContent[18]){//++media:credit ko owner
		        				arrList.add(parser.getText());
	        					//Log.e("media:credit ko owner",parser.getText()+"==");
		        			}else if(isContent[19]){//++media:credit ko writer
		        				arrList.add(parser.getText());
	        					//Log.e("media:credit ko writer",parser.getText()+"==");
		        			}else if(isContent[20]){//++media:credit ko illustor
		        				arrList.add(parser.getText());
	        					//Log.e("description ko",parser.getText()+"==");
		        			}else if(isContent[21]){//++media:thumbnail
	        					//Log.e("media:thumbnail",parser.getText()+"==");
		        			}
	        			
	        		break;
                    case XmlPullParser.END_TAG:
                    	 tag = parser.getName();                    	 
                 		
                    	 if(tag.compareTo("item") == 0){
                    		 inStoryList.put(i,arrList);
                    		 
                    		 arrList = null;
                    		 arrList = new ArrayList<Object>();
                    		 
                    		 
                    		 //Log.e("i==","=="+i+"=="+i);
                    	 }
                    	 
		        			//++false로 변경
		        		    for(int m = 0; m < 22; m++){
		        		    	 isContent[m] = false;
		        		    }
		        		    
                     break;	        		
                    case XmlPullParser.START_TAG:
                    	            
                    	tag = parser.getName();
                    	
                    	
                		if (tag.compareTo("item") == 0) {
                			i++;
                        }   
                		
                		if (tag.compareTo("guid") == 0) {
                        	isContent[0] = true;
                        }                
                		
                		if (tag.compareTo("title") == 0) {
                			if( parser.getAttributeCount() == 1){
                    			if(parser.getAttributeValue(0).equals("en")){
                            		isContent[1] = true;
                            	}else if(parser.getAttributeValue(0).equals("ko")){
                            		isContent[2] = true;
                            	}
 	                        }                        
                		}
                		if (tag.compareTo("author") == 0) {
                			if( parser.getAttributeCount() == 1){
	                			if(parser.getAttributeValue(0).equals("en")){
	                        		isContent[3] = true;
	                        	}else if(parser.getAttributeValue(0).equals("ko")){
	                        		isContent[4] = true;
	                        	}
                			}
                        }                   		
                		
                		if (tag.compareTo("pubDate") == 0) {
                        	isContent[5] = true;
                        }                		

                		if (tag.compareTo("description") == 0) {
                			if( parser.getAttributeCount() == 1){
	                			if(parser.getAttributeValue(0).equals("en")){
	                				isContent[6] = true;
	                        	}
	                			
	                			if(parser.getAttributeValue(0).equals("ko")){
	                        		isContent[7] = true;
	                        	}
                			}
                        }
                		
                		if (tag.compareTo("media:content") == 0) {
                			if( parser.getAttributeCount() == 4){
                				
	                			if(parser.getAttributeName(0).equals("url")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[8] = false;
	                        	}
	                			
	                			if(parser.getAttributeName(1).equals("type")){
	                        		arrList.add(parser.getAttributeValue(1));
	                        		isContent[9] = false;
	                        	}
	                			
	                			if(parser.getAttributeName(2).equals("duration")){
	                        		arrList.add(parser.getAttributeValue(2));
	                        		isContent[10] = false;
	                        	}
	                			
	                			if(parser.getAttributeName(3).equals("count")){
	                        		arrList.add(parser.getAttributeValue(3));
	                        		isContent[11] = false;
	                        	}
	                			
                			}
                        }
                		
                		if (tag.compareTo("media:location") == 0) {
                			
                			if( parser.getAttributeCount() == 2){
                				
	                			if(parser.getAttributeValue(1).equals("en")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[12] = false;
	                        	}
	                			
	                			if(parser.getAttributeValue(1).equals("ko")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[13] = false;
	                        	}
	                			
                			}
                        }   
                		
                		if (tag.compareTo("media:title") == 0) {
                			isContent[14] = true;
                        }                  		

                		if (tag.compareTo("media:credit") == 0 && parser.getAttributeCount() == 3 ) {
                			
                			
                			if(parser.getAttributeValue(2).equals("en")){
                        		if(parser.getAttributeValue(0).equals("owner")){
                        			isContent[15] = true;
                        		}else if(parser.getAttributeValue(0).equals("writer")){
                        			isContent[16] = true;
                        		}if(parser.getAttributeValue(0).equals("illustrator")){
                        			isContent[17] = true;
                        		}
                        	}else if(parser.getAttributeValue(2).equals("ko")){
                        		if(parser.getAttributeValue(0).equals("owner")){
                        			isContent[18] = true;
                        		}else if(parser.getAttributeValue(0).equals("writer")){
                        			isContent[19] = true;
                        		}if(parser.getAttributeValue(0).equals("illustrator")){
                        			isContent[20] = true;
                        		}
                        	}
                			
                        }    
                		
                		if (tag.compareTo("media:thumbnail") == 0) {
                			if( parser.getAttributeCount() == 1){
                				arrList.add(parser.getAttributeValue(0));
	                			
                			}                			
                			isContent[21] = false;
                        }    
                		
                		
                		
                        
                    break;
        		}
        		parserEvent = parser.next();
        	}
               
     	
        }catch( Exception e ){
        	showNotFoundXml() ;
       	 	Log.e("dd", "Error in network call", e);
        }    		
		
        //Log.e("inCountryList",""+inStoryList.size());
        
        Iterator<Integer> iter = inStoryList.keySet().iterator();
        
        
        while(iter.hasNext())
        {
            int nCount    = iter.next();
            
           ArrayList<Object> tmpList = new ArrayList<Object>();
            
            tmpList = inStoryList.get(nCount);
            
			/*
            for (int i = 0; i < tmpList.size(); i++){
            	 System.out.println("==nCount=="+nCount+"==i=="+i+"=="+tmpList.get(i));
            }
			*/
            
       	 	//System.out.println ( " value )  "+inList2.get(nCount)  ) ;             
           
        }
        
        return inStoryList;
       
	}	
	//++동화 리스트 xml 파싱하기 끝
	
	//++나라별 동화 리스트 xml 파싱하기 시작
	public HashMap<Integer, ArrayList<Object>> getXmlStoryImageResult(){
		
		boolean isContent[] = new boolean[22];
		String strGuid = "";
		HashMap<Integer, ArrayList<Object>> inStoryList = new HashMap<Integer, ArrayList<Object>>();
		ArrayList<Object> arrList = new ArrayList<Object>();
		
		//inList[0][0] = "111";

		try{
        	
        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();
        	
        	
        	InputStream stream = getInputStream(indexUrl);

        	
        	parser.setInput( stream, "utf-8" );
        	int parserEvent = parser.getEventType();
        	
        	
            String tag;
        	
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		  switch(parserEvent){
        		
	        		case XmlPullParser.TEXT:
	        			tag = parser.getName() ;
	        		
	                		if (isContent[0]) {
		                		strGuid = parser.getText();
	                        } 
                		
	                		if(isContent[2]){//++country
		        			}
	                		
	        				if(isContent[1]){//++media:thumbnail
		        			}
	        				
	        				if(isContent[3]){//++pubDate
	        					arrList.add(parser.getText());
		        			}

	        				if(isContent[6]){//++title_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[7]){//++title_ko
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[8]){//++author_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[9]){//++author_ko
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[10]){//++description_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[11]){//++description_ko
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[15]){//++media_title
	        					arrList.add(parser.getText());
		        			}
	        		
	        				if(isContent[16]){//++media_credit_owner_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[17]){//++media_credit_writer_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[18]){//++media_credit_illustrator_en
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[19]){//++media_credit_owner_ko
	        					arrList.add(parser.getText());
		        			}
	        				
	        				if(isContent[20]){//++media_credit_writer_ko
	        					arrList.add(parser.getText());
		        			}	
	        				
	        				if(isContent[21]){//++media_credit_illustrator_ko
	        					arrList.add(parser.getText());
		        			}	        				
	        				
	        		break;
                    case XmlPullParser.END_TAG:
                    	 tag = parser.getName();                    	 
                 		
                    	 if(tag.compareTo("item") == 0){
                    		 inStoryList.put(Integer.parseInt(strGuid),arrList);
                    		 
                    		 arrList = null;
                    		 arrList = new ArrayList<Object>();
                    		 
                    		 //Log.e("strGuid==","=="+strGuid);
                    	 }
                    	 
		        			//++false로 변경
                    	 for(int i= 0; i < 22; i++){
                    		 isContent[i] = false;
                    	 }
                    	 	
                    	 /*
                    	 	isContent[1] = false;
                    	 	isContent[2] = false;
                    	 	isContent[3] = false;
                    	 	isContent[4] = false; //++url
                    	 	isContent[5] = false; //++count
                    	 */	
		        		    
                     break;	        		
                    case XmlPullParser.START_TAG:
                    	            
                    	tag = parser.getName();
                    	
                	
                	
                		if (tag.compareTo("guid") == 0) {
                        	isContent[0] = true;
                        }
                		
                		if (tag.compareTo("title") == 0) {
                			if( parser.getAttributeCount() == 1){
                    			if(parser.getAttributeValue(0).equals("en")){
                            		isContent[6] = true;
                            	}else if(parser.getAttributeValue(0).equals("ko")){
                            		isContent[7] = true;
                            	}
 	                        }                        
                		}               		
                		
                		if (tag.compareTo("media:location") == 0) {
                			
                			if( parser.getAttributeCount() == 2){
                				
	                			if(parser.getAttributeValue(1).equals("en")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[2] = false;
	                        	}
	                			
	                			if(parser.getAttributeValue(1).equals("ko")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[14] = false;
	                        	}	                			
	                			
                			}
                        } 
                		
                		if (tag.compareTo("author") == 0) {
                			if( parser.getAttributeCount() == 1){
	                			if(parser.getAttributeValue(0).equals("en")){
	                        		isContent[8] = true;
	                        	}else if(parser.getAttributeValue(0).equals("ko")){
	                        		isContent[9] = true;
	                        	}
                			}
                        }                 		
                		
                		if (tag.compareTo("pubDate") == 0) {
                			//Log.e("pubDate",tag);
                        	isContent[3] = true;
                        }   
                		
                		if (tag.compareTo("description") == 0) {
                			if( parser.getAttributeCount() == 1){
	                			if(parser.getAttributeValue(0).equals("en")){
	                				isContent[10] = true;
	                        	}
	                			
	                			if(parser.getAttributeValue(0).equals("ko")){
	                        		isContent[11] = true;
	                        	}
                			}
                        }                		
                		if (tag.compareTo("media:content") == 0) {
                			if( parser.getAttributeCount() == 4){
                				
	                			if(parser.getAttributeName(0).equals("url")){
	                				arrList.add(parser.getAttributeValue(0));
	                        		isContent[4] = false;
	                        	}
	                			if(parser.getAttributeName(1).equals("type")){
	                        		arrList.add(parser.getAttributeValue(1));
	                        		isContent[12] = false;
	                        	}
	                			
	                			if(parser.getAttributeName(2).equals("duration")){
	                        		arrList.add(parser.getAttributeValue(2));
	                        		isContent[13] = false;
	                        	}
	                			
	                			
	                			if(parser.getAttributeName(3).equals("count")){
	                        		arrList.add(parser.getAttributeValue(3));
	                        		isContent[5] = false;
	                        	}
	                			
                			}
                        }
                		
                		if (tag.compareTo("media:title") == 0) {
                			isContent[15] = true;
                        }                  		

                		if (tag.compareTo("media:credit") == 0 && parser.getAttributeCount() == 3 ) {
                			
                			
                			if(parser.getAttributeValue(2).equals("en")){
                        		if(parser.getAttributeValue(0).equals("owner")){
                        			isContent[16] = true;
                        		}else if(parser.getAttributeValue(0).equals("writer")){
                        			isContent[17] = true;
                        		}if(parser.getAttributeValue(0).equals("illustrator")){
                        			isContent[18] = true;
                        		}
                        	}else if(parser.getAttributeValue(2).equals("ko")){
                        		if(parser.getAttributeValue(0).equals("owner")){
                        			isContent[19] = true;
                        		}else if(parser.getAttributeValue(0).equals("writer")){
                        			isContent[20] = true;
                        		}if(parser.getAttributeValue(0).equals("illustrator")){
                        			isContent[21] = true;
                        		}
                        	}
                			
                        }   
                		
                		if (tag.compareTo("media:thumbnail") == 0) {
                			if( parser.getAttributeCount() == 1){
                				arrList.add(parser.getAttributeValue(0));
	                			
                			}                			
                			isContent[1] = false;
                        }    
                		
              		
                		
                        
                    break;
        		}
        		parserEvent = parser.next();
        	}
               
     	
        }catch( Exception e ){
        	showNotFoundXml() ;
       	 	Log.e("dd", "Error in network call", e);
        }    		
		
        
        /*
        Log.e("StoryImage",""+inStoryList.size());
        
        Iterator<Integer> iter = inStoryList.keySet().iterator();
        
        
        while(iter.hasNext())
        {
            int nCount    = iter.next();
            
           String tmpList = "";
            
            tmpList = inStoryList.get(nCount);
            
            System.out.println("StoryOne==nCount=="+nCount+"=="+tmpList);
       	 	//System.out.println ( " value )  "+inList2.get(nCount)  ) ;             
           
        }
        */
        
        return inStoryList;
       
	}	
	//++나라별 동화 리스트 xml 파싱하기 끝	
	
	 private void showNotFoundXml() {
	    	Toast toast = Toast.makeText(mContext, "XML 파싱 에러", Toast.LENGTH_SHORT);
	    	toast.show();
	 }	
}
