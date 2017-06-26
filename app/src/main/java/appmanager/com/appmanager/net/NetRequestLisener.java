package appmanager.com.appmanager.net;

import com.android.volley.VolleyError;

public interface NetRequestLisener {
   public void success(String result);
   public void error(String error);

}
