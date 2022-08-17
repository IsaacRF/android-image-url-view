package com.isaacrf.imageurlview.main;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * {@link Bitmap} cache to easily store and retrieve Bitmap images
 * 
 * @author Isaac
 *
 */
public class BitmapCache {
	//Memory cache
	private LruCache<String, Bitmap> mMemoryCache;
	
	/**
	 * Basic constructor, builds the memory cache automatically
	 */
	public BitmapCache() {
		// Get max available VM memory, exceeding this amount will throw an
	    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
	    // int in its constructor.
	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = maxMemory / 8;
	    
	    //Initialize the memory cache
	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };	    
	}
	
	/**
	 * Add {@link Bitmap} to Memory cache identified by a key
	 * @param key ID of the {@link Bitmap} to retrieve it later
	 * @param bitmap {@link Bitmap} to be stored
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	/**
	 * Get Bitmap from Memory Cache by its key
	 * @param key ID of the {@link Bitmap} to be retrieved
	 * @return {@link Bitmap}
	 */
	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}

}
