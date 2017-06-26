package appmanager.com.appmanager.net;




import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Message;
import android.support.v4.util.Pair;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import appmanager.com.appmanager.MyApplication;


/**
 * Created by huangzhebin on 16/7/20.
 */
public class NetRequestUtils {


    //Thread thread;
   static  String responseResult;
   final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		 
       public boolean verify(String hostname, SSLSession session) {
           return true;
       }
   };


	public static void callMetroNetRequestPost(final NetRequestLisener listener) {



		String url = "http://ws.test.dlj100.cn/app/apk/latests";////Config.metroUrl + "?p=" + URLEncoder.encode(p.get("p").replace(" ", "")) + "&appkey=" + URLEncoder.encode(p.get("appkey").replace(" ", "")) + "&token=" + token + "&sign=" + URLEncoder.encode(p.get("sign").replace(" ", ""));

		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						System.out.println(response);
						listener.success(response);
//						try {
//
//							JSONObject json = new JSONObject(response);
//
//							String resp_code = json.getString("code");
//
//							String msg = json.getString("msg");
//							if (resp_code.equals("10000")) {
//								NetparamsHelper phelper = new NetparamsHelper();
//								String datajson = phelper.decodeServerResp(json.getString("data"));
//
//								listener.success(datajson);
//							} else {
//								listener.error(msg);
//								// activity.showToast(msg);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				if (error instanceof NoConnectionError) {

					listener.error("网络异常");
					Intent intent = new Intent("com.jiadu.update");
					intent.putExtra("status", 6);
					intent.putExtra("balance", "");

				}
			}
		}) {

		};
		MyApplication.getRequestQueue().add(stringRequest);

	}


}
