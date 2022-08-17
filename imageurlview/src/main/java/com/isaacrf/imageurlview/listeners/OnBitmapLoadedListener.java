package com.isaacrf.imageurlview.listeners;

import android.graphics.Bitmap;

/**
 * Listener to get a callback when an URL image request asynctask is completed
 * 
 * @author Isaac
 *
 */
public interface OnBitmapLoadedListener {
	/**
	 * Callback to be invoked when Bitmap is successfully retrieved
	 * @param url URL of the loaded Bitmap
	 * @param bitmap Retrieved Bitmap
	 */
	void onBitmapLoaded(String url, Bitmap bitmap);
}
