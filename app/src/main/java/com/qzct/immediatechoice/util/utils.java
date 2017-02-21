package com.qzct.immediatechoice.util;


import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class utils {
	private static JSONObject obj;

	public static JSONObject GetJson(String spec){

		try {
			URL url = new URL(spec);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(4000);
			int Code = con.getResponseCode();
			if(Code == 200){
				InputStream is = con.getInputStream();
				 String result = StreamTools.readFromStream(is);
				 obj = new JSONObject(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static JSONArray GetJsonArray(String spec){
		System.out.println("GetJsonArray");

		JSONArray jsonArray = null;
		//拿到一个Message对象，内存中如果没有就new一个，有就直接返回
		Message msg = Message.obtain(); 									
		
		try {
			System.out.println("try");
			//1.拿到URL
			URL url = new URL(spec);				
			//2.拿到Connection对象
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			//3.设置连接方式
			con.setRequestMethod("GET");									
			//4.设置连接超时时间
			con.setConnectTimeout(4000);									
			//5.拿到响应码(在这里连接了)
			int Code = con.getResponseCode();								
			//如果连接成功
			System.out.println(Code);
			System.out.println("code");
			if(Code == 200){												
				//拿到输入流
				 InputStream is = con.getInputStream();						
				 System.out.println("is");
				//从流中读取出String
				 String result = StreamTools.readFromStream(is);			
				 System.out.println(result);
				//拿到JSONObject对象
				 jsonArray = new JSONArray(result);		
				 System.out.println("jsonArray" + jsonArray);
				 System.out.println("array");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonArray;
		
	}

	public static String getTextFromStream(InputStream is){
		int len =0;
		byte[] b = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = is.read(b))!=-1) {
				bos.write(b, 0, len);
			}
			String text = new String(bos.toByteArray());
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
