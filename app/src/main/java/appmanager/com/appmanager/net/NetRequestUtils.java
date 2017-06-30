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

import appmanager.com.appmanager.EncriptUtils;
import appmanager.com.appmanager.MyApplication;
import appmanager.com.appmanager.bean.WsParams;


/**
 * Created by huangzhebin on 16/7/20.
 */
public class NetRequestUtils {


    //Thread thread;
   static  String responseResult;
	static EncriptUtils encriptUtils;
   final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		 
       public boolean verify(String hostname, SSLSession session) {
           return true;
       }
   };


	public static void callMetroNetRequestPost(final NetRequestLisener listener) {


        String acc = "launcher_telpo";
		String pwd = "appmanager.com";
		WsParams ps = new WsParams();
		String ts = String.valueOf(System.currentTimeMillis());
		StringBuilder url = new StringBuilder("http://ws.test.dlj100.cn/app/launcher/apps?");////Config.metroUrl + "?p=" + URLEncoder.encode(p.get("p").replace(" ", "")) + "&appkey=" + URLEncoder.encode(p.get("appkey").replace(" ", "")) + "&token=" + token + "&sign=" + URLEncoder.encode(p.get("sign").replace(" ", ""));
		encriptUtils = new EncriptUtils(url.toString(), acc, pwd);
		url.append("a=" + acc + "&t=" + ts + "&s=" + encriptUtils.aesEncryp(ps.json(acc, ts)));
		StringRequest stringRequest = new StringRequest(Method.GET, url.toString(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						System.out.println("返回结果====" + encriptUtils.aesDecrypt(response));
						listener.success(encriptUtils.aesDecrypt(response));

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

	public static void callNetRequestPost(final JSONObject param, final boolean showLoading, final NetRequestLisener listener) {

		String url = "http://ws.test.dlj100.cn/app/launcher/apps";
		SsX509TrustManager.allowAllSSL();
		MyJsonRequest request = new MyJsonRequest(Method.POST, url, param, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub

				try {
					String resp_code = response.getString("resp_code");

					if (resp_code.equals("000000"))
						listener.success(response.toString());
					else {
						listener.error(response.toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

				listener.error("连接服务器异常");
			}
		});

		request.setRetryPolicy( new DefaultRetryPolicy(10000,
				1,//默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
		MyApplication.getRequestQueue().add(request);
	}


}
