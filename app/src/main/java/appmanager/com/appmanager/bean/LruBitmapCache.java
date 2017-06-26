package appmanager.com.appmanager.bean;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import appmanager.com.appmanager.MyApplication;

public class LruBitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;
	
	public static Bitmap cacheBitmap;

	public LruBitmapCache() {
		mMemoryCache = new LruCache<String, Bitmap>(MyApplication.memoryCacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mMemoryCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
	}

}
