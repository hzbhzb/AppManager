package appmanager.com.appmanager.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MyJsonRequest extends JsonObjectRequest {
	  
     
    public MyJsonRequest(int method, String url, JSONObject jsonRequest,
                         Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	@Override  
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {  
            response.headers.put("HTTP.CONTENT_TYPE", "utf-8");  
//            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));  
            String jsonString = new String(response.data,"utf-8");  
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        }  
        catch (UnsupportedEncodingException e) {  
            return Response.error(new ParseError(e));
        }  
        catch (JSONException je) {  
            return Response.error(new ParseError(je));
        }  
    }   

}  
